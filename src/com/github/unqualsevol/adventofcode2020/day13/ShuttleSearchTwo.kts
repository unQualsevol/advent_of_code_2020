import java.io.File

val input = File("input").readLines()
val busSchedule = input[1].split(",").mapIndexed { index, s -> Pair(index, s) }
val busIds = busSchedule.filter { it.second != "x" }.map { Pair(it.first, it.second.toLong()) }

var time = busIds.first().second
var increment = busIds.first().second
for(current in busIds.subList(1, busIds.size)) {
    while ((time + current.first) % current.second != 0L) {
        time += increment
    }
    increment *= current.second
}
println("the result is $time")
