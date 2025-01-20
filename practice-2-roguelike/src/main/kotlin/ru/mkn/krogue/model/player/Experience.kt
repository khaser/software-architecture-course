package ru.mkn.krogue.model.player

import ru.mkn.krogue.model.game.Config

enum class LevelUPStat {
    HP,
    ATK,
    DEF,
}

class Experience {
    var points: Int = 0
        private set

    var level: Int = 0
        private set

    var levelPoints: Int = 0

    fun getPoints(points: Int) {
        this.points += points
        if (Config.Player.Experience.pointsToUpgrade(level) <= this.points) {
            this.points = 0
            level += 1
            levelPoints += 1
        }
    }
}
