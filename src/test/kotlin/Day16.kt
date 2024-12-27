package org.ricall.day16

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private fun String.invert() = toCharArray().map {
    when (it) {
        '0' -> '1'
        '1' -> '0'
        else -> it
    }
}.joinToString("")

private fun String.checksum() = toCharArray()
    .fold(mutableListOf<Char>() to null) { checksum: Pair<MutableList<Char>, Char?>, digit ->
        if (checksum.second == null) {
            Pair(checksum.first, digit)
        } else {
            Pair(checksum.first.apply { plusAssign(if (digit == checksum.second) '1' else '0') }, null)
        }
    }.first.joinToString("")

private fun checksumFor(input: String): String {
    var checksum = input.checksum()
    while (checksum.length % 2 == 0) {
        checksum = checksum.checksum()
    }
    return checksum
}

private fun solve(input: String, targetLength: Int): String {
    var current = input
    while (current.length < targetLength) {
        current += "0" + current.reversed().invert()
    }
    return checksumFor(current.substring(0, targetLength))
}

private fun solvePartOne(input: String) = solve(input, 272)

private fun solvePartTwo(input: String) = solve(input, 35651584)

class Day16 {
    @Test
    fun `part 1 checksum`() {
        val checksum = solve("10000", 20)

        assertEquals("01100", checksum)
    }

    @Test
    fun `part 1`() {
        val checksum = solvePartOne(File("./inputs/day16.txt").readText())

        assertEquals("01110011101111011", checksum)
    }

    @Test
    fun `part 2`() {
        val checksum = solvePartTwo(File("./inputs/day16.txt").readText())

        assertEquals("11001111011000111", checksum)
    }
}