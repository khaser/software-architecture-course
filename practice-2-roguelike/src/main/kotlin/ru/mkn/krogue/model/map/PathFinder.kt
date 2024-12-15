package ru.mkn.krogue.model.map

import java.util.*

fun Map.findPath(
    start: Position,
    fin: Position,
): List<Position>? {
    val prev = mutableMapOf(Pair(start, start))
    val queue: Queue<Position> = LinkedList<Position>()
    queue.offer(start)
    while (queue.isNotEmpty() && !prev.contains(fin)) {
        val curTile = queue.poll()
        getFreeAdjacentTiles(curTile).forEach {
            if (!prev.contains(it)) {
                prev[it] = curTile
                queue.offer(it)
            }
        }
    }

    if (!prev.contains(fin)) return null
    val res = mutableListOf<Position>()
    var cur = fin
    while (cur != start) {
        res.add(cur)
        cur = prev[cur]!!
    }
    return res.reversed()
}
