import java.io.File

val numbers = File("input").readText().split(",").map { it.toLong() }.toMutableList()

val numbersMap = numbers.mapIndexed { index, l -> l to Pair<Int?, Int>(null, index) }.toMap().toMutableMap()
val iterations = 30000000
for (index in numbers.size until iterations) {
    val previousNumber = numbers[index - 1]
    var nextNumber = 0L
    val indexesOfPreviousNumber = numbersMap.getValue(previousNumber)
    val previousIndex = indexesOfPreviousNumber.first
    if (previousIndex != null) {
        nextNumber = (index - previousIndex - 1).toLong()
    }
    numbers.add(nextNumber)
    numbersMap[nextNumber] = if (numbersMap.containsKey(nextNumber)) {
        val indexes = numbersMap.getValue(nextNumber)
        Pair<Int?, Int>(indexes.second, index)
    } else {
        Pair<Int?, Int>(null, index)
    }
}
println(numbers.last())
