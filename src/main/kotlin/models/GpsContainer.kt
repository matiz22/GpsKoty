package models

import java.io.File
import java.time.LocalDateTime
import java.util.*

class GpsContainer(
    val points: MutableList<Point> = mutableListOf(),
    val paths: MutableList<Path> = mutableListOf()
) {
    fun saveToFile(fileName: String = LocalDateTime.now().toString()) {
        val writer = File(fileName).bufferedWriter()
        points.forEach { point ->
            writer.write("Point:${point.x}:${point.y}\n")
        }
        paths.forEach { path ->
            writer.write("Path:${path.startPoint.x}:${path.startPoint.y}:${path.endPoint.x}:${path.endPoint.y}\n")
        }
        writer.close()
    }

    companion object {
        fun loadFromFile(fileName: String): GpsContainer {
            val file = File(fileName)
            val gpsContainer = GpsContainer()
            file.useLines { lines ->
                lines.forEach { line ->
                    val tokens = line.split(':')
                    if (tokens[0] == "Point") {
                        val x = tokens[1].toDouble()
                        val y = tokens[2].toDouble()
                        gpsContainer.points.add(Point(x, y))
                    } else if (tokens[0] == "Path") {
                        val startX = tokens[1].toDouble()
                        val startY = tokens[2].toDouble()
                        val endX = tokens[3].toDouble()
                        val endY = tokens[4].toDouble()
                        gpsContainer.paths.add(Path(Point(startX, startY), Point(endX, endY)))
                    }
                }
            }
            return gpsContainer
        }
    }
}

/**
 * Estimates the shortest path by inserting a point that would increase the total length of the path the least
 * until there are [k] intermediate points
 * @param source [Point] from which the path starts
 * @param destination [Point] at which the path ends
 * @param k how many intermediate points need to be visited on the path from [source] to [destination]
 * @return null if [k] is too large or if either [source] or [destination] don't exist in the [GpsContainer],
 * otherwise [Pair] of total path distance, and the estimated path
 */
fun GpsContainer.findShortestPathWithIntermediatePointsFor(
    source: Point,
    destination: Point,
    k: Int
): Pair<Double, List<Point>>? {
    if (!points.contains(source) || !points.contains(destination) || k > points.size - 2) {
        return null
    }

    val distances: HashMap<Point, HashMap<Point, Double>> = HashMap()

    distances[source] = getAllDistancesFrom(source)!!
    distances[destination] = getAllDistancesFrom(destination)!!

    val path: MutableList<Point> = mutableListOf(source, destination)
    var currentDistance: Double = distances[source]!![destination]!!

    while (path.size - 2 < k) {
        var best: Optional<Pair<Point, Int>> = Optional.empty()

        for (itr in 1..<path.size) {
            val start = path[itr - 1]
            val end = path[itr]

            val distanceTable: PriorityQueue<GpsDistanceEntry> = PriorityQueue(distances[start]!!.size)

            distances[start]!!.forEach { (point, startDistance) ->
                if (!path.contains(point)) {
                    distanceTable.add(
                        GpsDistanceEntry(
                            point = point,
                            distance = startDistance + distances[end]!![point]!!
                        )
                    )
                }
            }

            val closest = distanceTable.remove()

            if (best == Optional.empty<Pair<Point, Int>>() || best.get().second > closest.distance) {
                best = Optional.of(Pair(closest.point, itr))
            }
        }

        val start = path[best.get().second - 1]
        val end = path[best.get().second]

        distances[best.get().first] = getAllDistancesFrom(best.get().first)!!

        currentDistance -= distances[start]!![end]!!
        currentDistance += distances[start]!![best.get().first]!!
        currentDistance += distances[best.get().first]!![end]!!

        path.add(best.get().second, best.get().first)
    }

    return Pair<Double, List<Point>>(currentDistance, path)
}

/**
 * Finds [k] points closest to a direct connection between [source] and [destination]
 * and then calls [GpsContainer.findShortestPathWithIntermediatePointsFor] on that
 * @param source [Point] from which the path starts
 * @param destination [Point] at which the path ends
 * @param k how many intermediate points need to be visited on the path from [source] to [destination]
 * @return null if [k] is too large or if either [source] or [destination] don't exist in the [GpsContainer],
 * otherwise [Pair] of total path distance, and the estimated path
 */
fun GpsContainer.findFastShortestPathWithIntermediatePointsFor(
    source: Point,
    destination: Point,
    k: Int
): Pair<Double, List<Point>>? {
    if (!points.contains(source) || !points.contains(destination) || k > points.size - 2) {
        return null
    }

    val preprocessedPoints = getKClosestToDirectConnectionBetween(source, destination, k)!!.toMutableList()

    preprocessedPoints.add(source)
    preprocessedPoints.add(destination)

    return GpsContainer(
        points = preprocessedPoints,
    ).findShortestPathWithIntermediatePointsFor(source, destination, k)
}

/**
 * @param parent [Point] from which to get all of the distances from
 * @return null if [parent] doesn't exist in the [GpsContainer],
 * otherwise [HashMap] of distances from [parent] to other [Point]'s contained in the [GpsContainer]
 */
private fun GpsContainer.getAllDistancesFrom(parent: Point): HashMap<Point, Double>? {
    if (!points.contains(parent)) {
        return null
    }

    val map: HashMap<Point, Double> = HashMap()

    points.forEach { child ->
        map[child] = parent.distanceTo(child)
    }

    return map
}

/**
 * @param source [Point] from which the path starts
 * @param destination [Point] at which the path ends
 * @return null if either [source] or [destination] don't exist in the [GpsContainer],
 * otherwise [List] of all [Point]s in the [GpsContainer] sorted by the distance to a direct connection between [source] and [destination]
 */
private fun GpsContainer.getPointsSortedByDistanceToDirectConnectionBetweet(
    source: Point,
    destination: Point
): List<Point>? {
    if (!points.contains(source) || !points.contains(destination)) {
        return null
    }

    val distances: HashMap<Point, HashMap<Point, Double>> = HashMap()

    distances[source] = getAllDistancesFrom(source)!!
    distances[destination] = getAllDistancesFrom(destination)!!

    val distanceTable: PriorityQueue<GpsDistanceEntry> = PriorityQueue()

    distances[source]!!.forEach { (point, startDistance) ->
        if (point != source) {
            distanceTable.add(
                GpsDistanceEntry(
                    point = point,
                    distance = startDistance + distances[destination]!![source]!!
                )
            )
        }
    }

    val result: MutableList<Point> = mutableListOf()

    var test = distanceTable.poll()
    while (test != null) {
        result.add(test.point)
        test = distanceTable.poll()
    }

    return result.toList()
}

/**
 * @param source [Point] from which the path starts
 * @param destination [Point] at which the path ends
 * @param k how many points to find
 * @return null if [k] is too large or if either [source] or [destination] don't exist in the [GpsContainer],
 * otherwise [List] of [k] [Point]s that are closest to a direct connection between [source] and [destination]
 */
private fun GpsContainer.getKClosestToDirectConnectionBetween(source: Point, destination: Point, k: Int): List<Point>? {
    if (!points.contains(source) || !points.contains(destination) || k > points.size - 2) {
        return null
    }

    val pointsByDistance = getPointsSortedByDistanceToDirectConnectionBetweet(source, destination)!!

    return pointsByDistance.slice(IntRange(0, k - 1))
}

/**
 * Abstraction for [point] and [distance] that compares by [distance]
 */
private data class GpsDistanceEntry(
    val point: Point,
    val distance: Double,
) : Comparable<GpsDistanceEntry> {
    override fun compareTo(other: GpsDistanceEntry): Int {
        return this.distance.compareTo(other.distance)
    }
}