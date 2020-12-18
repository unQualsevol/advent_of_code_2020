import java.io.File
import java.lang.Exception

var currentPos = 0
var accumulator = 0
var readedInstructions = mutableSetOf(0)
val program = File("input").readLines().toMutableList();

//find the loop
do {
    val (posOffset, acumulateOffset) = readInstruction(currentPos)
    currentPos += posOffset
    accumulator += acumulateOffset
} while (readedInstructions.add(currentPos))
val loop = readedInstructions.drop(readedInstructions.indexOf(currentPos));
val nopOrJmpInstructions = loop.filter { program[it].matches("""^(nop|jmp).*$""".toRegex()) }

//run the program swapping one instruction at a time
for(swippedInstruction in nopOrJmpInstructions) {
    val oldInstruction = program[swippedInstruction]
    program[swippedInstruction] = swapInstruction(oldInstruction)
    currentPos = 0
    accumulator = 0
    readedInstructions = mutableSetOf(0)
    do {

        val (posOffset, acumulateOffset) = readInstruction(currentPos)
        currentPos += posOffset
        accumulator += acumulateOffset
    } while (currentPos < program.size && readedInstructions.add(currentPos))
    if(currentPos == program.size)
    {
        println("swapped instruction: $swippedInstruction $oldInstruction")
        break;
    }
    program[swippedInstruction] = oldInstruction
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
