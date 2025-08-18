package com.app.admin_chernobyl.core.model.commercialConditions

data class Points(
    val quantity: Int,
    val cashback: CashBackConfig,
    val isActive: Boolean
)
