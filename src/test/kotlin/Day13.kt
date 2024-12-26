package org.ricall.day13

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
}

private class Maze(val seed: Int) {
    private fun isValid(point: Point): Boolean {
        val (x, y) = point
        if (x < 0 || y < 0) {
            return false
        }
        val digits = (x * x + 3 * x + 2 * x * y + y + y * y + seed).toString(2)
        val oneBits = digits.count { it == '1' }
        return oneBits % 2 == 0
    }

    private fun neighbours(point: Point): List<Point> = listOf(
        Point(1, 0),
        Point(0, 1),
        Point(-1, 0),
        Point(0, -1)
    ).map { it + point }

    fun visitedSequence() = sequence {
        val start = Point(1, 1)
        val visited = mutableSetOf<Point>()
        var edges = listOf(start)
        visited.addAll(edges)
        while (edges.isNotEmpty()) {
            edges = edges.flatMap { neighbours(it).filter { isValid(it) && !visited.contains(it) } }
            visited.addAll(edges)
            yield(visited)
        }
        error("Should not reach this point")
    }

    fun solvePartOne(end: Point): Int {
        var length = 1
        for (visited in visitedSequence()) {
            if (visited.contains(end)) {
                return length
            }
            length++
        }
        error("invalid state")
    }

    fun solvePartTwo() = visitedSequence().take(50).last().size
}

class Day13 {
    @Test
    fun `part 1 test data`() {
        val result = Maze(10).solvePartOne(Point(7, 4))

        assertEquals(11, result)
    }

    @Test
    fun `part 1`() {
        val result = Maze(File("./inputs/day13.txt").readText().toInt())
            .solvePartOne(Point(31, 39))

        assertEquals(92, result)
    }

    @Test
    fun `part 2`() {
        val result = Maze(File("./inputs/day13.txt").readText().toInt()).solvePartTwo()

        assertEquals(124, result)
    }
}