package org.ricall.dayxx

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private fun parseInput(input: String) = input.lines().map { it.split(" a ") }.map { it.count() - 1 }

private fun solve(counts: List<Int>) = (0..counts.size - 2)
    .map { counts[it] }
    .fold(0 to 0) { (moves, items), count -> moves + 2 * (items + count - 1) - 1 to items + count }
    .first

private fun solvePartOne(input: String) = solve(parseInput(input))

private fun solvePartTwo(input: String) = solve(parseInput(input).let { it.toMutableList().also { it[0] += 4 } })

class Day11 {
    private val TEST_DATA = """
        |The first floor contains a hydrogen-compatible microchip and a lithium-compatible microchip.
        |The second floor contains a hydrogen generator.
        |The third floor contains a lithium generator.
        |The fourth floor contains nothing relevant.
    """.trimMargin()

    @Test
    fun `part 1 test data`() {
        val result = solvePartOne(TEST_DATA)

        assertEquals(9, result)
    }

    @Test
    fun `part 1`() {
        val result = solvePartOne(File("./inputs/day11.txt").readText())

        assertEquals(31, result)
    }

    @Test
    fun `part 2 test data`() {
        val result = solvePartTwo(TEST_DATA)

        assertEquals(33, result)
    }

    @Test
    fun `part 2`() {
        val result = solvePartTwo(File("./inputs/day11.txt").readText())

        assertEquals(55, result)
    }
}