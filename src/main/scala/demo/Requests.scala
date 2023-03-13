/*
 * Copyright © 2022-2023 by Pervasive Cats S.r.l.s.
 *
 * All Rights Reserved.
 */

package io.github.pervasivecats
package demo

import spray.json.DefaultJsonProtocol
import spray.json.JsonFormat
import spray.json.RootJsonFormat

object Requests extends DefaultJsonProtocol {

  final case class CartAdditionEntity(store: Long)

  given RootJsonFormat[CartAdditionEntity] = jsonFormat1(CartAdditionEntity.apply)

  final case class ItemCategoryAdditionEntity(name: String, description: String)

  given RootJsonFormat[ItemCategoryAdditionEntity] = jsonFormat2(ItemCategoryAdditionEntity.apply)

  final case class Price(amount: Double, currency: String)

  given JsonFormat[Price] = jsonFormat2(Price.apply)

  final case class CatalogItemAdditionEntity(itemCategoryId: Long, store: Long, price: Price)

  given RootJsonFormat[CatalogItemAdditionEntity] = jsonFormat3(CatalogItemAdditionEntity.apply)

  final case class ItemAdditionEntity(id: Long, kind: Long, store: Long)

  given RootJsonFormat[ItemAdditionEntity] = jsonFormat3(ItemAdditionEntity.apply)

  final case class CustomerRegistrationEntity(
    email: String,
    username: String,
    firstName: String,
    lastName: String,
    password: String
  )

  given RootJsonFormat[CustomerRegistrationEntity] = jsonFormat5(CustomerRegistrationEntity.apply)

  final case class CustomerLoginEntity(email: String, password: String)

  given RootJsonFormat[CustomerLoginEntity] = jsonFormat2(CustomerLoginEntity.apply)

  final case class CartAssociationEntity(cartId: Long, store: Long, customer: String)

  given RootJsonFormat[CartAssociationEntity] = jsonFormat3(CartAssociationEntity.apply)

  final case class CartLockEntity(cartId: Long, store: Long)

  given RootJsonFormat[CartLockEntity] = jsonFormat2(CartLockEntity.apply)

  final case class CustomerDeregistrationEntity(email: String, password: String)

  given RootJsonFormat[CustomerDeregistrationEntity] = jsonFormat2(CustomerDeregistrationEntity.apply)

  final case class ItemRemovalEntity(id: Long, kind: Long, store: Long)

  given RootJsonFormat[ItemRemovalEntity] = jsonFormat3(ItemRemovalEntity.apply)

  final case class CatalogItemRemovalEntity(id: Long, store: Long)

  given RootJsonFormat[CatalogItemRemovalEntity] = jsonFormat2(CatalogItemRemovalEntity.apply)

  final case class ItemCategoryRemovalEntity(id: Long)

  given RootJsonFormat[ItemCategoryRemovalEntity] = jsonFormat1(ItemCategoryRemovalEntity.apply)

  final case class CartRemovalEntity(id: Long, store: Long)

  given RootJsonFormat[CartRemovalEntity] = jsonFormat2(CartRemovalEntity.apply)

  final case class ItemShowEntity(id: Long, kind: Long, store: Long)

  given RootJsonFormat[ItemShowEntity] = jsonFormat3(ItemShowEntity.apply)
}
