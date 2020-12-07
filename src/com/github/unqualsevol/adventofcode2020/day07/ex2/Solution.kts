import java.io.File

data class QuantifiedColor(val quantity: Int, val name: String)

val bagToInsideBagMap = mutableMapOf<String, MutableList<QuantifiedColor>>()
File("../input").forEachLine {
    val regex = """^(\w+\s\w+) bags contain (.*)\.$""".toRegex()
    val matchResult = regex.find(it)
    val (currentBag, listOfBags) = matchResult!!.destructured
    val regexContent = """((\d+)\s(\w+\s\w+)\sbags?)+""".toRegex()
    val matchResult2 = regexContent.findAll(listOfBags)
    val childBags = mutableListOf<QuantifiedColor>()
    matchResult2.forEach {
        val bagQuantity = it.groupValues[it.groupValues.size - 2]
        val bagColor = it.groupValues.last()
        childBags.add(QuantifiedColor(bagQuantity.toInt(), bagColor))
    }
    bagToInsideBagMap.getOrPut(currentBag, { mutableListOf() }).addAll(childBags)
}


val startKey = "shiny gold"
fun calculateNumberOfInnerBags(bag: String, map: Map<String, List<QuantifiedColor>>): Int{
    var innerBagCount = 1
    val innerBags = map.get(bag)!!
    if(innerBags.isEmpty())
        return 1
    for (currentBag in innerBags) {
        val calculateNumberOfInnerBags = calculateNumberOfInnerBags(currentBag.name, map)
        innerBagCount += currentBag.quantity * calculateNumberOfInnerBags
    }
    return innerBagCount
}
println("bags inside a shiny gold bag ${calculateNumberOfInnerBags(startKey, bagToInsideBagMap)-1}")
