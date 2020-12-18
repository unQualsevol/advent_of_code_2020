import java.io.File

val entries = mutableListOf<Int>()
var validCount = 0;
File("../input").forEachLine {
    val regex = """(\d+)-(\d+)\s(\w):\s(.*)""".toRegex()
    val matchResult = regex.find(it)
    val (minString, maxString, letterString, pass) = matchResult!!.destructured
    val min = minString.toInt()
    val max = maxString.toInt()
    val letter = letterString.get(0);
    val count = pass.chars().filter { it.toChar() == letter }.count()
    if(count >= min && count <= max) {
        //println("min: $min max: $max letter: $letter pass: $pass count: $count")
        validCount++;
    }
}
println("Total: $validCount")