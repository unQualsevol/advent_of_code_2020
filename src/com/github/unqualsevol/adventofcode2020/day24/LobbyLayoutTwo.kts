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

var currentTileMap = tileMap.toMap()
for (day in 1..100) {
    currentTileMap = expand(currentTileMap)
    currentTileMap = currentTileMap.map { entry ->
        val newTile = entry.value.copy(color = computeColor(entry.value, currentTileMap))
        entry.key to newTile
    }.toMap()
    currentTileMap.values.count { it.color == Color.Black }
}
val result = currentTileMap.values.count { it.color == Color.Black }
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

fun computeColor(tile: Tile, tileMap: Map<Pair<Int, Int>, Tile>): Color {
    val adjacentTiles = listOfNotNull(tileMap[Pair(tile.x, tile.y + 2)],
            tileMap[Pair(tile.x + 1, tile.y + 1)],
            tileMap[Pair(tile.x + 1, tile.y - 1)],
            tileMap[Pair(tile.x, tile.y - 2)],
            tileMap[Pair(tile.x - 1, tile.y - 1)],
            tileMap[Pair(tile.x - 1, tile.y + 1)])
    //val whites = adjacentTiles.count { adjacentTile -> adjacentTile.color == Color.White }
    val blacks = adjacentTiles.count { adjacentTile -> adjacentTile.color == Color.Black }
    if (tile.color == Color.Black && (blacks == 0 || blacks > 2)) {
        return Color.White
    }
    if (tile.color == Color.White && blacks == 2) {
        return Color.Black
    }
    return tile.color
}

fun expand(tiles: Map<Pair<Int, Int>, Tile>): Map<Pair<Int, Int>, Tile> {
    val newTiles = mutableListOf<Tile>()
    tiles.values.forEach { tile ->
        newTiles.add(tile)
        val adjacentTiles = listOf(Tile(x = tile.x, y = tile.y + 2),
                Tile(x = tile.x + 1, y = tile.y + 1),
                Tile(x = tile.x + 1, y = tile.y - 1),
                Tile(x = tile.x, y = tile.y - 2),
                Tile(x = tile.x - 1, y = tile.y - 1),
                Tile(x = tile.x - 1, y = tile.y + 1)).filterNot { tiles.contains(Pair(it.x, it.y)) }
        newTiles.addAll(adjacentTiles)
    }
    return newTiles.map { Pair(it.x, it.y) to it }.toMap()
}
