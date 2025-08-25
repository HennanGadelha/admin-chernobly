package com.app.admin_chernobyl.service

import com.app.admin_chernobyl.repository.CommercialConditionsRepository
import com.app.admin_chernobyl.repository.SellerRepository
import com.app.admin_chernobyl.seller.Seller
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class SellerService(
    private val sellerRepository: SellerRepository,
    private val commercialConditionsRepository: CommercialConditionsRepository
) {

    fun save(seller: Seller) = sellerRepository.save(seller)

    fun findaAll() = sellerRepository.findAll()

    fun findById(id: UUID) = sellerRepository.findById(id)

    fun delete(id: UUID) = sellerRepository.deleteById(id)

    fun findAllSnapshotsBySellerId(sellerId: UUID) = commercialConditionsRepository.findAllSnapshotsBySellerId(sellerId)

}