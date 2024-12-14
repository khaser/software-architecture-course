package ru.mkn.krogue.model

import kotlin.random.Random

fun <T> Random.bernoulli(
    l: T,
    r: T,
): T {
    return if (this.nextBoolean()) {
        l
    } else {
        r
    }
}
