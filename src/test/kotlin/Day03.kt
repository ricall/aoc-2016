package org.ricall.day03

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private fun isValidTriangle(parts: List<Int>): Boolean {
    val max = parts.max()
    val maxIndex = parts.indexOf(max)
    return parts.filterIndexed { i, _ -> i != maxIndex }.sum() > max
}

private fun lineToInts(line: String) = line.trim().split("\\s+".toRegex()).map(String::trim).map(String::toInt)

private fun solvePartOne(input: String) = input.lines()
    .map(::lineToInts)
    .count(::isValidTriangle)

private fun transposeRowCol(input: List<List<Int>>): List<List<Int>> = (0..2).map { i ->
    listOf(input[0][i], input[1][i], input[2][i])
}

private fun solvePartTwo(input: String) = input.lines()
    .map(::lineToInts)
    .chunked(3)
    .flatMap(::transposeRowCol)
    .count(::isValidTriangle)

class Day03 {
    @Test
    fun `part 1 test data`() {
        val result = solvePartOne(
            """
            |5 10 25
            |3 4 5""".trimMargin()
        )

        assertEquals(1, result)
    }

    @Test
    fun `part 1`() {
        val result = solvePartOne(File("./inputs/day03.txt").readText())

        assertEquals(982, result)
    }

    @Test
    fun `part 2 test data`() {
        val result = solvePartTwo(
            """
            |101 301 501
            |102 302 502
            |103 303 503
            |201 401 601
            |202 402 602
            |203 403 603""".trimMargin()
        )

        assertEquals(6, result)
    }

    @Test
    fun `part 2`() {
        val result = solvePartTwo(File("./inputs/day03.txt").readText())

        assertEquals(1826, result)
    }
}