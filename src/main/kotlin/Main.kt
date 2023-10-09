import models.GpsContainer
import models.Path

fun main(args: Array<String>) {
    val points = mutableListOf(Point(1.0, 2.0), Point(3.5, 4.2), Point(5.1, 6.7))
    val paths = mutableListOf(Path(points[0], points[1]), Path(points[1], points[2]))
    val gpsContainer = GpsContainer(points = points, paths = paths)
    gpsContainer.saveToFile("cosik.txt")
    val loadedGpsContainer = GpsContainer.loadFromFile("cosik.txt")
    loadedGpsContainer.points.forEach{
        println(it)
    }
    loadedGpsContainer.paths.forEach{
        println(it)
    }

}
