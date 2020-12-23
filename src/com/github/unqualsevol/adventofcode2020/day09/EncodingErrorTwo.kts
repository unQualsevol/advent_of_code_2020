import java.io.File

val preambleSize = 25
val values = File("input").readLines().map { it.toLong() }
val preamble = values.subList(0,preambleSize).toMutableList()

var breakingValue = 0L
for(i in preambleSize until values.size-preambleSize)
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
        breakingValue = currentValue
        break
    }
    preamble.removeAt(0)
    preamble.add(currentValue)
}
println("first failing number: $breakingValue")

var currentPreambleSize = 2
var found = false
while (!found){
    for(i in 0 until values.size-currentPreambleSize)
    {
        val currentPreamble = values.subList(i, i+currentPreambleSize)
        if (currentPreamble.sum() == breakingValue){
            val maxValue = currentPreamble.max()!!
            val minValue = currentPreamble.min()!!
            println("max: $maxValue min: $minValue result: ${maxValue+minValue}")
            found = true
        }
    }
    currentPreambleSize++
}