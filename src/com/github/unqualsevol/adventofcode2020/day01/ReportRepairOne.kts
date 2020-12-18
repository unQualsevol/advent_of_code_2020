import java.io.File

val entries = mutableListOf<Int>()

File("input").forEachLine {
    val currentEntry = it.toInt()
    val opposite = 2020-currentEntry
    if(entries.contains(opposite)) {
        println("val1: $currentEntry val2: $opposite result ${currentEntry*opposite}")
    }
    entries.add(currentEntry)
}