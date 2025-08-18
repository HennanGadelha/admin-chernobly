package com.app.admin_chernobyl.repository

import com.app.admin_chernobyl.seller.Seller
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class SellerRepositoryImpl(
    private val jdbcTemplate: JdbcTemplate
) : SellerRepository {

    override fun save(seller: Seller): Seller {
        val sql = "INSERT INTO sellers (id, name) VALUES (?, ?)"
        jdbcTemplate.update(sql, seller.id, seller.name)
        return seller
    }

    override fun findAll(): List<Seller> {
        val sql = "SELECT id, name FROM sellers"
        return jdbcTemplate.query(sql, rowMapper)
    }

    override fun findById(id: UUID): Seller? {
        val sql = "SELECT id, name FROM sellers WHERE id = ?"
        return jdbcTemplate.query(sql, rowMapper, id).firstOrNull()
    }

    override fun deleteById(id: UUID) {
        val sql = "DELETE FROM sellers WHERE id = ?"
         val rows = jdbcTemplate.update(sql, id)
         if (rows == 0) throw EmptyResultDataAccessException(1)
    }

    private val rowMapper = RowMapper<Seller> { rs, _ ->
        Seller(
            id = rs.getObject("id", UUID::class.java),
            name = rs.getString("name")
        )
    }

}