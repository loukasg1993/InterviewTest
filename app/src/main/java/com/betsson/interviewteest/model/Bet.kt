package com.betsson.interviewteest.model


open class Bet(
    var type: String,
    var sellIn: Int,
    var odds: Int,
    var image: String) {
    override fun toString(): String {
        return this.type + ", " + this.sellIn + ", " + this.odds
    }
    constructor(other: Bet) : this(
        other.type,
        other.sellIn,
        other.odds,
        other.image
    )
}
