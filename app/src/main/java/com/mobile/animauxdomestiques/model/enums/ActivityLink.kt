package com.mobile.animauxdomestiques.model.enums

enum class ActivityLink(private val displayName : String ) {
    DEFAULT("Défaut"),
    SPECIE("Espèce"),
    ANIMAL("Animal");

    override fun toString():String{
        return displayName
    }

}