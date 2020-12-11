import java.io.File

val ground = '.'
val empty = 'L'
val occupied = '#'

var map = File("../input").readLines()
do {
    val oldMap = map;
    val newMap = runSeatRound(map)
    map = newMap
} while (!oldMap.equals(newMap))
println("occupied seats: ${map.map { it.count { it == occupied } }.sum()}")


fun runSeatRound(map: List<String>): List<String> {
    val newMap = mutableListOf<String>()
    for (i in map.indices) {
        val currentRow = map[i]
        var newRow = StringBuilder()
        for (j in currentRow.indices) {
            val currentCell = currentRow[j]
            newRow.append(
                    when {
                        (currentCell == empty && calculateVisibleEmptySeats(map, i, j) == 8) -> occupied
                        (currentCell == occupied && calculateVisibleOccupiedSeats(map, i, j) > 4) -> empty
                        else -> currentCell
                    })
        }
        newMap.add(String(newRow))
    }
    return newMap
}

fun calculateVisibleEmptySeats(seatMap: List<String>, row: Int, column: Int): Int {
    return calculateVisibleSeats(seatMap, row, column, empty)
}

fun calculateVisibleOccupiedSeats(seatMap: List<String>, row: Int, column: Int): Int {
    return calculateVisibleSeats(seatMap, row, column, occupied)
}

fun calculateVisibleSeats(seatMap: List<String>, row: Int, column: Int, seat: Char): Int {
    var emptySeats = 0
    for (i in -1..+1) {
        for (j in -1..+1) {
            if (i == 0 && j == 0) continue
            if (seekforSeat(seatMap, row, column, i, j) == seat) {
                emptySeats++
            }
        }
    }
    return emptySeats
}

fun seekforSeat(seatMap: List<String>, row: Int, column: Int, dy: Int, dx: Int): Char {
    var currentRow = row + dy
    var currentColumn = column + dx
    while (seatMap.indices.contains(currentRow)
            && seatMap[currentRow].indices.contains(currentColumn)) {

        val currentCell = seatMap[currentRow][currentColumn]
        if (currentCell != ground) return currentCell
        currentRow += dy
        currentColumn += dx
    }

    return empty
}