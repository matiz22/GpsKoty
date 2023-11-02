package helper

import kotlin.math.roundToLong
import kotlin.random.Random

fun generateRandomDouble(from: Double = 0.0, until: Double = 50.0, accuracy: Double = 0.01): Double {
    return (((Random.nextDouble() * (until - from)) + from) * (1.0 / accuracy)).roundToLong() / (1.0 / accuracy)
}

fun generateRandomInt(from: Int, until: Int): Int {
    return Random.nextInt(from, until)
}