import java.io.File

val ground = '.'
val empty = 'L'
val occupied = '#'

var map = File("input").readLines()
var count = 0
do {
    println("Round ${++count}")
    val oldMap = map
    val newMap = runSeatRound(map)
    map = newMap
} while (oldMap != newMap)
println("occupied seats: ${map.map { line -> line.count { it == occupied } }.sum()}")


fun runSeatRound(map:List<String>): List<String> {
    val newMap = mutableListOf<String>()
    for (i in map.indices) {
        val currentRow = map[i]
        val newRow = StringBuilder()
        for (j in currentRow.indices) {
            val currentCell = currentRow[j]
            if (currentCell == ground) {
                newRow.append(ground)
            } else {
                if (currentCell == empty) {
                    if (calculateAdjacentEmptySeats(map, i, j) == 8) {
                        newRow.append(occupied)
                    } else {
                        newRow.append(empty)
                    }
                } else if (currentCell == occupied) {
                    if (calculateAdjacentOccupiedSeats(map, i, j) > 3) {
                        newRow.append(empty)
                    } else {
                        newRow.append(occupied)
                    }

                }

            }
        }
        newMap.add(String(newRow))
    }
    newMap.forEach { println(it) }
    return newMap
}

fun calculateAdjacentEmptySeats(seatMap: List<String>, row: Int, column: Int): Int {
    var emptySeats = 8
    for (i in - 1 .. + 1) {
        for (j in - 1 .. + 1) {
            if(i == 0 && j == 0) continue
            val currentRow = row + i
            val currentColumn = column +j
            if (seatMap.indices.contains(currentRow)
                    && seatMap[currentRow].indices.contains(currentColumn)
                    && seatMap[currentRow][currentColumn] == occupied) {
                emptySeats--
            }
        }
    }
    return emptySeats
}

fun calculateAdjacentOccupiedSeats(seatMap: List<String>, row: Int, column: Int): Int {
    var occupiedSeats = 0
    for (i in - 1 .. + 1) {
        for (j in - 1 .. + 1) {
            if(i == 0 && j == 0) continue
            val currentRow = row + i
            val currentColumn = column +j
            if (seatMap.indices.contains(currentRow)
                    && seatMap[currentRow].indices.contains(currentColumn)
                    && seatMap[currentRow][currentColumn] == occupied) {
                occupiedSeats++
            }
        }
    }
    return occupiedSeats
}