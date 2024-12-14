package ru.mkn.krogue.model.map

import org.hexworks.zircon.api.data.Size

fun Size.fetchModelPositions(): Iterable<Position> =
    (0 until this.height).flatMap { y ->
        (0 until this.width).map { x ->
            Position(x, y)
        }
    }.asIterable()
