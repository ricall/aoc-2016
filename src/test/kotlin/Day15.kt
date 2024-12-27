package org.ricall.day15

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private data class Disk(val id: Int, val positions: Int, val position: Int) {
    fun passesThrough(index: Int) = (id + position + index) % positions == 0
}

private val PARSE_REGEX = """Disc #(\d+) has (\d+) positions; at time=0, it is at position (\d+).""".toRegex()
private fun parseDisk(line: String) = PARSE_REGEX.find(line)?.let {
    val (idText, positionsText, positionText) = it.destructured
    Disk(idText.toInt(), positionsText.toInt(), positionText.toInt())
}

private fun parseDisks(input: String) = input.lines().mapNotNull(::parseDisk)

private fun solve(disks: List<Disk>): Int {
    (0..Int.MAX_VALUE).forEach { index ->
        if (disks.all { it.passesThrough(index) }) {
            return index
        }
    }
    error("no solution")
}

private fun solvePartOne(input: String) = solve(parseDisks(input))

private fun solvePartTwo(input: String) = solve(parseDisks(input) + Disk(7, 11, 0))

class Day15 {
    private val TEST_DATA = """
        |Disc #1 has 5 positions; at time=0, it is at position 4.
        |Disc #2 has 2 positions; at time=0, it is at position 1.""".trimMargin()

    @Test
    fun `part 1 test data`() {
        val result = solvePartOne(TEST_DATA)

        assertEquals(5, result)
    }

    @Test
    fun `part 1`() {
        val result = solvePartOne(File("./inputs/day15.txt").readText())

        assertEquals(400589, result)
    }

    @Test
    fun `part 2`() {
        val result = solvePartTwo(File("./inputs/day15.txt").readText())

        assertEquals(3045959, result)
    }
}