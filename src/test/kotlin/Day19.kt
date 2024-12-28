package org.ricall.day19

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private fun solvePartOne(size: Int): Int {
    val queue = (1..size).toCollection(ArrayDeque(size))
    while (queue.size > 1) {
        queue.add(queue.removeFirst())
        queue.removeFirst()
    }
    return queue.removeFirst()
}

private fun solvePartTwo(size: Int): Int {
    val left = (1..size / 2).toCollection(ArrayDeque(size / 2))
    val right = (size / 2 + 1..size).toCollection(ArrayDeque(size / 2))

    while (left.size + right.size > 1) {
        right.removeFirst()
        right.add(left.removeFirst())
        if ((left.size + right.size) % 2 == 0) {
            left.add(right.removeFirst())
        }
    }
    return right.removeFirst()
}

class Day19 {
    @Test
    fun `part 1 test data`() {
        val result = solvePartOne(5)

        assertEquals(3, result)
    }

    @Test
    fun `part 1`() {
        val result = solvePartOne(File("./inputs/day19.txt").readText().toInt())

        assertEquals(1815603, result)
    }

    @Test
    fun `part 2 test data`() {
        val result = solvePartTwo(5)

        assertEquals(2, result)
    }

    @Test
    fun `part 2`() {
        val result = solvePartTwo(File("./inputs/day19.txt").readText().toInt())

        assertEquals(1410630, result)
    }
}