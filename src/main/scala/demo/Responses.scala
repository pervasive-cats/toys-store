/*
 * Copyright © 2022-2023 by Pervasive Cats S.r.l.s.
 *
 * All Rights Reserved.
 */

package io.github.pervasivecats
package demo

import spray.json.DefaultJsonProtocol
import spray.json.JsBoolean
import spray.json.JsNull
import spray.json.JsNumber
import spray.json.JsObject
import spray.json.JsString
import spray.json.JsValue
import spray.json.JsonFormat
import spray.json.deserializationError
import spray.json.enrichAny

import demo.Requests.*

object Responses extends DefaultJsonProtocol {

  case class Cart(id: Long, store: Long, movable: Boolean, customer: Option[String])

  given JsonFormat[Cart] with {

    override def read(json: JsValue): Cart = json.asJsObject.getFields("id", "store", "movable", "customer") match {
      case Seq(JsNumber(id), JsNumber(store), JsBoolean(movable), JsString(customer)) if id.isValidLong && store.isValidLong =>
        Cart(id.longValue, store.longValue, movable, Some(customer))
      case Seq(JsNumber(id), JsNumber(store), JsBoolean(movable), JsNull) if id.isValidLong && store.isValidLong =>
        Cart(id.longValue, store.longValue, movable, None)
      case _ => deserializationError(msg = "Json format is not valid")
    }

    override def write(cart: Cart): JsValue = JsObject(
      "id" -> cart.id.toJson,
      "store" -> cart.store.toJson,
      "movable" -> cart.movable.toJson,
      "customer" -> cart.customer.toJson
    )
  }

  case class ItemCategory(id: Long, name: String, description: String)

  given JsonFormat[ItemCategory] with {

    override def read(json: JsValue): ItemCategory = json.asJsObject.getFields("id", "name", "description") match {
      case Seq(JsNumber(id), JsString(name), JsString(description)) if id.isValidLong =>
        ItemCategory(id.longValue, name, description)
      case _ => deserializationError(msg = "Json format is not valid")
    }

    override def write(itemCategory: ItemCategory): JsValue = JsObject(
      "id" -> itemCategory.id.toJson,
      "name" -> itemCategory.name.toJson,
      "description" -> itemCategory.description.toJson
    )
  }

  case class CatalogItem(id: Long, category: Long, store: Long, price: Price, count: Long)

  given JsonFormat[CatalogItem] with {

    override def read(json: JsValue): CatalogItem =
      json.asJsObject.getFields("id", "category", "store", "price", "count") match {
        case Seq(JsNumber(id), JsNumber(category), JsNumber(store), price, JsNumber(count))
             if id.isValidLong && category.isValidLong && store.isValidLong && count.isValidLong =>
          CatalogItem(id.longValue, category.longValue, store.longValue, price.convertTo[Price], count.longValue)
        case _ => deserializationError(msg = "Json format is not valid")
      }

    override def write(catalogItem: CatalogItem): JsValue = JsObject(
      "id" -> catalogItem.id.toJson,
      "category" -> catalogItem.category.toJson,
      "store" -> catalogItem.store.toJson,
      "price" -> catalogItem.price.toJson,
      "count" -> catalogItem.count.toJson
    )
  }

  case class Item(id: Long, kind: CatalogItem, customer: Option[String], state: String)

  given JsonFormat[Item] with {

    override def read(json: JsValue): Item = json.asJsObject.getFields("id", "kind", "customer", "state") match {
      case Seq(JsNumber(id), kind, JsString(customer), JsString(state)) if id.isValidLong =>
        Item(id.longValue, kind.convertTo[CatalogItem], Some(customer), state)
      case Seq(JsNumber(id), kind, JsNull, JsString(state)) if id.isValidLong =>
        Item(id.longValue, kind.convertTo[CatalogItem], None, state)
      case _ => deserializationError(msg = "Json format is not valid")
    }

    override def write(item: Item): JsValue = JsObject(
      "id" -> item.id.toJson,
      "kind" -> item.kind.toJson,
      "customer" -> item.customer.toJson,
      "state" -> item.state.toJson
    )
  }

  case class Customer(username: String, email: String, firstName: String, lastName: String)

  given JsonFormat[Customer] with {

    override def read(json: JsValue): Customer = json.asJsObject.getFields("username", "email", "first_name", "last_name") match {
      case Seq(JsString(username), JsString(email), JsString(firstName), JsString(lastName)) =>
        Customer(username, email, firstName, lastName)
      case _ => deserializationError(msg = "Json format is not valid")
    }

    override def write(customer: Customer): JsValue = JsObject(
      "username" -> customer.username.toJson,
      "email" -> customer.email.toJson,
      "first_name" -> customer.firstName.toJson,
      "last_name" -> customer.lastName.toJson
    )
  }
}
