package com.app.admin_chernobyl.repository

import com.app.admin_chernobyl.core.model.commercialConditions.CommercialConditionsHeader
import com.app.admin_chernobyl.core.model.commercialConditions.CommercialConditionsSnapshot
import java.util.*

interface CommercialConditionsRepository {


    fun createCommercialConditionsHeader(header: CommercialConditionsHeader): CommercialConditionsHeader

    fun findAllCommercialConditionsHeader(): List<CommercialConditionsHeader>

    fun findHeaderBySellerId(sellerId: UUID): CommercialConditionsHeader?

    fun updateHeaderCurrentVersion(sellerId: UUID, newVersionId: UUID): Int

    fun findCurrentSnapshotBySellerId(sellerId: UUID): CommercialConditionsSnapshot?

    fun insertSnapshot(snapshot: CommercialConditionsSnapshot)

    fun inactivateSnapshot(versionId: UUID): Int

    fun findAllSnapshotsBySellerId(sellerId: UUID): List<CommercialConditionsSnapshot>

}