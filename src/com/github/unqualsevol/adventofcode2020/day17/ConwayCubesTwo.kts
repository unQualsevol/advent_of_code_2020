import java.io.File

data class Coordinate(val x: Int, val y: Int, val z: Int, val w: Int)

val active = '#'
val inactive = '.'

var pocketState = File("input").readLines().mapIndexed { indexY, s ->
    s.mapIndexed { indexX, c -> Pair(Coordinate(indexX, indexY, 0, 0), c) }
}.flatten().map { it.first to it.second }.toMap()

for (i in 1..6) {
    pocketState = calculateNewState(expand(pocketState))
}

val result = pocketState.count { it.value == active }
println("the active cubes are: $result")

fun calculateNewState(previousState: Map<Coordinate, Char>): Map<Coordinate, Char> {
    return previousState.map { calculateNewCubeState(it.key, it.value, previousState) }.map { it.first to it.second }.toMap()
}

fun calculateNewCubeState(cubeCoordinate: Coordinate, cubeState: Char, pocketState: Map<Coordinate, Char>): Pair<Coordinate, Char> {
    val countActiveAdjacent = getCountActiveAdjacent(cubeCoordinate, pocketState)
    val newCubeState = when {
        cubeState == active && !(countActiveAdjacent == 2 || countActiveAdjacent == 3) -> inactive
        cubeState == inactive && countActiveAdjacent == 3 -> active
        else -> cubeState
    }
    return Pair(cubeCoordinate, newCubeState)
}

fun getCountActiveAdjacent(cube: Coordinate, previousState: Map<Coordinate, Char>): Int {
    var count = 0
    for (w in -1..1) {
        for (z in -1..1) {
            for (y in -1..1) {
                for (x in -1..1) {
                    if (w == 0 && z == 0 && y == 0 && x == 0) {
                        continue
                    }
                    val orDefault = previousState.getOrDefault(Coordinate(cube.x - x, cube.y - y, cube.z - z, cube.w - w), inactive)
                    if (orDefault == active) count++;

                }
            }
        }
    }
    return count
}

fun expand(previousState: Map<Coordinate, Char>): Map<Coordinate, Char> {
    val expandedState = previousState.toMutableMap()
    expandedState.putAll(previousState.keys.map { expandCube(it, previousState.keys) }.flatten().map { Pair(it, inactive) })
    return expandedState
}

fun expandCube(cube: Coordinate, previousState: Set<Coordinate>): List<Coordinate> {
    val newCubes = mutableListOf<Coordinate>()
    for (x in -1..1) {
        for (y in -1..1) {
            for (z in -1..1) {
                for (w in -1..1) {
                    val newCoordinate = Coordinate(cube.x - x, cube.y - y, cube.z - z, cube.w - w)
                    if (!previousState.contains(newCoordinate)) {
                        newCubes.add(newCoordinate)
                    }
                }
            }
        }
    }
    return newCubes
}