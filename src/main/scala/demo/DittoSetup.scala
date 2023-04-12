/*
 * Copyright © 2022-2023 by Pervasive Cats S.r.l.s.
 *
 * All Rights Reserved.
 */

package io.github.pervasivecats
package demo

import scala.jdk.OptionConverters.*
import scala.util.matching.Regex

import org.eclipse.ditto.base.model.common.HttpStatus
import org.eclipse.ditto.client.DittoClient
import org.eclipse.ditto.client.DittoClients
import org.eclipse.ditto.client.configuration.BasicAuthenticationConfiguration
import org.eclipse.ditto.client.configuration.WebSocketMessagingConfiguration
import org.eclipse.ditto.client.live.messages.RepliableMessage
import org.eclipse.ditto.client.messaging.AuthenticationProviders
import org.eclipse.ditto.client.messaging.MessagingProviders
import org.eclipse.ditto.client.options.Options
import org.eclipse.ditto.json.JsonObject
import org.eclipse.ditto.json.JsonValue
import org.eclipse.ditto.messages.model.MessageDirection
import org.eclipse.ditto.things.model.ThingId
import spray.json.JsNumber
import spray.json.JsObject
import spray.json.JsString
import spray.json.JsValue
import spray.json.enrichAny
import spray.json.enrichString

import demo.Entity.*
import AnyOps.===

object DittoSetup {

  private def sendReply(
    message: RepliableMessage[String, String],
    correlationId: String,
    status: HttpStatus,
    payload: String
  ): Unit = message.reply().httpStatus(status).correlationId(correlationId).payload(payload).send()

  private def handleCartMessage(
    message: RepliableMessage[String, String],
    direction: MessageDirection,
    messageHandler: (RepliableMessage[String, String], Long, Long, String, Seq[JsValue]) => Unit,
    payloadFields: String*
  ): Unit = {
    val thingIdMatcher: Regex = "cart-(?<cartId>[0-9]+)-(?<store>[0-9]+)".r
    (message.getDirection, message.getEntityId.getName, message.getCorrelationId.toScala) match {
      case (messageDirection, thingIdMatcher(cartId, store), Some(correlationId))
           if cartId.toLongOption.isDefined && store.toLongOption.isDefined && messageDirection === direction =>
        messageHandler(
          message,
          cartId.toLong,
          store.toLong,
          correlationId,
          message.getPayload.toScala.map(_.parseJson.asJsObject.getFields(payloadFields: _*)).getOrElse(Seq.empty[JsValue])
        )
      case _ => ()
    }
  }

  private def handleShelvingMessage(
    message: RepliableMessage[String, String],
    direction: MessageDirection,
    messageHandler: (RepliableMessage[String, String], Long, Long, Long, String, Seq[JsValue]) => Unit,
    payloadFields: String*
  ): Unit = {
    val thingIdMatcher: Regex = "shelving-(?<store>[0-9]+)-(?<shelvingGroup>[0-9]+)-(?<shelving>[0-9]+)".r
    (message.getDirection, message.getEntityId.getName, message.getCorrelationId.toScala) match {
      case (messageDirection, thingIdMatcher(store, shelvingGroup, shelving), Some(correlationId))
           if store.toLongOption.isDefined
           && shelvingGroup.toLongOption.isDefined
           && shelving.toLongOption.isDefined
           && messageDirection === direction =>
        messageHandler(
          message,
          store.toLong,
          shelvingGroup.toLong,
          shelving.toLong,
          correlationId,
          message.getPayload.toScala.map(_.parseJson.asJsObject.getFields(payloadFields: _*)).getOrElse(Seq.empty[JsValue])
        )
      case _ => ()
    }
  }

  private def handleStoreMessage(
    message: RepliableMessage[String, String],
    direction: MessageDirection,
    messageHandler: (RepliableMessage[String, String], Long, String, Seq[JsValue]) => Unit,
    payloadFields: String*
  ): Unit = {
    val antiTheftThingIdMatcher: Regex = "antiTheftSystem-(?<store>[0-9]+)".r
    val dropSystemThingIdMatcher: Regex = "dropSystem-(?<store>[0-9]+)".r
    (message.getDirection, message.getEntityId.getName, message.getCorrelationId.toScala) match {
      case (messageDirection, antiTheftThingIdMatcher(store), Some(correlationId))
           if store.toLongOption.isDefined && messageDirection === direction =>
        messageHandler(
          message,
          store.toLong,
          correlationId,
          message.getPayload.toScala.map(_.parseJson.asJsObject.getFields(payloadFields: _*)).getOrElse(Seq.empty[JsValue])
        )
      case (messageDirection, dropSystemThingIdMatcher(store), Some(correlationId))
           if store.toLongOption.isDefined && messageDirection === direction =>
        messageHandler(
          message,
          store.toLong,
          correlationId,
          message.getPayload.toScala.map(_.parseJson.asJsObject.getFields(payloadFields: _*)).getOrElse(Seq.empty[JsValue])
        )
      case _ => ()
    }
  }

  @SuppressWarnings(Array("org.wartremover.warts.ToString"))
  private def simulateCart(client: DittoClient): Unit = {
    // Actions requests sent to the device
    client
      .live
      .registerForMessage[String, String](
        "demo_associate",
        "associate",
        classOf[String],
        (msg: RepliableMessage[String, String]) =>
          handleCartMessage(
            msg,
            MessageDirection.TO,
            (msg, cartId, store, correlationId, fields) =>
              fields match {
                case Seq(JsString(customer)) =>
                  client
                    .twin
                    .forId(ThingId.of(s"io.github.pervasivecats:cart-$cartId-$store"))
                    .putAttribute("customer", customer)
                    .toCompletableFuture
                    .get()
                  client
                    .twin
                    .forId(ThingId.of(s"io.github.pervasivecats:cart-$cartId-$store"))
                    .putAttribute("movable", true)
                    .toCompletableFuture
                    .get()
                  println(s"! [DITTO 🛒] Cart with id cart-$cartId-$store has been associated to customer $customer")
                  sendReply(msg, correlationId, HttpStatus.OK, ResultResponseEntity(()).toJson.compactPrint)
                case _ =>
                  sendReply(
                    msg,
                    correlationId,
                    HttpStatus.BAD_REQUEST,
                    ErrorResponseEntity(ValidationError("DittoError", "Fields were not the right ones")).toJson.compactPrint
                  )
              },
            "customer"
          )
      )
    client
      .live
      .registerForMessage(
        "demo_lock",
        "lock",
        classOf[String],
        (msg: RepliableMessage[String, String]) =>
          handleCartMessage(
            msg,
            MessageDirection.TO,
            (msg, cartId, store, correlationId, _) => {
              client
                .twin
                .forId(ThingId.of(s"io.github.pervasivecats:cart-$cartId-$store"))
                .putAttribute("customer", JsonValue.nullLiteral())
                .toCompletableFuture
                .get()
              client
                .twin
                .forId(ThingId.of(s"io.github.pervasivecats:cart-$cartId-$store"))
                .putAttribute("movable", false)
                .toCompletableFuture
                .get()
              println(s"! [DITTO 🛒] Cart with id cart-$cartId-$store has been locked")
              sendReply(
                msg,
                correlationId,
                HttpStatus.OK,
                ResultResponseEntity(()).toJson.compactPrint
              )
            }
          )
      )
    client
      .live
      .registerForMessage(
        "demo_raiseAlarm",
        "raiseAlarm",
        classOf[String],
        (msg: RepliableMessage[String, String]) =>
          handleCartMessage(
            msg,
            MessageDirection.TO,
            (msg, cartId, store, correlationId, _) => {
              println(s"! [DITTO 🛒] Cart with id cart-$cartId-$store is raising its alarm")
              sendReply(
                msg,
                correlationId,
                HttpStatus.OK,
                ResultResponseEntity(()).toJson.compactPrint
              )
            }
          )
      )
      // Events responses sent from the device
    client
      .live
      .registerForMessage(
        "demo_itemInsertedIntoCart",
        "itemInsertedIntoCart",
        classOf[String],
        (msg: RepliableMessage[String, String]) =>
          handleCartMessage(
            msg,
            MessageDirection.FROM,
            (_, cartId, store, _, _) => println(s"! [DITTO 🛒] Cart with thing id cart-$cartId-$store has now an item in it")
          )
      )
    client
      .live
      .registerForMessage(
        "demo_cartMoved",
        "cartMoved",
        classOf[String],
        (msg: RepliableMessage[String, String]) =>
          handleCartMessage(
            msg,
            MessageDirection.FROM,
            (_, cartId, store, _, _) => println(s"! [DITTO 🛒] Cart with thing id cart-$cartId-$store was moved")
          )
      )
  }

  private def simulateStore(client: DittoClient): Unit = {
    client
      .twin
      .create(
        JsonObject
          .newBuilder
          .set(
            "thingId",
            "io.github.pervasivecats:shelving-0-0-0"
          )
          .set("definition", "https://raw.githubusercontent.com/pervasive-cats/toys-store-stores/main/shelving.jsonld")
          .set(
            "attributes",
            JsonObject
              .newBuilder
              .set("store", 0)
              .set("shelvingGroup", 0)
              .set("id", 0)
              .build
          )
          .build
      )
      .toCompletableFuture
      .get()
    client
      .twin
      .create(
        JsonObject
          .newBuilder
          .set("thingId", "io.github.pervasivecats:antiTheftSystem-0")
          .set("definition", "https://raw.githubusercontent.com/pervasive-cats/toys-store-stores/main/antiTheftSystem.jsonld")
          .set("attributes", JsonObject.newBuilder.set("store", 0).build)
          .build
      )
      .toCompletableFuture
      .get()
    client
      .twin
      .create(
        JsonObject
          .newBuilder
          .set("thingId", "io.github.pervasivecats:dropSystem-0")
          .set("definition", "https://raw.githubusercontent.com/pervasive-cats/toys-store-stores/main/dropSystem.jsonld")
          .set("attributes", JsonObject.newBuilder.set("store", 0).build)
          .build
      )
      .toCompletableFuture
      .get()
      // Actions requests sent to the devices
    client
      .live
      .registerForMessage[String, String](
        "demo_raiseAlarm",
        "raiseAlarm",
        classOf[String],
        (msg: RepliableMessage[String, String]) =>
          handleStoreMessage(
            msg,
            MessageDirection.TO,
            (msg, storeId, correlationId, _) => {
              println(s"! [DITTO 🚨] Anti-theft system in store $storeId has raised its alarm")
              sendReply(
                msg,
                correlationId,
                HttpStatus.OK,
                ResultResponseEntity(()).toJson.compactPrint
              )
            }
          )
      )
    client
      .live
      .registerForMessage[String, String](
        "ditto_actor_showItemData",
        "showItemData",
        classOf[String],
        (msg: RepliableMessage[String, String]) =>
          handleStoreMessage(
            msg,
            MessageDirection.TO,
            (msg, storeId, correlationId, fields) =>
              fields match {
                case Seq(JsNumber(amount), JsString(currency), JsString(description), JsString(name)) =>
                  println(s"! [DITTO 💼] Drop system in store $storeId is displaying the following item")
                  println(s"! [DITTO 💼] (Name: $name, Description: $description, Price: (Amount: $amount, Currency: $currency))")
                  sendReply(
                    msg,
                    correlationId,
                    HttpStatus.OK,
                    ResultResponseEntity(()).toJson.compactPrint
                  )
                case _ =>
                  sendReply(
                    msg,
                    correlationId,
                    HttpStatus.BAD_REQUEST,
                    ErrorResponseEntity(ValidationError("DittoError", "Fields were not the right ones")).toJson.compactPrint
                  )
              },
            "amount",
            "currency",
            "description",
            "name"
          )
      )
      // Events responses sent from the devices
    client
      .live
      .registerForMessage[String, String](
        "demo_itemDetected",
        "itemDetected",
        classOf[String],
        (msg: RepliableMessage[String, String]) =>
          handleStoreMessage(
            msg,
            MessageDirection.FROM,
            (_, storeId, _, _) => println(s"! [DITTO 🚨] Anti-theft system in store $storeId detected an item")
          )
      )
    client
      .live()
      .registerForMessage[String, String](
        "demo_itemInsertedIntoDropSystem",
        "itemInsertedIntoDropSystem",
        classOf[String],
        (msg: RepliableMessage[String, String]) =>
          handleStoreMessage(
            msg,
            MessageDirection.FROM,
            (_, storeId, _, _) => println(s"! [DITTO 💼] Drop system in store $storeId detected an item")
          )
      )
    client
      .live()
      .registerForMessage[String, String](
        "demo_itemReturned",
        "itemReturned",
        classOf[String],
        (msg: RepliableMessage[String, String]) =>
          handleStoreMessage(
            msg,
            MessageDirection.FROM,
            (_, storeId, _, _) => println(s"! [DITTO 💼] Drop system in store $storeId returned an item")
          )
      )
    client
      .live()
      .registerForMessage[String, String](
        "demo_catalogItemLiftingRegistered",
        "catalogItemLiftingRegistered",
        classOf[String],
        (msg: RepliableMessage[String, String]) =>
          handleShelvingMessage(
            msg,
            MessageDirection.FROM,
            (_, storeId, shelvingGroupId, shelvingId, _, _) =>
              println(s"! [DITTO 🗄️] Shelving with thing id $storeId-$shelvingGroupId-$shelvingId lifted an item")
          )
      )
  }

  def apply(username: String, password: String): DittoClient = {
    val disconnectedDittoClient = DittoClients.newInstance(
      MessagingProviders.webSocket(
        WebSocketMessagingConfiguration
          .newBuilder
          .endpoint("ws://localhost:8080/ws/2")
          .connectionErrorHandler(e => {
            println("Error establishing connection to websocket endpoint")
            e.printStackTrace()
            sys.exit(1)
          })
          .build,
        AuthenticationProviders.basic(
          BasicAuthenticationConfiguration
            .newBuilder
            .username(username)
            .password(password)
            .build
        )
      )
    )
    val client: DittoClient =
      disconnectedDittoClient
        .connect
        .exceptionally { e =>
          disconnectedDittoClient.destroy()
          println("Error establishing connection to Ditto client")
          e.printStackTrace()
          sys.exit(1)
        }
        .toCompletableFuture
        .get()
    client
      .live
      .startConsumption(
        Options.Consumption.namespaces("io.github.pervasivecats")
      )
      .exceptionally { e =>
        disconnectedDittoClient.destroy()
        println("Error starting consumption for Ditto client at given namespace")
        e.printStackTrace()
        sys.exit(1)
      }
      .toCompletableFuture
      .get()
    simulateCart(client)
    simulateStore(client)
    client
  }
}
