import models.GpsContainer
import models.Path
import models.Point

const val N: Int = 10
const val K: Int = 4

fun main() {
    val points = MutableList(N) {
        Point.generateRandom()
    }

    val paths = mutableListOf<Path>()

    val gpsContainer = GpsContainer(points = points, paths = paths)

    gpsContainer.saveToFile("cosik.txt")

    val loadedGpsContainer = GpsContainer.loadFromFile("cosik.txt")

    loadedGpsContainer.points.forEach {
        println(it)
    }

    loadedGpsContainer.paths.forEach {
        println(it)
    }
}
