package org.ricall.day04

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private val ROOM_REGEX = """^(.*)-(\d+)\[(\w+)]$""".toRegex()

private data class Room(val name: String, val sector: Int, val checksum: String)

private fun parseRoom(input: String): Room {
    val (name, sector, checksum) = ROOM_REGEX.find(input)?.destructured!!
    return Room(name, sector.toInt(), checksum)
}

private fun isValidRoom(room: Room): Boolean {
    val (name, _, checksum) = room
    val frequency = buildMap<String, Int> {
        name.split("-")
            .flatMap { it.split("") }
            .filter { it.isNotBlank() }
            .forEach { ch -> put(ch, getOrDefault(ch, 0) + 1) }
    }
    val calculatedChecksum = frequency.entries.sortedWith(compareBy({ -it.value }, { it.key }))
        .take(5)
        .map { it.key[0] }
        .joinToString("")

    return checksum == calculatedChecksum
}

private fun solvePartOne(input: String) = input.lines()
    .map(::parseRoom)
    .filter(::isValidRoom)
    .map(Room::sector)
    .sum()

private fun decryptRoom(room: Room): Room {
    val (name, sector) = room
    val decrypted = name.split('-').map { text ->
        text.map { ch ->
            when (ch) {
                in ('a'..'z') -> ((ch - 'a' + sector) % 26 + 'a'.code).toChar()
                in ('A'..'Z') -> ((ch - 'A' + sector) % 26 + 'A'.code).toChar()
                else -> ch
            }
        }.joinToString("")
    }.joinToString(" ")

    return room.copy(name = decrypted)
}

private fun solvePartTwo(input: String) = input.lines()
    .map(::parseRoom)
    .filter(::isValidRoom)
    .map(::decryptRoom)
    .find { it.name == "northpole object storage" }
    ?.sector

class Day04 {
    @Test
    fun `part 1 test data`() {
        val result = solvePartOne(
            """
            |aaaaa-bbb-z-y-x-123[abxyz]
            |a-b-c-d-e-f-g-h-987[abcde]
            |not-a-real-room-404[oarel]
            |totally-real-room-200[decoy]""".trimMargin()
        )

        assertEquals(1514, result)
    }

    @Test
    fun `part 1`() {
        val result = solvePartOne(File("./inputs/day04.txt").readText())

        assertEquals(361724, result)
    }

    @Test
    fun `part 2 test data`() {
        val decrypted = decryptRoom(Room("qzmt-zixmtkozy-ivhz", 343, ""))

        assertEquals("very encrypted name", decrypted.name)
    }

    @Test
    fun `part 2`() {
        val result = solvePartTwo(File("./inputs/day04.txt").readText())

        assertEquals(482, result)
    }
}