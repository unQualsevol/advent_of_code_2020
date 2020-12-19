import java.io.File

val input = File("input").readLines()
val hasNumber = """\d+""".toRegex()
val startsWithAB = """^[a,b]""".toRegex()
val messages = input.filter { startsWithAB.containsMatchIn(it) }
val rulesMap = input.filter { hasNumber.containsMatchIn(it) }.map { rule ->
    val indexOfColon = rule.indexOf(':')
    rule.substring(0, indexOfColon) to rule.substring(indexOfColon + 1, rule.length).split("|").map { it.replace("\"".toRegex(), "").trim().split(" ") }
}.toMap()

val start = "0"
val rules = resolve(start, mutableMapOf())
println(rules.size)

val countMessagesMatching = messages.map { message -> rules.contains(message)}.count { it }
println("The number of messages that completely match rule 0 is: $countMessagesMatching")

fun resolve(ruleId: String, memo: MutableMap<String, List<String>>): List<String> {
    if (memo.containsKey(ruleId)) {
        return memo.getValue(ruleId)
    }
    if (ruleId.matches(startsWithAB)) {
        val value = mutableListOf(ruleId)
        memo[ruleId] = value
        return value
    }

    val result = rulesMap[ruleId]!!.map { options ->
        options.map { partialRegex -> resolve(partialRegex, memo) }.reduce { acc, list ->
            acc.map { item -> list.map { item + it } }.flatten()
        }
    }.flatten()
    memo[ruleId] = result
    return result
}
