import java.io.File

val input = File("input").readLines()
val regexRules = """(.+): (\d+)-(\d+) or (\d+)-(\d+)""".toRegex()


data class Rule(val name: String, val firstRange: IntRange, val secondRange: IntRange) {
    fun valid(field: Int): Boolean {
        return firstRange.contains(field) || secondRange.contains(field)
    }
}

var index = 0

val rules = input.subList(0, input.indexOfFirst { it == "" }).map {
    val (field, firstFrom, firstTo, secondFrom, secondTo) = regexRules.find(it)!!.destructured
    Rule(field, firstFrom.toInt()..firstTo.toInt(), secondFrom.toInt()..secondTo.toInt())
}

val myTicket = input.get(input.indexOfLast { it == "" } - 1).split(",").map { it.toInt() }

val nearbyTickets = input.subList(input.indexOfLast { it == "" } + 2, input.size).map {
    it.split(",").map { it.toInt() }
}

val validNearbyTickets = nearbyTickets.filter { ticket ->
    ticket.map { field ->
        if (rules.map { it.valid(field) }.count { it == true } > 0) 0 else field
    }.sum() == 0
}

val rulesToIndexes = rules.map { rule ->
    var indexes = mutableListOf<Int>()
    for (i in 0 until myTicket.size) {
        if (validNearbyTickets.map { ticket -> rule.valid(ticket[i]) }.count { it == false } == 0) {
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
