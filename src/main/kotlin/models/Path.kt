@file:Suppress("MemberVisibilityCanBePrivate")

package models

import tokens.PointTokens

data class Path(
    val startPoint: Point,
    val endPoint: Point
) {
    companion object {
        fun generateRandom(
            startPointXFrom: Double = PointTokens.RandomFrom,
            startPointXUntil: Double = PointTokens.RandomUntil,
            startPointXAccuracy: Double = PointTokens.RandomAccuracy,
            startPointYFrom: Double = PointTokens.RandomFrom,
            startPointYUntil: Double = PointTokens.RandomUntil,
            startPointYAccuracy: Double = PointTokens.RandomAccuracy,
            endPointXFrom: Double = PointTokens.RandomFrom,
            endPointXUntil: Double = PointTokens.RandomUntil,
            endPointXAccuracy: Double = PointTokens.RandomAccuracy,
            endPointYFrom: Double = PointTokens.RandomFrom,
            endPointYUntil: Double = PointTokens.RandomUntil,
            endPointYAccuracy: Double = PointTokens.RandomAccuracy,
        ): Path {
            return Path(
                startPoint = Point.generateRandom(
                    xFrom = startPointXFrom,
                    xUntil = startPointXUntil,
                    xAccuracy = startPointXAccuracy,
                    yFrom = startPointYFrom,
                    yUntil = startPointYUntil,
                    yAccuracy = startPointYAccuracy,
                ),
                endPoint = Point.generateRandom(
                    xFrom = endPointXFrom,
                    xUntil = endPointXUntil,
                    xAccuracy = endPointXAccuracy,
                    yFrom = endPointYFrom,
                    yUntil = endPointYUntil,
                    yAccuracy = endPointYAccuracy,
                ),
            )
        }

        fun generateRandom(
            startPointFrom: Double = PointTokens.RandomFrom,
            startPointUntil: Double = PointTokens.RandomUntil,
            startPointAccuracy: Double = PointTokens.RandomAccuracy,
            endPointFrom: Double = PointTokens.RandomFrom,
            endPointUntil: Double = PointTokens.RandomUntil,
            endPointAccuracy: Double = PointTokens.RandomAccuracy,
        ): Path {
            return generateRandom(
                startPointXFrom = startPointFrom,
                startPointXUntil = startPointUntil,
                startPointXAccuracy = startPointAccuracy,
                startPointYFrom = startPointFrom,
                startPointYUntil = startPointUntil,
                startPointYAccuracy = startPointAccuracy,
                endPointXFrom = endPointFrom,
                endPointXUntil = endPointUntil,
                endPointXAccuracy = endPointAccuracy,
                endPointYFrom = endPointFrom,
                endPointYUntil = endPointUntil,
                endPointYAccuracy = endPointAccuracy,
            )
        }

        fun generateRandom(
            pointFrom: Double = PointTokens.RandomFrom,
            pointUntil: Double = PointTokens.RandomUntil,
            pointAccuracy: Double = PointTokens.RandomAccuracy,
        ): Path {
            return generateRandom(
                startPointFrom = pointFrom,
                startPointUntil = pointUntil,
                startPointAccuracy = pointAccuracy,
                endPointFrom = pointFrom,
                endPointUntil = pointUntil,
                endPointAccuracy = pointAccuracy,
            )
        }
    }
}