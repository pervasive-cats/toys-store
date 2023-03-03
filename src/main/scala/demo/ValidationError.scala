/*
 * Copyright © 2022-2023 by Pervasive Cats S.r.l.s.
 *
 * All Rights Reserved.
 */

package io.github.pervasivecats
package demo

type Validated[A] = Either[ValidationError, A]

final case class ValidationError(tpe: String, message: String)
