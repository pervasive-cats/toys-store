/*
 * Copyright © 2022-2023 by Pervasive Cats S.r.l.s.
 *
 * All Rights Reserved.
 */

package io.github.pervasivecats
package demo

import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt
import scala.io.StdIn
import scala.util.Failure
import scala.util.Success

import akka.actor.ActorSystem as ClassicActorSystem
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.HttpExt
import akka.http.scaladsl.client.RequestBuilding.Delete
import akka.http.scaladsl.client.RequestBuilding.Post
import akka.http.scaladsl.client.RequestBuilding.Put
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.unmarshalling.Unmarshal
import org.eclipse.ditto.client.DittoClient
import org.eclipse.ditto.json.JsonObject
import org.eclipse.ditto.things.model.Thing
import org.eclipse.ditto.things.model.ThingId
import spray.json.JsObject
import spray.json.enrichAny

import demo.Entity.*
import demo.Requests.*

object Demo extends SprayJsonSupport {

  @SuppressWarnings(Array("org.wartremover.warts.ToString"))
  private def initializeStore(
    httpClient: HttpExt,
    dittoClient: DittoClient,
    store: Long
  )(
    using
    ExecutionContext,
    ClassicActorSystem
  ): (Long, Long, Long, Long, Long) = {
    // Cart addition
    val cartAdditionEntity = Await.result(
      httpClient
        .singleRequest(
          Post(
            "http://localhost:8083/cart",
            CartAdditionEntity(store)
          )
        ),
      5.minutes
    )
    println("Cart addition succeeded")
    println(Await.result(Unmarshal(cartAdditionEntity.entity).to[ResultResponseEntity[String]], 5.minutes).result + "\n")
    val cartId = StdIn.readLong()
    println("Carts present as digital twins")
    println(
      dittoClient
        .twin()
        .forId(ThingId.of(s"io.github.pervasivecats:cart-$cartId-$store"))
        .retrieve()
        .toCompletableFuture
        .get()
        .toString
      + "\n"
    )
    // Item addition
    val itemCategoryAdditionEntity = Await.result(
      httpClient
        .singleRequest(
          Post(
            "http://localhost:8082/item_category",
            ItemCategoryAdditionEntity("Lego Bat-mobile", "A simple item in a store, nothing else.")
          )
        ),
      5.minutes
    )
    println("Item category addition succeeded")
    println(Await.result(Unmarshal(itemCategoryAdditionEntity.entity).to[ResultResponseEntity[String]], 5.minutes).result + "\n")
    val itemCategoryId = StdIn.readLong()
    val catalogItemAdditionEntity = Await.result(
      httpClient
        .singleRequest(
          Post(
            "http://localhost:8082/catalog_item",
            CatalogItemAdditionEntity(itemCategoryId, store, Price(59.99, "EUR"))
          )
        ),
      5.minutes
    )
    println("Catalog item addition succeeded")
    println(Await.result(Unmarshal(catalogItemAdditionEntity.entity).to[ResultResponseEntity[String]], 5.minutes).result + "\n")
    val catalogItemId = StdIn.readLong()
    val firstItemId = 0
    val firstItemAdditionEntity = Await.result(
      httpClient
        .singleRequest(
          Post(
            "http://localhost:8082/item",
            ItemAdditionEntity(firstItemId, catalogItemId, store)
          )
        ),
      5.minutes
    )
    println("First item addition succeeded")
    println(Await.result(Unmarshal(firstItemAdditionEntity.entity).to[ResultResponseEntity[String]], 5.minutes).result + "\n")
    StdIn.readLine()
    val secondItemId = 1
    val secondItemAdditionEntity = Await
      .result(
        httpClient
          .singleRequest(
            Post(
              "http://localhost:8082/item",
              ItemAdditionEntity(secondItemId, catalogItemId, store)
            )
          ),
        5.minutes
      )
    println("Second item addition succeeded")
    println(Await.result(Unmarshal(secondItemAdditionEntity.entity).to[ResultResponseEntity[String]], 5.minutes).result + "\n")
    StdIn.readLine()
    (cartId, itemCategoryId, catalogItemId, firstItemId, secondItemId)
  }

  private def customerLogin(httpClient: HttpExt)(using ExecutionContext, ClassicActorSystem): (String, String) = {
    val customer = "luigi@mail.com"
    val password = "Password1!"
    val customerRegistrationEntity = Await.result(
      httpClient
        .singleRequest(
          Post(
            "http://localhost:8081/customer",
            CustomerRegistrationEntity(customer, "luigi", "Luigi", "Rossi", password)
          )
        ),
      5.minutes
    )
    println("Customer registration succeeded")
    println(Await.result(Unmarshal(customerRegistrationEntity.entity).to[ResultResponseEntity[String]], 5.minutes).result + "\n")
    val customerLoginEntity = Await.result(
      httpClient.singleRequest(
        Put(
          "http://localhost:8081/customer/login",
          CustomerLoginEntity(customer, password)
        )
      ),
      5.minutes
    )
    println("Customer login succeeded")
    println(Await.result(Unmarshal(customerLoginEntity.entity).to[ResultResponseEntity[String]], 5.minutes).result + "\n")
    StdIn.readLine()
    (customer, password)
  }

  private def shutdown(
    httpClient: HttpExt,
    store: Long,
    cartId: Long,
    itemCategoryId: Long,
    catalogItemId: Long,
    firstItemId: Long,
    secondItemId: Long,
    customer: String,
    password: String
  )(
    using
    ExecutionContext,
    ClassicActorSystem
  ): Unit = {
    // Customer de-registration
    val customerDeregistrationEntity = Await.result(
      httpClient.singleRequest(
        Delete(
          "http://localhost:8081/customer",
          CustomerDeregistrationEntity(customer, password)
        )
      ),
      5.minutes
    )
    println("Customer deregistration succeeded")
    println(
      Await.result(Unmarshal(customerDeregistrationEntity.entity).to[ResultResponseEntity[String]], 5.minutes).result + "\n"
    )
    StdIn.readLine()

    // Items deletion
    val firstItemRemovalEntity = Await.result(
      httpClient.singleRequest(
        Delete(
          "http://localhost:8081/item",
          ItemRemovalEntity(firstItemId, catalogItemId, store)
        )
      ),
      5.minutes
    )
    println("First item removal succeeded")
    println(Await.result(Unmarshal(firstItemRemovalEntity.entity).to[ResultResponseEntity[String]], 5.minutes).result + "\n")
    val secondItemRemovalEntity = Await.result(
      httpClient.singleRequest(
        Delete(
          "http://localhost:8081/item",
          ItemRemovalEntity(secondItemId, catalogItemId, store)
        )
      ),
      5.minutes
    )
    println("Second item removal succeeded")
    println(Await.result(Unmarshal(secondItemRemovalEntity.entity).to[ResultResponseEntity[String]], 5.minutes).result + "\n")
    StdIn.readLine()

    // Catalog item removal
    val catalogItemRemovalEntity = Await.result(
      httpClient.singleRequest(
        Delete(
          "http://localhost:8081/item",
          CatalogItemRemovalEntity(catalogItemId, store)
        )
      ),
      5.minutes
    )
    println("Catalog item removal succeeded")
    println(Await.result(Unmarshal(catalogItemRemovalEntity.entity).to[ResultResponseEntity[String]], 5.minutes).result + "\n")
    StdIn.readLine()

    // Item category removal
    val itemCategoryRemovalEntity = Await.result(
      httpClient.singleRequest(
        Delete(
          "http://localhost:8081/item",
          ItemCategoryRemovalEntity(itemCategoryId)
        )
      ),
      5.minutes
    )
    println("Item category removal succeeded")
    println(Await.result(Unmarshal(itemCategoryRemovalEntity.entity).to[ResultResponseEntity[String]], 5.minutes).result + "\n")
    StdIn.readLine()

    // Cart removal
    val cartRemovalEntity = Await.result(
      httpClient.singleRequest(
        Delete(
          "http://localhost:8081/item",
          CartRemovalEntity(cartId, store)
        )
      ),
      5.minutes
    )
    println("Cart removal succeeded")
    println(Await.result(Unmarshal(cartRemovalEntity.entity).to[ResultResponseEntity[String]], 5.minutes).result + "\n")
    StdIn.readLine()
  }

  @main
  def main(dittoUsername: String, dittoPassword: String): Unit = {
    val actorSystem: ActorSystem[Nothing] = ActorSystem[Nothing](Behaviors.empty[Nothing], "root_actor")
    given ClassicActorSystem = actorSystem.classicSystem
    given ExecutionContext = actorSystem.executionContext

    val dittoClient: DittoClient = DittoSetup(dittoUsername, dittoPassword)
    val httpClient: HttpExt = Http()

    // Store initialization
    println("Store initialization")
    val store = 999
    val (cartId, itemCategoryId, catalogItemId, firstItemId, secondItemId) = initializeStore(httpClient, dittoClient, store)

    // Client registration
    val (customer, password) = customerLogin(httpClient)

    // Cart association
    val cartAssociationEntity = Await.result(
      httpClient.singleRequest(
        Put(
          "http://localhost:8083/cart/associate",
          CartAssociationEntity(cartId, store, customer)
        )
      ),
      5.minutes
    )
    println("Cart association succeeded")
    println(Await.result(Unmarshal(cartAssociationEntity.entity).to[ResultResponseEntity[String]], 5.minutes).result + "\n")
    StdIn.readLine()

    // Cart moved
    dittoClient
      .live()
      .forId(ThingId.of(s"io.github.pervasivecats:cart-$cartId-$store"))
      .message[String]()
      .from()
      .subject("cartMoved")
      .send()
    StdIn.readLine()

    // Catalog item lifting

    // First item insertion into cart
    dittoClient
      .live()
      .forId(ThingId.of(s"io.github.pervasivecats:cart-$cartId-$store"))
      .message()
      .from()
      .subject("itemInsertedIntoCart")
      .payload(
        JsonObject.of(
          JsObject(
            "catalogItem" -> catalogItemId.toJson,
            "itemId" -> firstItemId.toJson
          ).compactPrint
        )
      )
      .send()
    StdIn.readLine()

    // First item removal with drop system

    // Second item neared to anti-theft system

    // Cart locked
    val cartLockEntity = Await.result(
      httpClient.singleRequest(
        Put(
          "http://localhost:8083/cart/lock",
          CartLockEntity(cartId, store)
        )
      ),
      5.minutes
    )
    println("Cart lock succeeded")
    println(Await.result(Unmarshal(cartLockEntity.entity).to[ResultResponseEntity[String]], 5.minutes).result + "\n")
    StdIn.readLine()

    // System shutdown
    shutdown(httpClient, store, cartId, itemCategoryId, catalogItemId, firstItemId, secondItemId, customer, password)
  }
}
