import java.io.File

val numbers = File("../input").readText().split(",").map { it.toInt() }.toMutableList()

val iterations = 2020
for (index in numbers.size until iterations) {
    val previousNumber = numbers[index - 1]
    val previousNumbers = numbers.subList(0, index - 1)
    var nextNumber = 0
    val indexOfPreviousNumber = previousNumbers.lastIndexOf(previousNumber)
    if (indexOfPreviousNumber > -1) {
        nextNumber = index - indexOfPreviousNumber - 1
    }
    numbers.add(nextNumber)
}
println(numbers.last())
