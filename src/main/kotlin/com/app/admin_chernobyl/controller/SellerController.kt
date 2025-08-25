package com.app.admin_chernobyl.controller

import com.app.admin_chernobyl.controller.request.SellerRequest
import com.app.admin_chernobyl.core.model.commercialConditions.CommercialConditionsSnapshot
import com.app.admin_chernobyl.seller.Seller
import com.app.admin_chernobyl.service.SellerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.*

@RestController
@RequestMapping("/sellers")
class SellerController(
    private val sellerService: SellerService
) {

    @PostMapping
    fun save(@RequestBody request: SellerRequest): ResponseEntity<Seller> {
        val seller = Seller(UUID.randomUUID(), request.name)
        val saved = sellerService.save(seller)
        val location = URI.create("/sellers/${saved.id}")
        return ResponseEntity.created(location).body(saved)
    }

    @GetMapping
    fun findAll(): List<Seller> =
        sellerService.findaAll()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: UUID): ResponseEntity<Seller> =
        sellerService.findById(id)
            ?.let { ResponseEntity.ok(it) }

            ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: UUID) {
        sellerService.delete(id)
    }

    @GetMapping("/{sellerId}/commercial-conditions/snapshots")
    fun listSnapshotsBySeller(
        @PathVariable sellerId: UUID
    ): ResponseEntity<List<CommercialConditionsSnapshot>> {
        val snapshots = sellerService.findAllSnapshotsBySellerId(sellerId)
        return ResponseEntity.ok(snapshots)
    }

}