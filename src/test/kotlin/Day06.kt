package org.ricall.day06

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private fun letterFrequencyByIndex(input: String) = sequence {
    val frequencies = mutableMapOf<Int, MutableMap<Char, Int>>()
    input.lines().forEach { line ->
        line.forEachIndexed { index, c ->
            val charFrequencies = frequencies.getOrPut(index, { mutableMapOf() })
            charFrequencies.put(c, charFrequencies.getOrDefault(c, 0) + 1)
        }
    }
    frequencies.keys.sorted().map {
        yield(frequencies[it]!!)
    }
}

private fun solvePartOne(input: String) = letterFrequencyByIndex(input)
    .map { it.entries.maxBy { it.value }.key }
    .joinToString("")

private fun solvePartTwo(input: String) = letterFrequencyByIndex(input)
    .map { it.entries.minBy { it.value }.key }
    .joinToString("")

class Day06 {
    private val TEST_DATA = """
        |eedadn
        |drvtee
        |eandsr
        |raavrd
        |atevrs
        |tsrnev
        |sdttsa
        |rasrtv
        |nssdts
        |ntnada
        |svetve
        |tesnvt
        |vntsnd
        |vrdear
        |dvrsen
        |enarar
    """.trimMargin()

    @Test
    fun `part 1 test data`() {
        val message = solvePartOne(TEST_DATA)
        assertEquals("easter", message)
    }

    @Test
    fun `part 1`() {
        val message = solvePartOne(File("./inputs/day06.txt").readText())
        assertEquals("gebzfnbt", message)
    }

    @Test
    fun `part 2 test data`() {
        val message = solvePartTwo(TEST_DATA)
        assertEquals("advent", message)
    }

    @Test
    fun `part 2`() {
        val message = solvePartTwo(File("./inputs/day06.txt").readText())
        assertEquals("fykjtwyn", message)
    }
}