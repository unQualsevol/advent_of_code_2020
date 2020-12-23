import java.io.File

val input = File("input").readLines()
val timestamp = input[0].toInt()
val busSchedule = input[1].split(",").mapIndexed { index, s -> Pair(index, s) }
val busIds = busSchedule.filter { it.second != "x" }.map { Pair(it.first, it.second.toInt()) }

val differences = busIds.map { Pair(it.first, it.second*((timestamp/it.second+1))-timestamp) }
val minDifference = differences.minBy { it.second }!!
println("the result is ${busIds.first { it.first == minDifference.first }.second*minDifference.second}")