val input = "952438716"
val dataSize = 9

data class Cup(val id: Int) {
    var right: Cup = this
    var left: Cup = this
}

val cups = input.map { Cup(it.toString().toInt()) }.toMutableList()
for(i in cups.indices) {
    cups[i].left = cups[if(i-1 >= 0) i-1 else cups.lastIndex]
    cups[i].right = cups[if(i+1 <= cups.lastIndex) i+1 else 0]
}

val sortedCups = cups.sortedBy { it.id }

//start pointing to previous cup
var currentCup = cups.first().left
for (i in 0 until 100) {
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
print("The label on the cups is: ")
var current = sortedCups[0].right
while(current != sortedCups[0]) {
    print(current.id)
    current = current.right
}
println()

fun findNextCup(startCup: Cup, pickup: List<Int>): Cup {
    var currentId = startCup.id
    do {
        currentId = if (currentId - 1 == 0) dataSize else currentId - 1
    } while (pickup.contains(currentId))
    return sortedCups[currentId-1]
}
