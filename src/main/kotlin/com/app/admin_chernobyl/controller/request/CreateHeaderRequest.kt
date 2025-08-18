package com.app.admin_chernobyl.controller.request

import org.jetbrains.annotations.NotNull
import java.util.*

data class CreateHeaderRequest(
    @field:NotNull
    val sellerId: UUID,
    val createdBy: String?,
    val currentVersionId: UUID?
)
