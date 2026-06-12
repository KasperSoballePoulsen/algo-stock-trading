package dk.ksp.algotrading.exception

class BrokerRejectedException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)