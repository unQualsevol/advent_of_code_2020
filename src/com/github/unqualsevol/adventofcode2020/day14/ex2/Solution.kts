import java.io.File

val andBaseMask = 68719476735L
val orBaseMask = 0L

val maskRegex = """([X,0,1]+)""".toRegex()
val memoryRegex = """mem\[(\d+)\] = (\d+)""".toRegex()

val memory = mutableMapOf<Long, Long>()
var orMask = 0L;
var currentMask = ""
val input = File("../input").forEachLine {
    when {
        it.startsWith("mask") -> updateMask(it)
        it.startsWith("mem") -> updateMemory(it)
    }
}

println("the sum of all values is: ${memory.values.sum()}")

fun updateMask(line: String): Unit {
    val matchResult = maskRegex.find(line)!!
    currentMask = matchResult.value
    orMask = currentMask.replace('X', '0').toLong(2) or orBaseMask
}

fun updateMemory(line: String): Unit {
    val matchResult = memoryRegex.find(line)!!
    val (adressString, valueString) = matchResult.destructured
    val address = adressString.toLong()
    val value = valueString.toLong()
    val maskedAddress = (address or orMask).toString(2).padStart(36, '0')
    writeOnMemory(value, maskedAddress, "")
}

fun writeOnMemory(value: Long, maskedAddress: String, address: String) {
    when {
        (address.length == currentMask.length) -> memory[address.toLong(2)] = value
        (currentMask[address.length] == '1') -> writeOnMemory(value, maskedAddress, address + "1")
        (currentMask[address.length] == '0') -> writeOnMemory(value, maskedAddress, address + maskedAddress[address.length])
        (currentMask[address.length] == 'X') -> {
            writeOnMemory(value, maskedAddress, address + "0")
            writeOnMemory(value, maskedAddress, address + "1")
        }
    }
}