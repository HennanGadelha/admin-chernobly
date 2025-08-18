package com.app.admin_chernobyl.core.model.commercialConditions

import java.time.Instant
import java.util.*

data class CommercialConditionsHeader(
    val sellerId: UUID,
    val createdAt: Instant,
    val createdBy: String?,
    val currentVersionId: UUID?
)
