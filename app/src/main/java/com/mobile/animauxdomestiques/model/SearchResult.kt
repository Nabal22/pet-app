package com.mobile.animauxdomestiques.model

import androidx.navigation.NavHostController

data class SearchResult(
    val headLineContent: String,
    val supportingContent: String,
    val type:String,
    val action: (NavHostController) -> Unit
)
