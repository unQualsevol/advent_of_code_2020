import java.io.File

val increment = 3
var current = 0
val tree = '#'

var treesCount = 0
File("input").forEachLine {
    val linePattern = it
    if(linePattern[current] == tree) treesCount++
    current = (current + increment) % linePattern.length
}
println("trees: $treesCount")