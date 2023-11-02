@file:Suppress("MemberVisibilityCanBePrivate")

package models

import helper.generateRandomDouble
import tokens.PointTokens
import kotlin.math.pow
import kotlin.math.sqrt

data class Point(
    val x: Double,
    val y: Double
) {
    fun distanceTo(other: Point): Double {
        return sqrt((this.x - other.x).pow(2) + (this.y - other.y).pow(2))
    }

    companion object {
        fun generateRandom(
            xFrom: Double = PointTokens.RandomFrom,
            xUntil: Double = PointTokens.RandomUntil,
            xAccuracy: Double = PointTokens.RandomAccuracy,
            yFrom: Double = PointTokens.RandomFrom,
            yUntil: Double = PointTokens.RandomUntil,
            yAccuracy: Double = PointTokens.RandomAccuracy,
        ): Point {
            return Point(
                x = generateRandomDouble(xFrom, xUntil, xAccuracy),
                y = generateRandomDouble(yFrom, yUntil, yAccuracy),
            )
        }

        fun generateRandom(
            from: Double = PointTokens.RandomFrom,
            until: Double = PointTokens.RandomUntil,
            accuracy: Double = PointTokens.RandomAccuracy,
        ): Point {
            return generateRandom(
                xFrom = from,
                xUntil = until,
                xAccuracy = accuracy,
                yFrom = from,
                yUntil = until,
                yAccuracy = accuracy,
            )
        }
    }
}