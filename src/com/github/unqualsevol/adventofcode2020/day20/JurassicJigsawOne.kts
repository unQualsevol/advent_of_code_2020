import java.io.File

data class Tile(val id: Int, val content: MutableList<String>) {
    fun edges(): List<Int> {
        val top = content.first()
        val bottom = content.last()
        val left = content.map { it[0] }.joinToString("")
        val right = content.map { it.last() }.joinToString("")
        val reverseTop = top.reversed()
        val reverseBottom = bottom.reversed()
        val reverseLeft = left.reversed()
        val reverseRight = right.reversed()
        return listOf(top, bottom, left, right, reverseTop, reverseBottom, reverseLeft, reverseRight)
                .map { it.replace(".", "0").replace("#", "1").toInt(2) }
    }
}

val tileMap = mutableMapOf<Int, Tile>()

var currentId = 0
var currentList = mutableListOf<String>()
File("input").forEachLine { line ->
    when {
        line.startsWith("Tile") -> {
            currentId = line.substring(5, 9).toInt()
            currentList = mutableListOf()
        }
        line.isNotEmpty() -> currentList.add(line)
        else -> {
            tileMap[currentId] = Tile(currentId, currentList)
        }
    }
}

val edges = tileMap.values.map { it.edges() }.flatten()
val result = tileMap.values.map { tile ->
    Pair(tile.id, tile.edges().filter { tileEdge -> edges.count { it == tileEdge } == 1 }.count())
}.filter { it.second == 4 }.map { it.first.toLong() }.reduce { acc, l -> acc*l }
println("The IDs of the four corner tiles multiplied: $result")