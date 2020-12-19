import java.io.File

val input = File("input").readLines()
val hasNumber = """\d+""".toRegex()
val startsWithAB = """^[a,b]""".toRegex()
val messages = input.filter { startsWithAB.containsMatchIn(it) }
val rulesMap = input.filter { hasNumber.containsMatchIn(it) }.map { rule ->
    val indexOfColon = rule.indexOf(':')
    rule.substring(0, indexOfColon) to rule.substring(indexOfColon + 1, rule.length).split("|").map { it.replace("\"".toRegex(), "").trim().split(" ") }
}.toMap().toMutableMap()

val memo1 = mutableMapOf<String, List<String>>()
val rules42 = resolve("42", memo1).joinToString("|")
val rules31 = resolve("31", memo1).joinToString("|")
val valid = """^($rules42){2}.*($rules31)$""".toRegex()

val startsWithRule42 = """^($rules42)+""".toRegex()
val endsWithRule31 = """($rules31)+$""".toRegex()

val filteredMessages = messages.filter { message ->
    message.matches(valid)
}.map { it.substring(16, it.length - 8) }

var countMessagesMatching = filteredMessages.count { it.isEmpty() }

countMessagesMatching += filteredMessages.filter { it.isNotEmpty() }.filter { message ->
    val n = startsWithRule42.find(message)?.value?.length ?: 0
    val m = endsWithRule31.find(message)?.value?.length ?: 0
    n >= m
}.count()-1
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
