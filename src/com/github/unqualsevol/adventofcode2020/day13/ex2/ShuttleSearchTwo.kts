import java.io.File
import java.lang.IllegalArgumentException
import kotlin.math.absoluteValue

val input = File("../input").readLines()
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

/*
val maxBusId = busIds.maxBy { it.second }!!
val startTimestamp = 1000L
//val startTimestamp = 101697580200320L
var currentTimestamp = ((startTimestamp /maxBusId.second)+1)*maxBusId.second-maxBusId.first

var found = false
var count = 0
while (!found) {
    if(count++ % 10000000 == 0) {
        println("current timestamp $currentTimestamp")
    }
    var valid = true
//    println("current timestamp $currentTimestamp")
    for(bus in busIds.sortedBy { -it.second }) {
//        println("bus: ${bus.second} offset: ${bus.first} value: ${currentTimestamp+bus.first} mod: ${(currentTimestamp+bus.first)%bus.second}")
        if((currentTimestamp+bus.first)%bus.second != 0L) {
            valid = false;
            break;
        }
    }
//    println()
    found = valid
    if(found) {
        println("the result is $currentTimestamp")
    }
    currentTimestamp+= maxBusId.second;
}
*/