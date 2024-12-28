package org.ricall.day21

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private fun interface Instruction {
    fun transform(text: String, decode: Boolean): String
}

private val INSTRUCTIONS = mapOf<Regex, (MatchResult.Destructured) -> Instruction>(
    """^swap position (\d+) with position (\d+)$""".toRegex() to { (xIndex, yIndex) ->
        Instruction { text, _ ->
            val x = xIndex.toInt()
            val y = yIndex.toInt()
            val chars = text.toCharArray()
            val temp = chars[x]
            chars[x] = chars[y]
            chars[y] = temp
            String(chars)
        }
    },
    """^swap letter (\w+) with letter (\w+)$""".toRegex() to { (a, b) ->
        Instruction { text, _ -> text.replace(a, "~").replace(b, a).replace("~", b) }
    },
    """^rotate (left|right) (\d+) step(s){0,1}$""".toRegex() to { (direction, stepIndex) ->
        Instruction { text: String, decode ->
            val multiplier = when (decode) { true -> -1 else -> 1 }
            val steps = when (direction) {
                "left" -> stepIndex.toInt() * multiplier
                else -> -stepIndex.toInt() * multiplier
            }
            (0..<text.length).map { text.get((it + steps).mod(text.length))}.joinToString("")
        }
    },
    """^rotate based on position of letter (\w+)$""".toRegex() to { (a) ->
        Instruction { text, decode ->
            val index = text.indexOf(a)
            val rot = when (decode) {
                true -> index / 2 + (if (index % 2 == 1 || index == 0) 1 else 5) // Hack - only works for 8 characters
                else -> -(index + 1 + (if (index >= 4) 1 else 0))
            }
            (0..<text.length).map { text.get((it + rot).mod(text.length))}.joinToString("")
        }
    },
    """^reverse positions (\d+) through (\d+)$""".toRegex() to { (xIndex, yIndex) ->
        Instruction { text, _ ->
            val x = xIndex.toInt()
            val y = yIndex.toInt()
            text.substring(0, x) + text.substring(x, y + 1).reversed() + text.substring(y + 1)
        }
    },
    """^move position (\d+) to position (\d+)$""".toRegex() to { (xIndex, yIndex) ->
        Instruction { text, decode ->
            val x = when(decode) { true -> yIndex else -> xIndex}.toInt()
            val y = when(decode) { true -> xIndex else -> yIndex}.toInt()
            val ch = text[x]
            val temp = text.filterIndexed { i, _ -> i != x }
            temp.substring(0, y) + ch + temp.substring(y)
        }
    },
)

private fun parseInstructions(input: String) = input.lines().map { line ->
    INSTRUCTIONS.entries.firstNotNullOf { (key, value) ->
        val match = key.find(line)
        if (match != null) {
            value(match.destructured)
        } else {
            null
        }
    }
}

private fun String.solvePartOne(text: String) = parseInstructions(this)
    .fold(text) { password, instruction -> instruction.transform(password, false) }

private fun String.solvePartTwo(text: String) = parseInstructions(this)
    .reversed()
    .fold(text) { password, instruction -> instruction.transform(password, true) }

private val TEST_DATA = """
        |swap position 4 with position 0
        |swap letter d with letter b
        |reverse positions 0 through 4
        |rotate left 1 step
        |move position 1 to position 4
        |move position 3 to position 0
        |rotate based on position of letter b
        |rotate based on position of letter d
    """.trimMargin()

class Day21 {
    @Test
    fun `part 1 test data`() {
        val result = TEST_DATA.solvePartOne("abcde")

        assertEquals("decab", result)
    }

    @Test
    fun `part 1`() {
        val result = File("./inputs/day21.txt").readText().solvePartOne("abcdefgh")

        assertEquals("agcebfdh", result)
    }

    @Test
    fun `part 2 test data`() {
        val result = File("./inputs/day21.txt").readText().solvePartTwo("agcebfdh")
        assertEquals("abcdefgh", result)
    }

    @Test
    fun `part 2`() {
        val result = File("./inputs/day21.txt").readText().solvePartTwo("fbgdceah")
        assertEquals("afhdbegc", result)
    }
}