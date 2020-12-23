val input = "952438716"
val dataSize = 1000000

data class Cup(val id: Int) {
    var right: Cup = this
    var left: Cup = this
}

val cups = input.map { Cup(it.toString().toInt()) }.toMutableList()
for(i in cups.indices) {
    cups[i].left = cups[if(i-1 >= 0) i-1 else cups.lastIndex]
    cups[i].right = cups[if(i+1 <= cups.lastIndex) i+1 else 0]
}

for (i in 10..dataSize) {
    val newCup = Cup(i)
    val firstCup = cups.first()
    val lastCup = cups.last()
    firstCup.left = newCup
    newCup.right = firstCup
    newCup.left = lastCup
    lastCup.right = newCup
    cups.add(newCup)
}


val sortedCups = cups.sortedBy { it.id }

var currentCup = cups.first().left
for (i in 0 until 10000000) {
    //pickup cup, will be always re next one
    currentCup = currentCup.right
    //pickup the 3 cups at the right
    val startPickup = currentCup.right
    val endPickup = startPickup.right.right
    //cut off the pickup
    currentCup.right = endPickup.right
    endPickup.right.left = currentCup
    //get the destination cup, it should be the decreasing next number that is not in pickup
    val destinationCup = findNextCup(currentCup, listOf(startPickup.id, startPickup.right.id, endPickup.id))
    //add the pickup after the destination
    val destinationRight = destinationCup.right
    destinationCup.right = startPickup
    startPickup.left = destinationCup
    endPickup.right = destinationRight
    destinationRight.left = destinationCup
}

val result = sortedCups[0].right.id.toLong()*sortedCups[0].right.right.id.toLong()
println("the two cups immediately clockwise of cup 1 multiplied is: $result")


fun findNextCup(startCup: Cup, pickup: List<Int>): Cup {
    var currentId = startCup.id
    do {
        currentId = if (currentId - 1 == 0) dataSize else currentId - 1
    } while (pickup.contains(currentId))
    return sortedCups[currentId-1]
}
