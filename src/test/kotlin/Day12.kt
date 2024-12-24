package org.ricall.day12

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private class Computer(input: String) {
    private val instructions = input.lines().map { it.split(" ") }
    private var registers = mutableMapOf<String, Int>()

    fun setRegister(name: String, value: Int) = when (name) {
        "a", "b", "c", "d" -> registers.put(name, value)
        else -> error("unknown register $name")
    }

    fun getRegister(name: String): Int = when(name) {
        "a", "b", "c", "d" -> registers.get(name) ?: 0
        else -> name.toInt()
    }

    fun execute(): Computer {
        var programCounter = 0
        val jump = { offset: Int -> programCounter += offset - 1 }
        while (programCounter in instructions.indices) {
            val arguments = instructions[programCounter].iterator()
            val instruction = arguments.next()
            val argument = arguments.next()
            when (instruction) {
                "cpy" -> setRegister(arguments.next(), getRegister(argument))
                "inc" -> setRegister(argument, getRegister(argument) + 1)
                "dec" -> setRegister(argument, getRegister(argument) - 1)
                "jnz" -> { if (getRegister(argument) != 0) { jump(arguments.next().toInt()) } }
                else -> error("Invalid instruction: $instruction")
            }
            programCounter++
        }
        return this
    }
}

private fun solvePartOne(input: String): Int {
    val computer = Computer(input).execute()

    return computer.getRegister("a")
}

private fun solvePartTwo(input: String): Int {
    val computer = Computer(input)
    computer.setRegister("c", 1)
    computer.execute()

    return computer.getRegister("a")
}

class Day12 {
    private val TEST_DATA = """
        |cpy 41 a
        |inc a
        |inc a
        |dec a
        |jnz a 2
        |dec a""".trimMargin()

    @Test
    fun `part 1 test data`() {
        val result = solvePartOne(TEST_DATA)

        assertEquals(42, result)
    }

    @Test
    fun `part 1`() {
        val result = solvePartOne(File("./inputs/day12.txt").readText())

        assertEquals(318077, result)
    }

    @Test
    fun `part 2`() {
        val result = solvePartTwo(File("./inputs/day12.txt").readText())

        assertEquals(9227731, result)
    }
}