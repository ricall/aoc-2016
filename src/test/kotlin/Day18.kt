package org.ricall.day18

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private fun generateTraps(input: String) = sequence {
    var current = input
    while (true) {
        yield(current)
        val temp = "." + current + "."
        current = (0..current.length - 1).map { i ->
            when (temp.substring(i, i + 3)) {
                "^^." -> "^"
                ".^^" -> "^"
                "^.." -> "^"
                "..^" -> "^"
                else -> "."
            }
        }.joinToString("")
    }
}

private fun solve(input: String, length: Int) = generateTraps(input).take(length).map { it.count { it == '.' }}.sum()

private fun solvePartOne(input: String) = solve(input, 40)

private fun solvePartTwo(input: String) = solve(input, 400000)

class Day18 {

    @Test
    fun `part 1 test data`() {
        val result = solve(".^^.^.^^^^".trimMargin(), 10)

        assertEquals(38, result)
    }

    @Test
    fun `part 1`() {
        val result = solvePartOne(File("./inputs/day18.txt").readText())

        assertEquals(1987, result)
    }

    @Test
    fun `part 2`() {
        val result = solvePartTwo(File("./inputs/day18.txt").readText())

        assertEquals(19984714, result)
    }
}