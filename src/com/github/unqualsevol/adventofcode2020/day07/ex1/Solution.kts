import java.io.File

val bagToParentBagMap = mutableMapOf<String, MutableList<String>>()
File("../input").forEachLine {
    val regex = """^(\w+\s\w+) bags contain (.*)\.$""".toRegex()
    val matchResult = regex.find(it)
    val (currentBag, listOfBags) = matchResult!!.destructured
//    val regexContent = """^((\d)\s(\w+\s\w+)\sbags?)(\,\s((\d)\s(\w+\s\w+))\sbags?)*.$""".toRegex()
    val regexContent = """((\w+\s\w+)\sbags?)+""".toRegex()
    val matchResult2 = regexContent.findAll(listOfBags)
    val childBags = mutableListOf<String>()
    bagToParentBagMap.getOrPut(currentBag, { mutableListOf() });
    matchResult2.forEach {
        val bag = it.groupValues.last()
        childBags.add(bag)
        bagToParentBagMap.getOrPut(bag, { mutableListOf() }).add(currentBag)
    }
}
val startKey = "shiny gold"
val queue = mutableListOf(startKey)
val bagsHavingShinyGold = mutableSetOf<String>()
do {
    val currentParents = bagToParentBagMap.get(queue.removeAt(0))
    bagsHavingShinyGold.addAll(currentParents!!)
    queue.addAll(currentParents)
} while (queue.isNotEmpty())
println("bags having shiny gold ${bagsHavingShinyGold.size}")
