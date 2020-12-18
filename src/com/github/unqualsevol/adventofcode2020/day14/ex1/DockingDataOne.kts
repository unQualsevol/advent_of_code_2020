import java.io.File

val andBaseMask = 68719476735L
val orBaseMask = 0L

val maskRegex = """([X,0,1]+)""".toRegex()
val memoryRegex = """mem\[(\d+)\] = (\d+)""".toRegex()

val memory = mutableMapOf<Int, Long>()
var andMask = 0L;
var orMask = 0L;
val input = File("../input").forEachLine {
    when {
        it.startsWith("mask") -> updateMask(it)
        it.startsWith("mem") -> updateMemory(it)
    }
}

println("the sum of all values is: ${memory.values.sum()}")

fun updateMask(line: String): Unit {
    val matchResult = maskRegex.find(line)!!
    val mask = matchResult.value
    andMask = mask.replace('X', '1').toLong(2) and andBaseMask
    orMask = mask.replace('X', '0').toLong(2) or orBaseMask
}

fun updateMemory(line: String): Unit {
    val matchResult = memoryRegex.find(line)!!
    val (adressString, valueString) = matchResult.destructured
    val address = adressString.toInt()
    val value = valueString.toLong()
    val maskedValue = (value and andMask) or orMask
    memory[address] = maskedValue
}