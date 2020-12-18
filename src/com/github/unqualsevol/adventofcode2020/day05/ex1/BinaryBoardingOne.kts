import java.io.File
import kotlin.math.max


fun computeSeatId(seat: String): Int {
    val rowFactor = 8
    val row = toNumber(seat.substring(0..6), "F", "B")
    val column = toNumber(seat.substring(7..9), "L", "R")
    val seatId = row * rowFactor + column
    return seatId
}

fun toNumber(passportData: String, zero: String, one: String): Int {
    val replace = passportData.replace(zero, "0").replace(one, "1")
    return replace.toInt(2)
}

var maxSeatId = 0;
File("../input").forEachLine {
    maxSeatId = max(maxSeatId, computeSeatId(it))
}
println("maxSeatId: $maxSeatId")
