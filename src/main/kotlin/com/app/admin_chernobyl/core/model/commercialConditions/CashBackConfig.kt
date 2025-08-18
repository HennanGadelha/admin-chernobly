package com.app.admin_chernobyl.core.model.commercialConditions

import java.math.BigDecimal

data class CashBackConfig(
    val isActive: Boolean,
    val cpp: BigDecimal
)
