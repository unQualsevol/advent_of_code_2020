import java.io.File

val preambleSize = 25
val values = File("../input").readLines().map { it.toLong() }
val preamble = values.subList(0,preambleSize).toMutableList()

for(i in preambleSize..values.size-preambleSize-1)
{
    val currentValue = values[i]
    var found = false
    for(j in preamble) {
        if(preamble.contains(currentValue-j)){
            found = true
            break
        }
    }
    if(!found) {
        println("first failing number: $currentValue")
        break
    }
    preamble.removeAt(0)
    preamble.add(currentValue)
}