import java.io.File

val increment = 3
var current = 0
val tree = '#'
val openSquare = '.'

var treesCount = 0;
File("input").forEachLine {
    val linePattern = it
    if(linePattern.get(current) == tree) treesCount++;
    current = (current + increment) % linePattern.length
}
println("trees: $treesCount")