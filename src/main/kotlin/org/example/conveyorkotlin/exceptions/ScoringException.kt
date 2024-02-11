package org.example.conveyorkotlin.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
class ScoringException(message: String?) : RuntimeException(message) {
    companion object {
        private const val serialVersionUID = 1L
    }
}