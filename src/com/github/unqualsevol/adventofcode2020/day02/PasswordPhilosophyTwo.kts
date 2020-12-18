import java.io.File

val entries = mutableListOf<Int>()
var validCount = 0;
File("input").forEachLine {
    val regex = """(\d+)-(\d+)\s(\w):\s(.*)""".toRegex()
    val matchResult = regex.find(it)
    val (firstPosition, secondPosition, letterString, pass) = matchResult!!.destructured
    val actualFirstPosition = firstPosition.toInt()-1
    val actualSecondPosition = secondPosition.toInt()-1
    val letter = letterString.get(0);
    val firstLetter = pass.get(actualFirstPosition);
    val secondLetter = pass.get(actualSecondPosition);
    if((letter == firstLetter).xor(letter == secondLetter)) {
        validCount++;
    }
}
println("Total: $validCount")