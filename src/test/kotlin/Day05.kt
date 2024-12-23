package org.ricall.day05

import org.junit.jupiter.api.Test
import java.io.File
import java.security.MessageDigest
import kotlin.test.assertEquals

private val messageDigest = MessageDigest.getInstance("MD5")

@OptIn(ExperimentalStdlibApi::class)
private fun passwordGenerator(seed: String) = sequence<String> {
    for (value in (0..Int.MAX_VALUE)) {
        val digest = messageDigest.digest("$seed$value".toByteArray())
        val hex = digest.toHexString()
        if (hex.startsWith("00000")) {
            yield(hex)
        }
    }
}

private fun solvePartOne(seed: String, length: Int = 8) = passwordGenerator(seed)
    .take(length)
    .map { it.substring(5, 6) }
    .joinToString("")

private fun solvePartTwo(seed: String, length: Int = 8): String {
    val digits = mutableMapOf<Int, String>()
    for (value in (passwordGenerator(seed))) {
        val index = value.substring(5, 6).toInt(16)
        val digit = value.substring(6, 7)
        if (index < length && !digits.containsKey(index)) {
            digits[index] = digit
            if (digits.keys.size == length) {
                return digits.entries.sortedBy { it.key }.map {it.value}.joinToString("")
            }
        }
    }
    error("Failed to solve")
}

class Day05 {
    @Test
    fun `part 1 test data`() {
        val password = solvePartOne("abc", 3)
        assertEquals("18f", password)
    }

    @Test
    fun `part 1`() {
        val password = solvePartOne(File("./inputs/day05.txt").readText())
        assertEquals("1a3099aa", password)
    }

    @Test
    fun `part 2 test data`() {
        val password = solvePartTwo("abc", 8)
        assertEquals("05ace8e3", password)
    }

    @Test
    fun `part 2`() {
        val password = solvePartTwo(File("./inputs/day05.txt").readText())
        assertEquals("694190cd", password)
    }
}