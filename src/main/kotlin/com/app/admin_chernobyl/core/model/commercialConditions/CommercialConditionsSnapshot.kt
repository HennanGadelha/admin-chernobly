package com.app.admin_chernobyl.core.model.commercialConditions

import java.time.Instant
import java.util.*

data class CommercialConditionsSnapshot(
    val versionId: UUID,
    val sellerId: UUID,
    //val effectiveFrom: Instant,
    //val effectiveTo: Instant?
    val status: CommercialConditionsStatus,
    val commercialConditions: CommercialConditions,
    val createdAt: Instant,
    val createdBy: String?,
    val changeReason: String?,
    val previousVersionId :String?
)
