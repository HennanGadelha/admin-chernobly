package com.app.admin_chernobyl.controller.request

import com.app.admin_chernobyl.core.model.commercialConditions.CommercialConditions
import java.util.*

data class SaveConditionsRequest(
    val sellerId: UUID,
    val firstVersion: Boolean,
    val commercialConditions: CommercialConditions,
    val createdBy: String?,
    val changeReason: String?
)
