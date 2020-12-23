import java.io.File
import java.lang.Exception

var currentPos = 0
var accumulator = 0
var readInstructions = mutableSetOf(0)
val program = File("input").readLines().toMutableList()

//find the loop
do {
    val (posOffset, accumulateOffset) = readInstruction(currentPos)
    currentPos += posOffset
    accumulator += accumulateOffset
} while (readInstructions.add(currentPos))
val loop = readInstructions.drop(readInstructions.indexOf(currentPos))
val nopOrJmpInstructions = loop.filter { program[it].matches("""^(nop|jmp).*$""".toRegex()) }

//run the program swapping one instruction at a time
for(swipedInstruction in nopOrJmpInstructions) {
    val oldInstruction = program[swipedInstruction]
    program[swipedInstruction] = swapInstruction(oldInstruction)
    currentPos = 0
    accumulator = 0
    readInstructions = mutableSetOf(0)
    do {

        val (posOffset, accumulateOffset) = readInstruction(currentPos)
        currentPos += posOffset
        accumulator += accumulateOffset
    } while (currentPos < program.size && readInstructions.add(currentPos))
    if(currentPos == program.size)
    {
        println("swapped instruction: $swipedInstruction $oldInstruction")
        break
    }
    program[swipedInstruction] = oldInstruction
}
println("the accumulator is: $accumulator")

fun swapInstruction(instruction: String): String {
    val (instructionCode, value) = instruction.split(" ")
    return if (instructionCode == "nop") "jmp $value" else "nop $value"
}

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
