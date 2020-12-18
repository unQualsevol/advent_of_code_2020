import java.io.File

var sum = 0;
var currentBlock = mutableSetOf<Char>();
var innerRow = 0
File("../input").forEachLine {
    val currentLine = it
    if (currentLine.isBlank()) {
        println("group sum: ${currentBlock.size}")
        sum += currentBlock.size
        currentBlock = mutableSetOf()
        innerRow = 0
    } else {
        if (innerRow == 0) {
            currentBlock.addAll(currentLine.asIterable())
            println("Empty: $currentBlock")
        } else {
            print("intersect $currentBlock with $currentLine ")
            currentBlock = currentBlock.intersect(currentLine.asIterable()).toMutableSet()
            println("result $currentBlock")
        }
        innerRow++
    }
}
println("total sum: $sum")
