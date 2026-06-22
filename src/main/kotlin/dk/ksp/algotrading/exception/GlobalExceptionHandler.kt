package dk.ksp.algotrading.exception

import dk.ksp.algotrading.dto.response.ErrorResponseDTO
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BrokerRejectedException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleBrokerRejected(ex: BrokerRejectedException) = ErrorResponseDTO(ex.message, ex.errorCode)

}