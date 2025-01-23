package com.mobile.animauxdomestiques.navigation

sealed class Screen(val route: String) {
    data object AnimalListScreen : Screen("animalList")
    data object AnimalScreen : Screen("animal")

    data object ActivityListScreen : Screen("activityList")
    data object ActivityScreen : Screen("activity")

    data object SpecieListScreen : Screen("specieList")
    data object SpecieScreen : Screen("specie")

    data object SearchScreen : Screen("search")

    data object SettingsScreen : Screen("settings")
}