package com.app.admin_chernobyl.controller

import com.app.admin_chernobyl.controller.request.CreateHeaderRequest
import com.app.admin_chernobyl.controller.request.SaveConditionsRequest
import com.app.admin_chernobyl.core.model.commercialConditions.CommercialConditionsHeader
import com.app.admin_chernobyl.core.model.commercialConditions.CommercialConditionsSnapshot
import com.app.admin_chernobyl.service.CommercialConditionsService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.time.Instant

@RestController
@RequestMapping("/commercial-conditions")
class CommercialConditionsController(
    private val service: CommercialConditionsService
) {

    @PostMapping
    fun create(@Valid @RequestBody req: CreateHeaderRequest): ResponseEntity<CommercialConditionsHeader> {
        val header = CommercialConditionsHeader(
            sellerId = req.sellerId,
            createdAt = Instant.now(),
            createdBy = req.createdBy,
            currentVersionId = req.currentVersionId
        )

        val saved = service.createHeader(header)
        val location = URI.create("/commercial-conditions/headers/${saved.sellerId}")
        return ResponseEntity.created(location).body(saved)
    }

    @GetMapping
    fun listAll(): List<CommercialConditionsHeader> =
        service.listHeaders()


    @PostMapping("/snapshot")
    fun createSnapshot(@RequestBody request: SaveConditionsRequest): ResponseEntity<CommercialConditionsSnapshot> {
        val saved = service.saveConditions(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(saved)
    }
}