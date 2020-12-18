import java.io.File

val entries = mutableListOf<Int>()

File("../input").forEachLine {
    val currentEntry = it.toInt()
    val first = currentEntry;
    for (j in 0..entries.size-1) {
        val second = entries[j]
        val partial = first+second
        if(partial >= 2020) continue;
        val expectedThird = 2020 - partial
        if(entries.contains(expectedThird)) {
            println("val1: $first val2: $second val3: $expectedThird result ${first * second * expectedThird}")
            return@forEachLine
        }
    }
    entries.add(currentEntry)
}