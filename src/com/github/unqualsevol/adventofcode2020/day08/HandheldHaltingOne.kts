import java.io.File
import java.lang.Exception

var currentPos = 0
var accumulator = 0
val readInstructions = mutableSetOf(0)
val program = File("input").readLines()

do {
    val (posOffset, accumulateOffset) = readInstruction(currentPos)
    currentPos += posOffset
    accumulator += accumulateOffset
} while (readInstructions.add(currentPos))
println("the accumulator is: $accumulator")

fun readInstruction(position: Int): Pair<Int, Int> {
    val instruction = program[position]
    val (instructionCode, value) = instruction.split(" ")
    val offset = value.toInt()
    return when (instructionCode) {
        "nop" -> Pair(1, 0)
        "acc" -> Pair(1, offset)
        "jmp" -> Pair(offset, 0)
        else -> throw Exception("Invalid instruction: \"$instruction\"")
    }
}
