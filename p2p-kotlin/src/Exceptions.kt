import java.lang.Exception

class SameInetAddressException (message : String) : Exception(message)

class NotDoubleAmount (message: String): Exception(message)

class NotEnoughFunds (message: String): Exception(message);

class NegativeFundsAmount (message: String) : Exception(message)

class NotParseableToDouble (message: String) : Exception(message);

class UserAlreadyExists (message: String) : Exception(message);