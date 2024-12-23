package org.ricall.day09

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private val MARKER_REGEX = """[(](\d+)x(\d+)[)]""".toRegex()
private fun solve(text: String, totalLengthFor: (String) -> Long): Long {
    var length = text.length.toLong()
    var marker = MARKER_REGEX.find(text)
    while (marker != null) {
        val (charactersText, repeatsText) = marker.destructured
        val repeats = repeatsText.toInt()
        val repeatTextLength = charactersText.toInt()
        val start = marker.range.last + 1
        val repeatText = text.substring(start, start + repeatTextLength)

        val totalCharacters = totalLengthFor(repeatText)

        length += (totalCharacters * repeats) - (start - marker.range.first) - repeatTextLength
        marker = MARKER_REGEX.find(text, marker.range.last + repeatTextLength + 1)
    }
    return length
}

private fun solvePartOne(text: String) = solve(text, { it.length.toLong() })

private fun solvePartTwo(text: String): Long = solve(text, ::solvePartTwo)

class Day09 {
    @Test
    fun `part 1 test data`() {
        assertEquals(6, solvePartOne("ADVENT"))
        assertEquals(7, solvePartOne("A(1x5)BC"))
        assertEquals(9, solvePartOne("(3x3)XYZ"))
        assertEquals(11, solvePartOne("A(2x2)BCD(2x2)EFG"))
        assertEquals(18, solvePartOne("X(8x2)(3x3)ABCY"))
    }

    @Test
    fun `part 1`() {
        val result = solvePartOne(File("./inputs/day09.txt").readText())

        assertEquals(107035, result)
    }

    @Test
    fun `part 2 test data`() {
        assertEquals(9, solvePartTwo("(3x3)XYZ"))
        assertEquals(20, solvePartTwo("X(8x2)(3x3)ABCY"))
        assertEquals(241920, solvePartTwo("(27x12)(20x12)(13x14)(7x10)(1x12)A"))
        assertEquals(445, solvePartTwo("(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN"))
    }

    @Test
    fun `part 2`() {
        val result = solvePartTwo(File("./inputs/day09.txt").readText())

        assertEquals(11451628995, result)
    }
}