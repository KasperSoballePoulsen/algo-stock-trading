package dk.ksp.algotrading.exception

class BrokerRejectedException(
    message: String,
    val errorCode: String? = null,
    val modelState: Map<String, List<String>>? = null,
    cause: Throwable? = null
) : RuntimeException(message, cause)