package com.app.admin_chernobyl.repository

import com.app.admin_chernobyl.core.model.commercialConditions.CommercialConditions
import com.app.admin_chernobyl.core.model.commercialConditions.CommercialConditionsHeader
import com.app.admin_chernobyl.core.model.commercialConditions.CommercialConditionsSnapshot
import com.app.admin_chernobyl.core.model.commercialConditions.CommercialConditionsStatus
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.Timestamp
import java.util.*

@Repository
class CommercialConditionsImpl(
    private val jdbcTemplate: JdbcTemplate,
    private val objectMapper: ObjectMapper
) : CommercialConditionsRepository {


    override fun createCommercialConditionsHeader(header: CommercialConditionsHeader): CommercialConditionsHeader {
        val sql = """
            INSERT INTO commercial_conditions_header (seller_id, created_at, created_by, current_version_id)
            VALUES (?, ?, ?, ?)
        """.trimIndent()

        jdbcTemplate.update(
            sql,
            header.sellerId,
            Timestamp.from(header.createdAt),
            header.createdBy,
            header.currentVersionId
        )

        return header
    }

    override fun findAllCommercialConditionsHeader(): List<CommercialConditionsHeader> {
        val sql = "SELECT seller_id, created_at, created_by, current_version_id FROM commercial_conditions_header"

        return jdbcTemplate.query(sql) { rs, _ ->
            CommercialConditionsHeader(
                sellerId = UUID.fromString(rs.getString("seller_id")),
                createdAt = rs.getTimestamp("created_at").toInstant(),
                createdBy = rs.getString("created_by"),
                currentVersionId = rs.getString("current_version_id")?.let { UUID.fromString(it) }
            )
        }
    }

    override fun findHeaderBySellerId(sellerId: UUID): CommercialConditionsHeader? {
        val sql = """
            SELECT seller_id, created_at, created_by, current_version_id
            FROM commercial_conditions_header
            WHERE seller_id = ?
        """.trimIndent()

        return jdbcTemplate.query(sql, headerMapper, sellerId).firstOrNull()
    }

    override fun updateHeaderCurrentVersion(sellerId: UUID, newVersionId: UUID): Int {
        val sql = """
            UPDATE commercial_conditions_header
               SET current_version_id = ?
             WHERE seller_id = ?
        """.trimIndent()

        return jdbcTemplate.update(sql, newVersionId, sellerId)
    }


    override fun findCurrentSnapshotBySellerId(sellerId: UUID): CommercialConditionsSnapshot? {
        val sql = """
            SELECT s.version_id,
                   s.seller_id,
                   s.status,
                   s.commercial_conditions::text AS commercial_conditions,
                   s.created_at,
                   s.created_by,
                   s.change_reason,
                   s.previous_version_id::text AS previous_version_id
              FROM commercial_conditions_snapshot s
             JOIN commercial_conditions_header h
                ON h.seller_id = s.seller_id
               AND h.current_version_id = s.version_id
             WHERE s.seller_id = ?
        """.trimIndent()

        return jdbcTemplate.query(sql, snapshotMapper, sellerId).firstOrNull()
    }

    override fun insertSnapshot(snapshot: CommercialConditionsSnapshot) {
        val sql = """
            INSERT INTO commercial_conditions_snapshot (
                version_id,
                seller_id,
                status,
                commercial_conditions,
                created_at,
                created_by,
                change_reason,
                previous_version_id
            )
            VALUES (
                ?, ?, ?, ?::jsonb, ?, ?, ?, CAST(? AS UUID)
            )
        """.trimIndent()

        val json = objectMapper.writeValueAsString(snapshot.commercialConditions)

        jdbcTemplate.update(
            sql,
            snapshot.versionId,
            snapshot.sellerId,
            snapshot.status.name,
            json,
            Timestamp.from(snapshot.createdAt),
            snapshot.createdBy,
            snapshot.changeReason,
            snapshot.previousVersionId
        )
    }

    override fun inactivateSnapshot(versionId: UUID): Int {
        val sql = """
            UPDATE commercial_conditions_snapshot
               SET status = 'INACTIVE'
             WHERE version_id = ?
        """.trimIndent()

        return jdbcTemplate.update(sql, versionId)
    }

    private val headerMapper = RowMapper<CommercialConditionsHeader> { rs, _ ->
        CommercialConditionsHeader(
            sellerId = rs.getObject("seller_id", UUID::class.java),
            createdAt = rs.getTimestamp("created_at").toInstant(),
            createdBy = rs.getString("created_by"),
            currentVersionId = rs.getObject("current_version_id", UUID::class.java)
        )
    }

    private val snapshotMapper = RowMapper<CommercialConditionsSnapshot> { rs, _ ->
        CommercialConditionsSnapshot(
            versionId = rs.getObject("version_id", UUID::class.java),
            sellerId = rs.getObject("seller_id", UUID::class.java),
            status = CommercialConditionsStatus.valueOf(rs.getString("status")),
            commercialConditions = objectMapper.readValue(
                rs.getString("commercial_conditions"),
                CommercialConditions::class.java
            ),
            createdAt = rs.getTimestamp("created_at").toInstant(),
            createdBy = rs.getString("created_by"),
            changeReason = rs.getString("change_reason"),
            previousVersionId = rs.getString("previous_version_id")
        )
    }


}