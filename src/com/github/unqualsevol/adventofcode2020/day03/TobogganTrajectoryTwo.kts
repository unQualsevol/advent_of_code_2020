import java.io.File

val tree = '#'
val openSquare = '.'


val lines = File("input").readLines()

val horizontalIncrements = arrayOf(1, 3, 5, 7, 1)
val verticalIncrements = arrayOf(1, 1, 1, 1, 2)

var result: Long = 1;
for (i in horizontalIncrements.indices) {
    val increment = horizontalIncrements[i]
    var current = 0
    var treesCount = 0
    val verticalIncrement = verticalIncrements[i]
    for(j in 0..lines.size-1 step verticalIncrement){
        val line = lines[j]
        if (line.get(current) == tree) treesCount++
        current = (current + increment) % line.length
    }
    println("On $i trees: $treesCount")
    result *= treesCount
}
println("Result $result")