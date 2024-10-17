package org.example.project.networking

import kotlinx.serialization.Serializable

@Serializable
data class CensoredText(
    val result: String
)
