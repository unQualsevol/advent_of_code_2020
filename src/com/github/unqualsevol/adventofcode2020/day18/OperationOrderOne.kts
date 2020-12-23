import java.io.File

val addition = '+'
val product = '*'
val openParentheses = '('
val closeParentheses = ')'

var result = File("input").readLines().map { resolve(it) }.sum()
println("the total sum is: $result")

fun resolve(line: String): Long {

    val firstOperand = readOperand(line, 0)
    var index = firstOperand.second
    var result = firstOperand.first
    while (index < line.length) {
        val operatorResult = readOperator(line, index)
        val operator = operatorResult.first
        index = operatorResult.second

        val secondOperand = readOperand(line, index)
        index = secondOperand.second
        val second = secondOperand.first
        result = when (operator) {
            addition -> result + second
            product -> result * second
            else -> throw IllegalArgumentException("Invalid operator $operator")
        }
    }
    return result
}

fun findIndexOfMatchingClosingParentheses(line: String, openParenthesesIndex: Int): Int {
    var parenthesesCount = 1
    var index = openParenthesesIndex + 1
    while (parenthesesCount > 0) {
        when (line[index]) {
            openParentheses -> parenthesesCount++
            closeParentheses -> parenthesesCount--
            else -> {
            }
        }
        index++
    }
    return index
}

fun readOperand(line: String, start: Int): Pair<Long, Int> {
    var index = start
    val result = when (val operandChar = line[index]) {
        openParentheses -> {
            val indexOfClosingParentheses = findIndexOfMatchingClosingParentheses(line, index)
            val innerResult = resolve(line.substring(index + 1, indexOfClosingParentheses))
            index = indexOfClosingParentheses + 1
            innerResult
        }
        in '0'..'9' -> {
            index += 2
            operandChar.toString().toLong()
        }
        else -> throw IllegalArgumentException("Invalid operand $operandChar")
    }
    return Pair(result, index)
}

fun readOperator(line: String, start: Int): Pair<Char, Int> {
    var index = start
    var operator = line[start]
    while (operator != addition && operator != product) {
        operator = line[++index]
    }
    index += 2
    return Pair(operator, index)
}