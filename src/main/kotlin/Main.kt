import helper.generateRandomInt
import models.GpsContainer
import models.Path
import models.Point
import models.findShortestPathWithIntermediatePointsFor

const val N: Int = 500
//const val K: Int = 100

fun main() {
    val K = generateRandomInt(0, N - 1)

    val points = MutableList(N) {
        Point.generateRandom()
    }

    val paths = mutableListOf<Path>()

    val gpsContainer = GpsContainer(points = points, paths = paths)

    gpsContainer.saveToFile("cosik.txt")

    val loadedGpsContainer = GpsContainer.loadFromFile("cosik.txt")

    val source = points[generateRandomInt(0, N - 1)]
    val destination = points[generateRandomInt(0, N - 1)]

    println("K: $K")
    println("source: $source")
    println("destination: $destination")

    val estimatedPath = loadedGpsContainer.findShortestPathWithIntermediatePointsFor(
        source = source,
        destination = destination,
        k = K,
    )

    println(estimatedPath)

    // may throw an exception if there are some points with exactly the same coordinates in the list
    // significantly faster, but usually returns a significantly worse estimate
    // should be used for high values of K and/or N
//    val estimatedFastPath = loadedGpsContainer.findFastShortestPathWithIntermediatePointsFor(
//        source = points[10],
//        destination = points[10],
//        k = K,
//    )
//
//    println(estimatedFastPath)
}