package com.app.admin_chernobyl.service

import com.app.admin_chernobyl.controller.request.SaveConditionsRequest
import com.app.admin_chernobyl.core.model.commercialConditions.CommercialConditions
import com.app.admin_chernobyl.core.model.commercialConditions.CommercialConditionsHeader
import com.app.admin_chernobyl.core.model.commercialConditions.CommercialConditionsSnapshot
import com.app.admin_chernobyl.core.model.commercialConditions.CommercialConditionsStatus
import com.app.admin_chernobyl.repository.CommercialConditionsRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Service
class CommercialConditionsService(private val commercialConditionsRepository: CommercialConditionsRepository) {


    fun createHeader(header: CommercialConditionsHeader) = commercialConditionsRepository.createCommercialConditionsHeader(header)

    fun listHeaders() = commercialConditionsRepository.findAllCommercialConditionsHeader()

    @Transactional
    fun saveConditions(request: SaveConditionsRequest): CommercialConditionsSnapshot {

        val header = this.findHeaderBySellerId(request.sellerId)

        return if (this.resolveIsFirstVersion(request)) {

            val newSnapshot = CommercialConditionsSnapshot(
                versionId = UUID.randomUUID(),
                sellerId = request.sellerId,
                status = CommercialConditionsStatus.ACTIVE,
                commercialConditions = request.commercialConditions,
                createdAt = Instant.now(),
                createdBy = request.createdBy,
                changeReason = request.changeReason,
                previousVersionId = null
            )

            insertSnapshot(newSnapshot)
            updateHeaderCurrentVersion(request, newSnapshot)
            newSnapshot

        } else {

            val current = commercialConditionsRepository.findCurrentSnapshotBySellerId(request.sellerId)
                ?: error("No current snapshot for sellerId=${request.sellerId}")

            val newSnapshot = CommercialConditionsSnapshot(
                versionId = UUID.randomUUID(),
                sellerId = request.sellerId,
                status = CommercialConditionsStatus.ACTIVE,
                commercialConditions = request.commercialConditions,
                createdAt = Instant.now(),
                createdBy = request.createdBy,
                changeReason = request.changeReason,
                previousVersionId = current.versionId.toString()
            )

            insertSnapshot(newSnapshot)
            commercialConditionsRepository.inactivateSnapshot(current.versionId)
            updateHeaderCurrentVersion(request, newSnapshot)
            newSnapshot
        }
    }

    private fun updateHeaderCurrentVersion(
        request: SaveConditionsRequest,
        newSnapshot: CommercialConditionsSnapshot
    ) {
        commercialConditionsRepository.updateHeaderCurrentVersion(request.sellerId, newSnapshot.versionId)
    }

    private fun insertSnapshot(newSnapshot: CommercialConditionsSnapshot) {
        commercialConditionsRepository.insertSnapshot(newSnapshot)
    }

    private fun findHeaderBySellerId(sellerId: UUID):  CommercialConditionsHeader {
      return  commercialConditionsRepository.findHeaderBySellerId(sellerId)
            ?: error("Header not found for sellerId=${sellerId}")
    }

    private fun resolveIsFirstVersion(request: SaveConditionsRequest): Boolean{
        val current = commercialConditionsRepository.findCurrentSnapshotBySellerId(request.sellerId)
        return request.firstVersion && current == null
    }

}