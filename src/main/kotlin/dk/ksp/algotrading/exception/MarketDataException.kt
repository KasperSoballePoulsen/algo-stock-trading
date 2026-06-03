package dk.ksp.algotrading.exception

class MarketDataException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)