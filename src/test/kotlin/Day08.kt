package org.ricall.day08

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.text.MatchResult.Destructured

private typealias Screen = List<BooleanArray>
private val commands = mapOf(
    """^rect (\d+)x(\d+)$""".toRegex() to { screen: Screen, args: Destructured ->
        val (x, yText) = args
        (0..< yText.toInt()).forEach { y -> (0..< x.toInt()).forEach { x -> screen[y][x] = true } }
    },
    """^rotate row y=(\d+) by (\d+)$""".toRegex() to { screen: Screen, args: Destructured ->
        val (y, p) = args
        val line = screen[y.toInt()]
        val pixels = p.toInt()
        val newLine = line.indices.map { x ->
            val offset = when {
                x - pixels < 0 -> x - pixels + line.size
                else -> x - pixels
            }
            line[offset]
        }
        newLine.indices.forEach { x -> line[x] = newLine[x] }
    },
    """^rotate column x=(\d+) by (\d+)$""".toRegex() to { screen: Screen, args: Destructured ->
        val (xText, p) = args
        val x = xText.toInt()
        val pixels = p.toInt()
        val newLine = screen.indices.map { y ->
            val offset = when {
                y - pixels < 0 -> y - pixels + screen.size
                else -> y-pixels
            }
            screen[offset][x]
        }
        newLine.indices.forEach { y -> screen[y][x] = newLine[y] }
    },
)

private fun createScreen(input: String, width: Int, height: Int): Screen {
    val screen = (1..height).map { BooleanArray(width) }
    input.lines().forEach { line ->
        for ((regex, action) in commands) { regex.find(line)?.let { action(screen, it.destructured)} }
    }
    return screen
}

private fun display(screen: Screen) {
    println()
    screen.forEach { line -> println(line.map { if (it) "#" else " " }.joinToString("")) }
    println()
}

private fun countPixels(screen: Screen) = screen.map { line -> line.count { it }}.sum()

class Day08 {
    private val TEST_DATA = """
        |rect 3x2
        |rotate column x=1 by 1
        |rotate row y=0 by 4
        |rotate column x=1 by 1
    """.trimMargin()

    @Test
    fun `part 1 test data`() {
        val screen = createScreen(TEST_DATA, 7, 3)

        assertEquals(6, countPixels(screen))
    }

    @Test
    fun `part 1 and part 2`() {
        val screen = createScreen(File("./inputs/day08.txt").readText(), 50, 6)
        display(screen)

        assertEquals(128, countPixels(screen))
    }
}