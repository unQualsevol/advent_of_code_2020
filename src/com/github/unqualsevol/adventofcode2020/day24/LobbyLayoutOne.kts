import java.io.File

enum class Color {
    Black, White
}

enum class Direction(val code: String) {
    East("e") {
        override fun nextTile(currentTile: Tile): Tile = Tile(x = currentTile.x, y = currentTile.y + 2)
    },
    SouthEast("se") {
        override fun nextTile(currentTile: Tile): Tile = Tile(x = currentTile.x + 1, y = currentTile.y + 1)
    },
    SouthWest("sw") {
        override fun nextTile(currentTile: Tile): Tile = Tile(x = currentTile.x + 1, y = currentTile.y - 1)
    },
    West("w") {
        override fun nextTile(currentTile: Tile): Tile = Tile(x = currentTile.x, y = currentTile.y - 2)
    },
    NorthWest("nw") {
        override fun nextTile(currentTile: Tile): Tile = Tile(x = currentTile.x - 1, y = currentTile.y - 1)
    },
    NorthEast("ne") {
        override fun nextTile(currentTile: Tile): Tile = Tile(x = currentTile.x - 1, y = currentTile.y + 1)
    };

    abstract fun nextTile(currentTile: Tile): Tile
}

data class Tile(var color: Color = Color.White, val x: Int, val y: Int) {
    fun swap() {
        this.color = if (this.color == Color.White)
            Color.Black
        else
            Color.White
    }
}

val startingTile = Tile(Color.White, 0, 0)

val tileMap = mutableMapOf<Pair<Int, Int>, Tile>()

File("input").forEachLine { line ->
    val tile = computeTile(startingTile, line)
    tileMap.getOrPut(Pair(tile.x, tile.y), { tile }).swap()
}
val result = tileMap.values.count { it.color == Color.Black }
println("tiles that are left with the black side up: $result")

fun computeTile(currentTile: Tile, path: String): Tile {
    val direction = readMove(path)
    val newTile = direction.nextTile(currentTile)
    val remainingPath = path.substring(direction.code.length, path.length)
    return if (remainingPath.isEmpty()) {
        newTile
    } else
        computeTile(newTile, remainingPath)
}

fun readMove(line: String): Direction {
    return Direction.values().first { line.startsWith(it.code) }
}
