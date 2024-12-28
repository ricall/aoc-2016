package org.ricall.day20

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.math.max
import kotlin.test.assertEquals

private data class Range(val start: Long, val end: Long) {
    fun overlaps(other: Range) = end >= other.start
    fun length() = end - start + 1
}

private fun getRanges(input: String) = input.lines()
    .map { line ->
        val (a, b) = line.split("-")
        Range(a.toLong(), b.toLong())
    }
    .sortedWith(compareBy(Range::start, Range::end))
    .fold(emptyList<Range>()) { ranges, range ->
        if (ranges.isEmpty() || !ranges.last().overlaps(range)) {
            ranges + range
        } else {
            val lastRange = ranges.last()
            ranges.dropLast(1) + Range(lastRange.start, max(lastRange.end, range.end))
        }
    }

private fun solvePartOne(input: String) = getRanges(input).first().end + 1
private fun solvePartTwo(input: String) = (1L shl 32) - getRanges(input).map(Range::length).sum()

class Day20 {
    private val TEST_DATA = """
        |5-8
        |0-2
        |4-7""".trimMargin()

    @Test
    fun `part 1 test data`() {
        val lowest = solvePartOne(TEST_DATA)

        assertEquals(3, lowest)
    }

    @Test
    fun `part 1`() {
        val lowest = solvePartOne(File("./inputs/day20.txt").readText())

        assertEquals(17348574, lowest)
    }

    @Test
    fun `part 2`() {
        val lowest = solvePartTwo(File("./inputs/day20.txt").readText())

        assertEquals(104, lowest)
    }
}