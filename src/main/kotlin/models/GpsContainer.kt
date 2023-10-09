package models

import Point
import java.io.File
import java.time.LocalDateTime

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