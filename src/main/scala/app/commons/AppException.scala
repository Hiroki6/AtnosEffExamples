package app.commons

sealed trait AppException extends Exception

case class PreconditionException(message: String) extends AppException
