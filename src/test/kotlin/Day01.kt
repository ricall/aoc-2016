package org.ricall.day01

import org.junit.jupiter.api.Test
import org.ricall.day01.Direction.NORTH
import java.io.File
import kotlin.math.abs
import kotlin.test.assertEquals

data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
}
enum class Direction(val delta: Point) {
    NORTH(Point(0, -1)),
    EAST(Point(1, 0)),
    SOUTH(Point(0, 1)),
    WEST(Point(-1, 0));
    private fun directionFor(index: Int) = when {
        index < 0 -> entries[4 + index]
        else -> entries[index % 4]
    }
    fun turnLeft() = directionFor(entries.indexOf(this) - 1)
    fun turnRight() = directionFor(entries.indexOf(this) + 1)
}

fun followDirections(directions: String) = sequence {
    var currentDirection = NORTH
    var current = Point(0, 0)

    for (step in directions.split(", ")) {
        val blocks = step.substring(1).toInt()
        currentDirection = when(step.substring(0, 1)) {
            "L" -> currentDirection.turnLeft()
            "R" -> currentDirection.turnRight()
            else -> error("Unsupported direction $currentDirection")
        }
        repeat(blocks) {
            current += currentDirection.delta
            yield(current)
        }
    }
}

fun distanceOf(p: Point) = abs(p.x) + abs(p.y)

fun solvePartOne(directions: String) = distanceOf(followDirections(directions).last())

fun Sequence<Point>.firstDuplicate(): Point {
    val visited = mutableSetOf<Point>()
    for (p in this) {
        if (visited.contains(p)) {
            return p
        }
        visited.add(p)
    }
    error("no duplicates found")
}

fun solvePartTwo(directions: String) = distanceOf(followDirections(directions).firstDuplicate())

class Day01 {
    @Test
    fun `part 1 test data`() {
        assertEquals(5, solvePartOne("R2, L3"))
        assertEquals(2, solvePartOne("R2, R2, R2"))
        assertEquals(12, solvePartOne("R5, L5, R5, R3"))
    }

    @Test
    fun `part 1`() {
        assertEquals(271, solvePartOne(File("./inputs/day01.txt").readText()))
    }

    @Test
    fun `part 2 test data`() {
        assertEquals(4, solvePartTwo("R8, R4, R4, R8"))
    }

    @Test
    fun `part 2`() {
        assertEquals(153, solvePartTwo(File("./inputs/day01.txt").readText()))
    }
}