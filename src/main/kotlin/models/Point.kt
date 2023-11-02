@file:Suppress("MemberVisibilityCanBePrivate")

package models

import helper.generateRandomDouble
import tokens.PointTokens

data class Point(
    val x: Double,
    val y: Double
) {
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