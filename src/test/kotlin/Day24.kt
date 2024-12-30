package org.ricall.day24

import org.junit.jupiter.api.Test
import org.ricall.day24.MinimumRouteMode.NORMAL
import org.ricall.day24.MinimumRouteMode.RETURN
import java.io.File
import kotlin.test.assertEquals

private fun solvePartOne(input: String): Int {
    val distances = getDistanceMatrix(parseInput(input))

    return findMinimumRoute(distances, NORMAL)
}

private fun solvePartTwo(input: String): Int {
    val distances = getDistanceMatrix(parseInput(input))

    return findMinimumRoute(distances, RETURN)
}

private data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
}

private typealias Maze = Map<Point, Char>

private fun parseInput(input: String): Maze = buildMap {
    input.lines().forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            if (c != '#') {
                put(Point(x, y), c)
            }
        }
    }
}

private fun Maze.adjacentCells(position: Point) = listOf(Point(-1, 0), Point(0, -1), Point(1, 0), Point(0, 1))
    .map { delta -> position + delta }
    .filter { this[it] != null }

private enum class MinimumRouteMode { NORMAL, RETURN }

private fun findMinimumRoute(distances: Map<Int, Map<Int, Int>>, mode: MinimumRouteMode): Int {
    fun findMin(from: Int, remaining: Set<Int>, acc: Int): Int = when {
        remaining.isEmpty() -> acc + when (mode) {
            RETURN -> distances[from]!![0]!!
            else -> 0
        }
        else -> remaining.minOfOrNull { findMin(it, remaining - it, acc + distances[from]!![it]!!) }!!
    }
    return findMin(0, distances.keys - 0, 0)
}

private fun digitDistances(maze: Maze, start: Point, digits: Set<Int>) = buildMap {
    val queue = mutableListOf(start to 0)
    val visited = mutableSetOf(start)
    val remaining = digits.toMutableSet()
    while (queue.isNotEmpty() && remaining.isNotEmpty()) {
        val (pos, distance) = queue.removeFirst()
        val type = maze[pos]
        if (type?.isDigit() == true) {
            val digit = "$type".toInt()
            put(digit, distance)
            remaining -= digit
        }
        val next = maze.adjacentCells(pos).filter { it !in visited }
        visited.addAll(next)
        queue.addAll(next.map { it to distance + 1 })
    }
}

private fun findDigits(maze: Maze) = maze.entries
    .filter { (_, v) -> v.isDigit() }
    .associate { (k, v) -> "$v".toInt() to k }

private fun getDistanceMatrix(maze: Maze) = findDigits(maze).let {
    it.entries.associate { (digit, start) -> digit to digitDistances(maze, start, it.keys) }
}

class Day24 {
    private val TEST_DATA = """
        |###########
        |#0.1.....2#
        |#.#######.#
        |#4.......3#
        |###########""".trimMargin()

    @Test
    fun `part 1 test data`() {
        val result = solvePartOne(TEST_DATA)

        assertEquals(14, result)
    }

    @Test
    fun `part 1`() {
        val result = solvePartOne(File("./inputs/day24.txt").readText())

        assertEquals(502, result)
    }

    @Test
    fun `part 2 test data`() {
        val result = solvePartTwo(TEST_DATA)

        assertEquals(20, result)
    }

    @Test
    fun `part 2`() {
        val result = solvePartTwo(File("./inputs/day24.txt").readText())

        assertEquals(724, result)
    }
}