package com.mobile.animauxdomestiques.model.enums

import java.time.DayOfWeek

enum class Day (private val displayName : String, val associatedJavaTimeValue : DayOfWeek){
    MONDAY("Lundi", DayOfWeek.MONDAY),
    TUESDAY("Mardi", DayOfWeek.TUESDAY),
    WEDNESDAY("Mercredi", DayOfWeek.WEDNESDAY),
    THURSDAY("Jeudi", DayOfWeek.THURSDAY),
    FRIDAY("Vendredi", DayOfWeek.FRIDAY),
    SATURDAY("Samedi", DayOfWeek.SATURDAY),
    SUNDAY("Dimanche", DayOfWeek.SUNDAY);

    override fun toString():String{
        return displayName;
    }
}