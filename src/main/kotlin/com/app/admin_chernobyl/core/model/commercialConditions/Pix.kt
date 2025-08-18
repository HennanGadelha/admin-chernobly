package com.app.admin_chernobyl.core.model.commercialConditions

import java.math.BigDecimal

data class Pix(
    val value: BigDecimal,
    val cashback: CashBackConfig,
    val isActive: Boolean
)
