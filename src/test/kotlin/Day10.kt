package org.ricall.day10

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private data class Destination(val type: String, val id: String)
private data class Bot(
    val lowDestination: Destination,
    val highDestination: Destination,
    val send: (Destination, Int) -> Unit,
    val inputs: MutableList<Int> = mutableListOf()
) {
    fun add(value: Int) {
        inputs.add(value)
        if (inputs.size == 2) {
            inputs.sort()
            send(lowDestination, inputs[0])
            send(highDestination, inputs[1])
        }
    }
}

typealias Output = Map<String, Int>
typealias BotResult = Map<String, List<Int>>

private val VALUE_REGEX = """value (\d+) goes to bot (\d+)""".toRegex()
private val BOT_REGEX = """bot (\d+) gives low to (\w+) (\d+) and high to (\w+) (\d+)""".toRegex()
private fun solve(input: String): Pair<Output, BotResult> {
    val lines = input.lines()
    val outputs = mutableMapOf<String, Int>()
    val bots = mutableMapOf<String, Bot>()
    fun send(destination: Destination, value: Int) {
        when (destination.type) {
            "output" -> outputs[destination.id] = value
            "bot" -> bots[destination.id]?.add(value)
        }
    }
    lines.forEach { line ->
        BOT_REGEX.find(line)?.let {
            val (botId, lowDestination, lowId, highDestination, highId) = it.destructured
            bots.put(botId, Bot(Destination(lowDestination, lowId), Destination(highDestination, highId), ::send))
        }
    }
    lines.forEach { line ->
        VALUE_REGEX.find(line)?.let {
            val (value, botId) = it.destructured
            bots[botId]?.add(value.toInt())
        }
    }
    return outputs to bots.mapValues { it.value.inputs }
}

private fun solvePartOne(input: String, botMatching: List<Int>): String {
    val (_, botResults) = solve(input)
    return botResults.entries.find { it.value == botMatching }!!.key
}

private fun solvePartTwo(input: String): Int {
    val (outputs) = solve(input)
    return listOf("0", "1", "2")
        .mapNotNull { outputs[it] }
        .fold(1) { result, value -> result * value }
}

class Day10 {
    private val TEST_DATA = """
        |value 5 goes to bot 2
        |bot 2 gives low to bot 1 and high to bot 0
        |value 3 goes to bot 1
        |bot 1 gives low to output 1 and high to bot 0
        |bot 0 gives low to output 2 and high to output 0
        |value 2 goes to bot 2
    """.trimMargin()

    @Test
    fun `part 1 test data`() {
        val result = solvePartOne(TEST_DATA, listOf(2, 5))

        assertEquals("2", result)
    }

    @Test
    fun `part 1`() {
        val result = solvePartOne(File("./inputs/day10.txt").readText(), listOf(17, 61))

        assertEquals("27", result)
    }

    @Test
    fun `part 2`() {
        val result = solvePartTwo(File("./inputs/day10.txt").readText())

        assertEquals(13727, result)
    }
}