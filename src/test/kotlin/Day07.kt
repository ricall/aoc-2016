package org.ricall.day07

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private data class IPV7(val addresses: List<String>, val hypernetAddresses: List<String>) {
    fun supportsTLS() = addresses.any(::partSupportsTLS) && hypernetAddresses.none(::partSupportsTLS)
    fun supportsSSL() = findAllABA(addresses)
        .map { (a, b) -> "$b$a$b" }
        .any { bab -> hypernetAddresses.any { it.contains(bab) } }
}

private fun partSupportsTLS(part: String) = part.split("")
    .windowed(4)
    .any { (a, b, c, d) -> a != b && a == d && b == c }

private fun findAllABA(addresses: List<String>) = addresses.flatMap { address ->
    address.split("").windowed(3).filter { (a, b, c) -> a != b && a == c }
}

private fun parseIPV7(address: String): IPV7 {
    val addresses = mutableListOf<String>()
    val hypernetAddresses = mutableListOf<String>()

    address.split("""[\[\]]""".toRegex())
        .forEachIndexed { index, part ->
            when (index % 2) {
                0 -> addresses.add(part)
                else -> hypernetAddresses.add(part)
            }
        }
    return IPV7(addresses, hypernetAddresses)
}

private fun supportsTLS(address: String) = parseIPV7(address).supportsTLS()
private fun solvePartOne(input: String) = input.lines().count(::supportsTLS)

private fun supportsSSL(address: String) = parseIPV7(address).supportsSSL()
private fun solvePartTwo(input: String) = input.lines().count(::supportsSSL)

class Day07 {
    @Test
    fun `part 1 test data`() {
        assertEquals(true, supportsTLS("abba[mnop]qrst"))
        assertEquals(false, supportsTLS("abcd[bddb]xyyx"))
        assertEquals(false, supportsTLS("aaaa[qwer]tyui"))
        assertEquals(true, supportsTLS("abba[mnop]qrst"))
        assertEquals(true, supportsTLS("ioxxoj[asdfgh]zxcvbn"))
    }

    @Test
    fun `part 1`() {
        val result = solvePartOne(File("./inputs/day07.txt").readText())

        assertEquals(118, result)
    }

    @Test
    fun `part 2 test data`() {
        assertEquals(true, supportsSSL("aba[bab]xyz"))
        assertEquals(false, supportsSSL("xyx[xyx]xyx"))
        assertEquals(true, supportsSSL("aaa[kek]eke"))
        assertEquals(true, supportsSSL("zazbz[bzb]cdb"))
    }

    @Test
    fun `part 2`() {
        val result = solvePartTwo(File("./inputs/day07.txt").readText())

        assertEquals(260, result)
    }
}