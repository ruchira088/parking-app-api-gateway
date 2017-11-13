package exceptions

case class HttpResponseException(statusCode: Int, errorMessage: String) extends Exception
