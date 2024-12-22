package org.ricall.dayxx

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
}

class Keypad(buttonMatrix: String) {
    private val buttons = buttonMatrix.lines()
        .flatMapIndexed { y, line -> line.mapIndexed { x, ch -> Point(x, y) to ch } }
        .filter { (_, ch) -> ch != ' ' }
        .toMap()
    private var current = buttons.entries.find { (_, button) -> button == '5' }?.key!!

    fun followInstructions(instructions: String): Char {
        for (ch in instructions) {
            val candidate = when (ch) {
                'U' -> current + Point(0, -1)
                'D' -> current + Point(0, 1)
                'L' -> current + Point(-1, 0)
                'R' -> current + Point(1, 0)
                else -> error("Unknown instruction $ch")
            }
            if (buttons.contains(candidate)) {
                current = candidate
            }
        }
        return buttons[current]!!
    }
}

fun solvePartOne(input: String): String {
    val keypad = Keypad(
        """
        |123
        |456
        |789""".trimMargin()
    )
    return input.lines().map(keypad::followInstructions).joinToString("")
}

fun solvePartTwo(input: String): String {
    val keypad = Keypad(
        """
        |  1  
        | 234 
        |56789
        | ABC 
        |  D  
    """.trimMargin()
    )
    return input.lines().map(keypad::followInstructions).joinToString("")
}

class Day02 {
    private val TEST_DATA = """
        |ULL
        |RRDDD
        |LURDL
        |UUUUD""".trimMargin()

    @Test
    fun `part 1 test data`() {
        assertEquals("1985", solvePartOne(TEST_DATA))
    }

    @Test
    fun `part 1`() {
        assertEquals("48584", solvePartOne(File("./inputs/day02.txt").readText()))
    }

    @Test
    fun `part 2 test data`() {
        assertEquals("5DB3", solvePartTwo(TEST_DATA))
    }

    @Test
    fun `part 2`() {
        assertEquals("563B6", solvePartTwo(File("./inputs/day02.txt").readText()))
    }
}