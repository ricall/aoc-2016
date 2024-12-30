package org.ricall.day22

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.math.abs
import kotlin.test.assertEquals

private data class Node(val x: Int, val y: Int, val size: Int, val used: Int) {
    val available = size - used
}

private val NODE_REGEX = """^/dev/grid/node-x(\d+)-y(\d+)\s+(\d+)T\s+(\d+)T.+$""".toRegex()
private fun parseInput(input: String) = input.lines().mapNotNull { line ->
    NODE_REGEX.find(line)?.let {
        val (x, y, size, used) = it.destructured
        Node(x.toInt(), y.toInt(), size.toInt(), used.toInt())
    }
}

private fun solvePartOne(input: String) = parseInput(input)
    .filter { it.used > 0 }
    .sumOf { nodeA -> parseInput(input).count { nodeB -> nodeB != nodeA && nodeB.available >= nodeA.used } }

private fun solvePartTwo(input: String): Int {
    val nodes = parseInput(input)
    val width = nodes.maxOf { it.x }
    val empty = nodes.find { it.used == 0 }!!
    val wall = nodes.filter { it.used > empty.available }.minBy { it.x }

    var steps = abs(empty.x - (wall.x - 1)) + abs(empty.y - wall.y)
    steps += abs((wall.x - 1) - width) + wall.y
    return steps + 5 * (width - 1)
}

class Day22 {
    @Test
    fun `part 1`() {
        val result = solvePartOne(File("./inputs/Day22.txt").readText())
        assertEquals(952, result)
    }

    @Test
    fun `part 2`() {
        val result = solvePartTwo(File("./inputs/Day22.txt").readText())
        assertEquals(181, result)
    }
}