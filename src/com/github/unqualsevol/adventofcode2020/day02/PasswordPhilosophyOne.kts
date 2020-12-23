import java.io.File

var validCount = 0
File("input").forEachLine { line ->
    val regex = """(\d+)-(\d+)\s(\w):\s(.*)""".toRegex()
    val matchResult = regex.find(line)
    val (minString, maxString, letterString, pass) = matchResult!!.destructured
    val min = minString.toInt()
    val max = maxString.toInt()
    val letter = letterString[0]
    val count = pass.chars().filter { it.toChar() == letter }.count()
    if(count in min..max) {
        validCount++
    }
}
println("Total: $validCount")