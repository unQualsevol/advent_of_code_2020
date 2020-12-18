import java.io.File

val numbers = File("input").readText().split(",").map { it.toLong() }.toMutableList()

val numbersMap = numbers.mapIndexed { index, l -> l to Pair<Int?, Int>(null, index) }.toMap().toMutableMap()
val iterations = 30000000
for (index in numbers.size until iterations) {
    val previousNumber = numbers[index - 1]
    var nextNumber = 0L
    val indexesOfPreviousNumber = numbersMap.get(previousNumber)!!
    val previousIndex = indexesOfPreviousNumber.first
    if (previousIndex != null) {
        nextNumber = (index - previousIndex - 1).toLong()
    }
    numbers.add(nextNumber)
    if (numbersMap.containsKey(nextNumber)) {
        val indexes = numbersMap.get(nextNumber)!!
        numbersMap.put(nextNumber, Pair<Int?, Int>(indexes.second, index))
    } else {
        numbersMap.put(nextNumber, Pair<Int?, Int>(null, index))
    }
}
println(numbers.last())
