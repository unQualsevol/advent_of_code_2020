package com.github.unqualsevol.adventofcode2020.day20

import java.io.File

enum class Side {
    TOP, BOTTOM, LEFT, RIGHT, NO_MATCH
}

data class Tile(val id: Int, val content: List<String>) {
    private val top = content.first()
    private val bottom = content.last()
    private val left = content.map { it.first() }.joinToString("")
    private val right = content.map { it.last() }.joinToString("")
    private val reverseTop = top.reversed()
    private val reverseBottom = bottom.reversed()
    private val reverseLeft = left.reversed()
    private val reverseRight = right.reversed()

    fun topEdge(): Pair<Int, Int> = Pair(toInt(top), toInt(reverseTop))
    fun bottomEdge(): Pair<Int, Int> = Pair(toInt(bottom), toInt(reverseBottom))
    fun leftEdge(): Pair<Int, Int> = Pair(toInt(left), toInt(reverseLeft))
    fun rightEdge(): Pair<Int, Int> = Pair(toInt(right), toInt(reverseRight))

    fun match(edge: Pair<Int, Int>): Side {
        return when {
            matchTop(edge) -> Side.TOP
            matchBottom(edge) -> Side.BOTTOM
            matchLeft(edge) -> Side.LEFT
            matchRight(edge) -> Side.RIGHT
            else -> Side.NO_MATCH
        }
    }

    fun otherSide(side: Side): Pair<Int, Int> {
        return when (side) {
            Side.TOP -> bottomEdge()
            Side.BOTTOM -> topEdge()
            Side.LEFT -> rightEdge()
            Side.RIGHT -> leftEdge()
            Side.NO_MATCH -> throw IllegalStateException("cannot find other side of unmatched")
        }
    }

    private fun matchTop(edge: Pair<Int, Int>): Boolean = match(edge, topEdge())
    private fun matchBottom(edge: Pair<Int, Int>): Boolean = match(edge, bottomEdge())
    private fun matchLeft(edge: Pair<Int, Int>): Boolean = match(edge, leftEdge())
    private fun matchRight(edge: Pair<Int, Int>): Boolean = match(edge, rightEdge())

    fun edges(): List<Pair<Int, Int>> = listOf(topEdge(), bottomEdge(), leftEdge(), rightEdge())

    private fun match(one: Pair<Int, Int>, two: Pair<Int, Int>) = one == two || one == two.copy(first = two.second, second = two.first)

    private fun toInt(stringValue: String): Int = stringValue.replace(".", "0").replace("#", "1").toInt(2)
}


fun <T> match(one: Pair<T, T>, two: Pair<T, T>): Boolean = one == two || one == two.copy(first = two.second, second = two.first)

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
//                  #
//#    ##    ##    ###
// #  #  #  #  #  #
val monsterPattern = listOf(Pair(0, 18),
        Pair(1, 0), Pair(1, 5), Pair(1, 6), Pair(1, 11), Pair(1, 12), Pair(1, 17), Pair(1, 18), Pair(1, 19),
        Pair(2, 1), Pair(2, 4), Pair(2, 7), Pair(2, 10), Pair(2, 13), Pair(2, 16))

val mainUniqueEdgesByTile = calculateUniqueEdgesByTile(tileMap.values)
val mainCorners = calculateCornersAndSides(mainUniqueEdgesByTile).first.map { Pair(it, mainUniqueEdgesByTile.getValue(it.id)) }

val orderedTiles = mutableListOf<List<Tile>>()
var line = 0
while (tileMap.size > 12) {
    orderedTiles.add(calculateRow())
    line++
}
orderedTiles.add(calculateLastRow())
val map = toMap(orderedTiles.map { row ->
    row.map { tile ->
        Tile(tile.id, tile.content.subList(1, tile.content.lastIndex).map { line -> line.substring(1, line.lastIndex) })
    }
}, 7)

findMonsters(map)
findMonsters(map.reversed())
findMonsters(map.map { it.reversed() })
findMonsters(map.reversed().map { it.reversed() })
findMonsters(rotateLeft(map))
findMonsters(rotateLeft(map).reversed())
findMonsters(rotateLeft(map).map { it.reversed() })
findMonsters(rotateLeft(map).reversed().map { it.reversed() })

fun findMonsters(map: List<String>) {
    val currentMap = map.toMutableList()
    var monsterCount = 0
    for (i in 0 until 93) {
        for (j in 0 until 76) {
            val patternOnMap = monsterPattern.map { Pair(it.first + i, it.second + j) }
            if (patternOnMap.all { currentMap[it.first][it.second] == '#' }) {
                patternOnMap.forEach {
                    currentMap[it.first] = currentMap[it.first].replaceRange(it.second..it.second, "O")
                }
                monsterCount++
            }
        }
    }
    if (monsterCount > 0) {
        println("'#' that are not part of a sea monster: ${currentMap.map { it.count { it == '#' } }.sum()}")
    }
}


fun toMap(tileMatrix: List<List<Tile>>, length: Int): List<String> {
    return tileMatrix.map { row ->
        val list = mutableListOf<String>()
        for (line in 0..length) {
            list.add(row.joinToString("") { it.content[line] })
        }
        list
    }.flatten()
}

fun calculateUniqueEdgesByTile(tiles: MutableCollection<Tile>): Map<Int, List<Pair<Int, Int>>> {

    val edges = tiles.map { it.edges() }.flatten()
    return tiles.map { tile ->
        Pair(tile.id,
                tile.edges().filter { tileEdge -> edges.count { match(it, tileEdge) } == 1 })
    }.filterNot { it.second.isEmpty() }
            .sortedBy { it.second.size }.reversed()
            .map { it.first to it.second }.toMap()
}

fun calculateCornersAndSides(uniqueEdgesByTile: Map<Int, List<Pair<Int, Int>>>): Pair<MutableList<Tile>, MutableList<Tile>> {

    val corners = uniqueEdgesByTile.filter { it.value.size == 2 }.map { tileMap[it.key]!! }.toMutableList()
    val sides = uniqueEdgesByTile.filter { it.value.size == 1 }.map { tileMap[it.key]!! }.toMutableList()
    return Pair(corners, sides)
}

fun calculateRow(): List<Tile> {
    val uniqueEdgesByTile = calculateUniqueEdgesByTile(tileMap.values)
    val (corners, sides) = calculateCornersAndSides(uniqueEdgesByTile)

    val topLeftCorner = if (line == 0) {
        val newCorner = corners.first()
        val uniqueEdges = uniqueEdgesByTile.getValue(newCorner.id)
        moveCornerToTopLeft(newCorner, uniqueEdges[0], uniqueEdges[1])
    } else {
        val topEdge = orderedTiles[line - 1][0].bottomEdge()
        val newCorner = corners.first { cornerTile -> cornerTile.match(topEdge) != Side.NO_MATCH }
        //align newCorner.top to be
        moveCornerToTopLeft(newCorner, topEdge, uniqueEdgesByTile.getValue(newCorner.id).filterNot { match(it, topEdge) }.first())
    }

    val currentRow = mutableListOf(topLeftCorner)
    corners.removeIf { it.id == topLeftCorner.id }
    tileMap.remove(topLeftCorner.id)

//CREATE THE FIRST ROW
    var currentTile = topLeftCorner
//until find a corner edge look at sides
    var column = 1
    while (corners.all { tileOnCorner -> tileOnCorner.match(currentTile.rightEdge()) == Side.NO_MATCH }) {

        //look in the sides
        val newMiddle = sides.first { tileOnSide -> tileOnSide.match(currentTile.rightEdge()) != Side.NO_MATCH }
        val alignedTopTile = moveToAlignedTop(currentTile, newMiddle, uniqueEdgesByTile.getValue(newMiddle.id))
        tileMap.remove(alignedTopTile.id)
        sides.removeIf { it.id == alignedTopTile.id }
        currentRow.add(alignedTopTile)
        currentTile = alignedTopTile
        column++
    }
    val newCorner = corners.first { tileOnCorner -> tileOnCorner.match(currentTile.rightEdge()) != Side.NO_MATCH }
    val leftEdge = currentTile.rightEdge()
    val topEdge = if (line == 0) {
        val matchingSide = newCorner.match(leftEdge)
        val otherSide = newCorner.otherSide(matchingSide)
        uniqueEdgesByTile.getValue(newCorner.id).filterNot { match(it, otherSide) }.first()
    } else {
        orderedTiles[line - 1][column].bottomEdge()
    }
    val topRightCorner = moveCornerToTopLeft(newCorner, topEdge, leftEdge)
    currentRow.add(moveCornerToAlignedTopRight(topRightCorner, uniqueEdgesByTile.getValue(topRightCorner.id)))
    corners.removeIf { it.id == topRightCorner.id }
    tileMap.remove(topRightCorner.id)
//orderedTiles.forEach { println(it.edges()) }
    return currentRow
}

fun moveCornerToTopLeft(tile: Tile, top: Pair<Int, Int>, left: Pair<Int, Int>): Tile {
    return when (Pair(tile.match(top), tile.match(left))) {
        Pair(Side.TOP, Side.LEFT) -> tile
        Pair(Side.TOP, Side.RIGHT) -> Tile(tile.id, tile.content.map { it.reversed() })
        Pair(Side.BOTTOM, Side.LEFT) -> Tile(tile.id, tile.content.reversed())
        Pair(Side.BOTTOM, Side.RIGHT) -> Tile(tile.id, tile.content.reversed().map { it.reversed() })
        Pair(Side.LEFT, Side.TOP) -> Tile(tile.id, rotateLeft(tile.content).reversed())
        Pair(Side.LEFT, Side.BOTTOM) -> Tile(tile.id, rotateRight(tile.content))
        Pair(Side.RIGHT, Side.TOP) -> Tile(tile.id, rotateLeft(tile.content))
        Pair(Side.RIGHT, Side.BOTTOM) -> Tile(tile.id, rotateLeft(tile.content).map { it.reversed() })
        else -> throw IllegalStateException("This is not a corner!")
    }
}

fun moveCornerToAlignedTopRight(tile: Tile, uniqueEdges: List<Pair<Int, Int>>): Tile {
    val sides = uniqueEdges.map { tile.match(it) }
    val corner = Pair(sides.first(), sides.last())
    return when {
        match(corner, Pair(Side.TOP, Side.LEFT)) -> Tile(tile.id, tile.content.map { it.reversed() })
        match(corner, Pair(Side.TOP, Side.RIGHT)) -> tile
        match(corner, Pair(Side.BOTTOM, Side.LEFT)) -> Tile(tile.id, rotateLeft(tile.content).reversed())
        match(corner, Pair(Side.BOTTOM, Side.RIGHT)) -> Tile(tile.id, tile.content.reversed())
        else -> throw IllegalStateException("This is not a corner!")
    }
}

fun moveToAlignedTop(current: Tile, new: Tile, uniqueEdges: List<Pair<Int, Int>>): Tile {
    var pointingUp = when (uniqueEdges.map { new.match(it) }.first()) {
        Side.TOP -> new
        Side.BOTTOM -> Tile(new.id, new.content.reversed())
        Side.LEFT -> Tile(new.id, rotateRight(new.content))
        Side.RIGHT -> Tile(new.id, rotateLeft(new.content))
        Side.NO_MATCH -> throw IllegalStateException("this tile is not at side!")
    }
    if (pointingUp.match(current.rightEdge()) == Side.RIGHT) {
        pointingUp = Tile(pointingUp.id, pointingUp.content.map { it.reversed() })
    }
    return pointingUp
}

fun rotateRight(content: List<String>): List<String> {
    val size = content.size
    val result = mutableListOf<String>()
    for (i in 0 until size) {
        result.add(content.map { it[i] }.joinToString(""))
    }
    return result
}

fun rotateLeft(content: List<String>): List<String> {
    val size = content.size
    val result = mutableListOf<String>()
    for (i in size - 1 downTo 0) {
        result.add(content.map { it[i] }.joinToString(""))
    }
    return result
}

fun calculateLastRow(): List<Tile> {
    val orderedRow = mutableListOf<Tile>()
    val leftTopEdge = orderedTiles[10][0].bottomEdge()
    val bottomLeftCornerWithEdges = mainCorners.first { it.first.match(leftTopEdge) != Side.NO_MATCH }
    var bottomLeftCorner = bottomLeftCornerWithEdges.first
    val matchingSide = bottomLeftCorner.match(leftTopEdge)
    val otherSide = bottomLeftCorner.otherSide(matchingSide)
    val leftLeftEdge = mainUniqueEdgesByTile.getValue(bottomLeftCorner.id).filterNot { match(it, otherSide) }.first()
    bottomLeftCorner = moveCornerToTopLeft(bottomLeftCorner, leftTopEdge, leftLeftEdge)
    orderedRow.add(bottomLeftCorner)
    tileMap.remove(bottomLeftCorner.id)
    //transform to bottom left
    val bottomRightCornerWithEdges = mainCorners.first { it.first.match(orderedTiles[10][11].bottomEdge()) != Side.NO_MATCH }
    val bottomRightCorner = bottomRightCornerWithEdges.first
    tileMap.remove(bottomRightCorner.id)
    var currentTile = bottomLeftCorner
    var column = 1
    val remainingTiles = tileMap.values
    while (remainingTiles.isNotEmpty()) {
        if (column == 6) {
            //HACKING TO THE GATE
            currentTile = Tile(currentTile.id, currentTile.content.map { it.reversed() })
        }
        val newMiddle = remainingTiles.first { it.match(currentTile.rightEdge()) != Side.NO_MATCH }
        currentTile = moveCornerToTopLeft(newMiddle, orderedTiles[10][column].bottomEdge(), currentTile.rightEdge())
        orderedRow.add(currentTile)
        tileMap.remove(currentTile.id)
        remainingTiles.remove(newMiddle)
        column++
    }
    //transform to bottom right
    orderedRow.add(moveCornerToTopLeft(bottomRightCorner, orderedTiles[10][column].bottomEdge(), currentTile.rightEdge()))

    return orderedRow
}

