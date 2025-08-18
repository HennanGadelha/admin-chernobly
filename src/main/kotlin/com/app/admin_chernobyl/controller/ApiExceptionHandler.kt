package com.app.admin_chernobyl.controller

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(EmptyResultDataAccessException::class)
    fun handleEmptyResult(): ResponseEntity<Void> {
        return ResponseEntity.notFound().build()
    }
}
