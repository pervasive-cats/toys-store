/*
 * Copyright © 2022-2023 by Pervasive Cats S.r.l.s.
 *
 * All Rights Reserved.
 */

package io.github.pervasivecats
package demo

import scala.annotation.targetName

object AnyOps {

  extension [T](e: T) {

    @targetName("equals")
    @SuppressWarnings(Array("org.wartremover.warts.Equals"))
    def ===(other: T): Boolean = e == other

    @targetName("notEquals")
    def !==(other: T): Boolean = !(e === other)
  }
}
