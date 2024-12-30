package org.ricall.day25

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

    fun execute(): Boolean {
        var programCounter = 0
        val jump = { offset: Int -> programCounter += offset - 1 }
        var outCount = 0
        var expected = 0
        while (programCounter in instructions.indices && outCount < 4_000) {
            val arguments = instructions[programCounter].iterator()
            val instruction = arguments.next()
            val argument = arguments.next()
            when (instruction) {
                "cpy" -> setRegister(arguments.next(), getRegister(argument))
                "inc" -> setRegister(argument, getRegister(argument) + 1)
                "dec" -> setRegister(argument, getRegister(argument) - 1)
                "jnz" -> { if (getRegister(argument) != 0) { jump(getRegister(arguments.next())) } }
                "out" -> {
                    val out = getRegister(argument)
                    if (out != expected) {
                        return false
                    }
                    expected = 1 - expected
                    outCount++
                }
                else -> error("Invalid instruction: $instruction")
            }
            programCounter++
        }
        return outCount >= 40
    }
}


class Day25 {
    @Test
    fun `part 1`() {
        val computer = Computer(File("./inputs/day25.txt").readText())
        computer.setRegister("a", 198)
        val result = computer.execute()

        assertEquals(true, result)
    }
}