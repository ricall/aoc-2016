package org.ricall.day17

import org.junit.jupiter.api.Test
import java.io.File
import java.security.MessageDigest
import kotlin.test.assertEquals

private typealias HashFunction = (String) -> String
@OptIn(ExperimentalStdlibApi::class)
private fun hashFunctionFor(seed: String): HashFunction {
    val seedArray = seed.toByteArray()
    val md5 = MessageDigest.getInstance("MD5")
    return { path: String -> md5.update(seedArray); md5.digest(path.toByteArray()).toHexString() }
}

private enum class Directions(val dx: Int, val dy: Int) {
    UP(0,-1), DOWN(0,1), LEFT(-1,0), RIGHT(1, 0)
}

private data class State(val path: String, val position: Pair<Int, Int>, private val hashFunction: HashFunction) {
    private val hash = hashFunction(path)
    private val doors = BooleanArray(4) { hash[it] in 'b'..'f' }

    fun getNextStates(): List<State> {
        return Directions.entries.toTypedArray()
            .filterIndexed { i, _ -> doors[i] }
            .map { State(path+it.name[0], Pair(position.first+it.dx, position.second+it.dy), hashFunction) }
            .filter { (_, it) -> it.first in 0..3 && it.second in 0..3 }
    }
}

private fun pathSequenceFor(passcode: String) = sequence {
    val target = Pair(3, 3)
    val start = State("", Pair(0, 0), hashFunctionFor(passcode))
    val queue = ArrayDeque(listOf(start))
    while (queue.isNotEmpty()) {
        val currentState = queue.removeFirst()
        if (currentState.position == target) {
            yield(currentState.path)
        } else {
            queue.addAll(currentState.getNextStates())
        }
    }
}

private fun solvePartOne(passcode: String) = pathSequenceFor(passcode).first()

private fun solvePartTwo(passcode: String) = pathSequenceFor(passcode).last().length

class Day17 {
    @Test
    fun `part 1 test data`() {
        assertEquals("DDRRRD", solvePartOne("ihgpwlah"))
        assertEquals("DDUDRLRRUDRD", solvePartOne("kglvqrro"))
        assertEquals("DRURDRUDDLLDLUURRDULRLDUUDDDRR", solvePartOne("ulqzkmiv"))
    }

    @Test
    fun `part 1`() {
        val path = solvePartOne(File("./inputs/day17.txt").readText())

        assertEquals("DDRUDLRRRD", path)
    }

    @Test
    fun `part 2 test data`() {
        assertEquals(370, solvePartTwo("ihgpwlah"))
        assertEquals(492, solvePartTwo("kglvqrro"))
        assertEquals(830, solvePartTwo("ulqzkmiv"))
    }

    @Test
    fun `part 2`() {
        val length = solvePartTwo(File("./inputs/day17.txt").readText())

        assertEquals(398, length)
    }
}