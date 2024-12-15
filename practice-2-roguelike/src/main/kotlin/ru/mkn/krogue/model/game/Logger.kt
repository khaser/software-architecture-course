package ru.mkn.krogue.model.game

class Logger {
    private val lines: MutableList<String> = mutableListOf()

    fun log(line: String) {
        lines.add(line)
    }

    fun consumeLog(): List<String> {
        val consumed = lines.toList()
        lines.clear()
        return consumed
    }
}
