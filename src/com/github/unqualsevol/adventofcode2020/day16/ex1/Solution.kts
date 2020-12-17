import java.io.File

val input = File("../input").readLines()
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

val result = nearbyTickets.map { ticket ->
    ticket.map { field ->
        if (rules.map { it.valid(field) }.count { it == true } > 0) 0 else field
    }.sum()
}.sum()
println("the ticket scanning error rate is $result")
