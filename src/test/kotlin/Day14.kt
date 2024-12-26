package org.ricall.day14

import org.junit.jupiter.api.Test
import java.io.File
import java.security.MessageDigest
import kotlin.test.assertEquals

private val md = MessageDigest.getInstance("MD5")

@OptIn(ExperimentalStdlibApi::class)
private fun md5(text: String) = md.digest(text.toByteArray()).toHexString()
private fun keyStretch(text: String) = (0..2016).fold(text) { current, _ -> md5(current) }

private typealias HashFunction = (String) -> String

private fun generateRandomData(seed: String, hash: HashFunction = ::md5) = sequence {
    (0..Int.MAX_VALUE).forEach { yield(hash("$seed${it}")) }
}

private fun firstTriple(key: String) = key.split("")
    .windowed(3)
    .find { (a, b, c) -> a == b && a == c }?.get(0)

private fun keySequence(sequence: Sequence<String>) = sequence
    .windowed(1001)
    .mapIndexedNotNull { index, values ->
        val ch = firstTriple(values[0])
        if (ch == null) {
            null
        } else {
            val match = "$ch$ch$ch$ch$ch"
            if ((1..1000).any { i -> values[i].contains(match) }) {
                index to values[0]
            } else {
                null
            }
        }
    }

class Day14 {
    @Test
    fun `part 1 test data`() {
        val keys = keySequence(generateRandomData("abc")).take(64).toList()

        assertEquals(39, keys[0].first)
        assertEquals(92, keys[1].first)
        assertEquals(22728, keys[63].first)
    }

    @Test
    fun `part 1`() {
        val randomData = generateRandomData(File("./inputs/day14.txt").readText())
        val keys = keySequence(randomData).take(64).toList()

        assertEquals(23890, keys[63].first)
    }

    @Test
    fun `part 2 keyStretch`() {
        assertEquals("a107ff634856bb300138cac6568c0f24", keyStretch("abc0"))
    }

    @Test
    fun `part 2 test data`() {
        val keys = keySequence(generateRandomData("abc", ::keyStretch)).take(64).toList()

        assertEquals(10, keys[0].first)
        assertEquals(22551, keys[63].first)
    }

    @Test
    fun `part 2`() {
        val randomData = generateRandomData(File("./inputs/day14.txt").readText(), ::keyStretch)
        val keys = keySequence(randomData).take(64).toList()

        assertEquals(22696, keys[63].first)
    }
}