package com.app.admin_chernobyl.repository

import com.app.admin_chernobyl.seller.Seller
import org.springframework.stereotype.Repository
import java.util.*

interface SellerRepository {

    fun save(seller: Seller): Seller

    fun findAll(): List<Seller>

    fun findById(id: UUID): Seller?

    fun deleteById(id: UUID)
}