package com.app.admin_chernobyl.core.model.commercialConditions

import java.time.Instant
import java.util.*

data class CommercialConditions(
    val creditCard: CreditCard,
    val pix: Pix,
    val points: Points,
)
