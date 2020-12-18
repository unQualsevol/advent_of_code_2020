import java.io.File

fun countQuestions(data: String): Int {
    val setOfQuestions = mutableSetOf<Char>()
    data.forEach { setOfQuestions.add(it) }

    return setOfQuestions.size;
}

var sum = 0;
var currentBlock = "";
File("input").forEachLine {
    val currentLine = it
    if (currentLine.isBlank()) {
        sum += countQuestions(currentBlock)
        currentBlock = ""
    } else {
        currentBlock += currentLine
    }
}
println("total sum: $sum")
