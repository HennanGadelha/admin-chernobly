package com.app.admin_chernobyl.core.model.commercialConditions

import java.math.BigDecimal

data class CreditCard(
    val minInstallments: Int,
    val maxInstallments: Int,
    val valueMinInstallments: BigDecimal,
    val cahbackConfig: CashBackConfig
)
