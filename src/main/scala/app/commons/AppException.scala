package app.commons

import cats.data.NonEmptyList

sealed trait AppException extends Exception

case class PreconditionException(message: String) extends AppException
case class PreconditionExceptions(messages: NonEmptyList[String]) extends AppException
