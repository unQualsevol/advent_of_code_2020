import java.io.File

val input = File("input").readLines()
val regexRules = """(.+): (\d+)-(\d+) or (\d+)-(\d+)""".toRegex()


data class Rule(val name: String, val firstRange: IntRange, val secondRange: IntRange) {
    fun valid(field: Int): Boolean {
        return firstRange.contains(field) || secondRange.contains(field)
    }
}

val rules = input.subList(0, input.indexOfFirst { it == "" }).map {
    val (field, firstFrom, firstTo, secondFrom, secondTo) = regexRules.find(it)!!.destructured
    Rule(field, firstFrom.toInt()..firstTo.toInt(), secondFrom.toInt()..secondTo.toInt())
}

val myTicket = input[input.indexOfLast { it == "" } - 1].split(",").map { it.toInt() }

val nearbyTickets = input.subList(input.indexOfLast { it == "" } + 2, input.size).map { line ->
    line.split(",").map { it.toInt() }
}

val validNearbyTickets = nearbyTickets.filter { ticket ->
    ticket.map { field ->
        if (rules.any { it.valid(field) }) 0 else field
    }.sum() == 0
}

val rulesToIndexes = rules.map { rule ->
    val indexes = mutableListOf<Int>()
    for (i in myTicket.indices) {
        if (validNearbyTickets.all { ticket -> rule.valid(ticket[i]) }) {
            indexes.add(i)
        }
    }
    Pair(rule.name, indexes)
}.toMutableList()

val rulesToIndex = mutableListOf<Pair<String, Int>>()
while (rulesToIndex.count { it.first.startsWith("departure") } < 6) {
    val ruleDefined = rulesToIndexes.first { it.second.size == 1 }
    if (ruleDefined.first.startsWith("departure")) {
        rulesToIndex.add(Pair(ruleDefined.first, ruleDefined.second[0]))
    }
    rulesToIndexes.remove(ruleDefined)
    rulesToIndexes.forEach { it.second.removeAll(ruleDefined.second) }
}


val result = rulesToIndex.map { myTicket[it.second].toLong() }.reduce { acc, l -> acc * l }
println("six fields product: $result")
