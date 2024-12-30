package org.ricall.day23

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private class Computer(input: String) {
    private val instructions = input.lines().map { it.split(" ") }.toMutableList()
    private var registers = mutableMapOf<String, Int>()

    fun setRegister(name: String, value: Int) = when (name) {
        "a", "b", "c", "d" -> registers.put(name, value)
        else -> value
    }

    fun getRegister(name: String): Int = when(name) {
        "a", "b", "c", "d" -> registers.get(name) ?: 0
        else -> name.toInt()
    }

    private fun toggleRegister(index: Int) {
        if (index >= instructions.size) {
            return
        }
        val existing = instructions[index]
        instructions[index] = when (existing.size) {
            2 -> when (existing[0]) {
                "inc" -> listOf("dec", existing[1])
                else -> listOf("inc", existing[1])
            }
            3 -> when (existing[0]) {
                "jnz" -> listOf("cpy", existing[1], existing[2])
                else -> listOf("jnz", existing[1], existing[2])
            }
            else -> error("unsupported instruction")
        }
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
                "jnz" -> { if (getRegister(argument) != 0) { jump(getRegister(arguments.next())) } }
                "tgl" -> toggleRegister(programCounter + getRegister(argument))
                else -> error("Invalid instruction: $instruction")
            }
            programCounter++
        }
        return this
    }
}

class Day23 {
    private val TEST_DATA = """
        |cpy 2 a
        |tgl a
        |tgl a
        |tgl a
        |cpy 1 a
        |dec a
        |dec a""".trimMargin()

    @Test
    fun `part 1 test data`() {
        val result = Computer(TEST_DATA).execute().getRegister("a")

        assertEquals(3, result)
    }

    @Test
    fun `part 1`() {
        val computer = Computer(File("./inputs/day23.txt").readText())
        computer.setRegister("a", 7)
        computer.execute()

        assertEquals(10661, computer.getRegister("a"))
    }

    @Test
    fun `part 2`() {
        val computer = Computer(File("./inputs/day23.txt").readText())
        computer.setRegister("a", 12)
        computer.execute()

        assertEquals(479007221, computer.getRegister("a"))
    }
}