import java.io.File

fun computeSeatId(seat: String): Int {
    val rowFactor = 8
    val row = toNumber(seat.substring(0..6), "F", "B")
    val column = toNumber(seat.substring(7..9), "L", "R")
    return row * rowFactor + column
}

fun toNumber(passportData: String, zero: String, one: String): Int {
    val replace = passportData.replace(zero, "0").replace(one, "1")
    return replace.toInt(2)
}

val seatIds = mutableListOf<Int>()
File("input").forEachLine {
    seatIds.add(computeSeatId(it))
}
val minSeatId = seatIds.min()!!
val maxSeatId = seatIds.max()!!

for (mySeat in minSeatId..maxSeatId) {
    if(!seatIds.contains(mySeat) && seatIds.containsAll(arrayListOf(mySeat-1, mySeat+1))){
        println("mySeat: $mySeat")
    }
}