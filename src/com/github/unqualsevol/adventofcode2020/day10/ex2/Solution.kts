import java.io.File

val preambleSize = 25
val joltageRatings = File("../input").readLines().map { it.toInt() }.sorted().toMutableList()
joltageRatings.add(0, 0)
joltageRatings.add(joltageRatings.max()!! + 3)

var pathCount = combination(joltageRatings)
println("possible paths $pathCount")

fun combination(joltageRatings: List<Int>, combinationMemo: MutableMap<String, Long> = mutableMapOf()): Long {
    val key = joltageRatings.joinToString()
    if (combinationMemo.containsKey(key)) {
        return combinationMemo.get(key)!!
    }

    var result = 1L
    for (i in 1 until joltageRatings.size-1) {
        val previous = joltageRatings[i - 1]
        val next = joltageRatings[i + 1]
        if (next - previous <= 3) {

            val mutableListOf = mutableListOf(previous)
            mutableListOf.addAll(joltageRatings.subList(i+1, joltageRatings.size))
            result += combination(mutableListOf, combinationMemo)
        }
    }
    combinationMemo.put(key, result)
    return result
}
