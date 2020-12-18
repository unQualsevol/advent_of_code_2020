import java.io.File
import java.lang.IllegalArgumentException
import kotlin.math.absoluteValue

enum class Direction {
    NORTH {
        override fun left(degrees: Int): Direction {
            return when (degrees) {
                90 -> WEST
                180 -> SOUTH
                270 -> EAST
                else -> this
            }
        }

        override fun right(degrees: Int): Direction {
            return when (degrees) {
                90 -> EAST
                180 -> SOUTH
                270 -> WEST
                else -> this
            }
        }
        override fun move(coordinate: Coordinate, increment: Int): Coordinate {
            return Coordinate(coordinate.x, coordinate.y + increment)
        }
    },
    SOUTH {
        override fun left(degrees: Int): Direction {
            return when (degrees) {
                90 -> EAST
                180 -> NORTH
                270 -> WEST
                else -> this
            }
        }

        override fun right(degrees: Int): Direction {
            return when (degrees) {
                90 -> WEST
                180 -> NORTH
                270 -> EAST
                else -> this
            }
        }
        override fun move(coordinate: Coordinate, increment: Int): Coordinate {
            return Coordinate(coordinate.x, coordinate.y - increment)
        }
    },
    EAST {
        override fun left(degrees: Int): Direction {
            return when (degrees) {
                90 -> NORTH
                180 -> WEST
                270 -> SOUTH
                else -> this
            }
        }

        override fun right(degrees: Int): Direction {
            return when (degrees) {
                90 -> SOUTH
                180 -> WEST
                270 -> NORTH
                else -> this
            }
        }
        override fun move(coordinate: Coordinate, increment: Int): Coordinate {
            return Coordinate(coordinate.x + increment, coordinate.y)
        }
    },
    WEST {
        override fun left(degrees: Int): Direction {
            return when (degrees) {
                90 -> SOUTH
                180 -> EAST
                270 -> NORTH
                else -> this
            }
        }

        override fun right(degrees: Int): Direction {
            return when (degrees) {
                90 -> NORTH
                180 -> EAST
                270 -> SOUTH
                else -> this
            }
        }
        override fun move(coordinate: Coordinate, increment: Int): Coordinate {
            return Coordinate(coordinate.x - increment, coordinate.y)
        }
    };

    abstract fun left(degrees: Int): Direction
    abstract fun right(degrees: Int): Direction
    abstract fun move(coordinate: Coordinate, increment: Int): Coordinate
}

enum class Action {
    NORTH {
        override fun getNextCoordinate(shipState: ShipState, value: Int): Coordinate {
            return Direction.NORTH.move(shipState.position, value)
        }
    },
    SOUTH {
        override fun getNextCoordinate(shipState: ShipState, value: Int): Coordinate {
            return Direction.SOUTH.move(shipState.position, value)
        }
    },
    EAST {
        override fun getNextCoordinate(shipState: ShipState, value: Int): Coordinate {
            return Direction.EAST.move(shipState.position, value)
        }
    },
    WEST {
        override fun getNextCoordinate(shipState: ShipState, value: Int): Coordinate {
            return Direction.WEST.move(shipState.position, value)
        }
    },
    LEFT {
        override fun getNextDirection(direction: Direction, degrees: Int): Direction = direction.left(degrees)

        override fun getNextCoordinate(shipState: ShipState, value: Int): Coordinate = shipState.position
    },
    RIGHT {
        override fun getNextDirection(direction: Direction, degrees: Int): Direction = direction.right(degrees)

        override fun getNextCoordinate(shipState: ShipState, value: Int): Coordinate = shipState.position
    },
    FORWARD {
        override fun getNextCoordinate(shipState: ShipState, value: Int): Coordinate {
            return shipState.direction.move(shipState.position, value)
        }
    };

    open fun getNextDirection(direction: Direction, degrees: Int): Direction = direction

    abstract fun getNextCoordinate(shipState: ShipState, value: Int): Coordinate

}

class ActionAdapter {

    val NORTH = 'N'
    val SOUTH = 'S'
    val EAST = 'E'
    val WEST = 'W'
    val LEFT = 'L'
    val RIGHT = 'R'
    val FORWARD = 'F'

    fun adapt(action: Char): Action = when (action) {
        NORTH -> Action.NORTH
        SOUTH -> Action.SOUTH
        EAST -> Action.EAST
        WEST -> Action.WEST
        LEFT -> Action.LEFT
        RIGHT -> Action.RIGHT
        FORWARD -> Action.FORWARD
        else -> throw IllegalArgumentException("Invalid instruction $action.")
    }
}


data class Coordinate(val x: Int, val y: Int)
data class ShipState(val direction: Direction, val position: Coordinate) {
    fun distanceTo(other: ShipState): Int {
        return (this.position.x - other.position.x).absoluteValue + (this.position.y - other.position.y).absoluteValue
    }
}

data class Instruction(val action: Action, val value: Int) {
    fun getNextPosition(shipState: ShipState): ShipState {
        return ShipState(this.action.getNextDirection(shipState.direction, this.value), this.action.getNextCoordinate(shipState, this.value))
    }
}

val adapter = ActionAdapter()

var instructions = File("input").readLines().map { Instruction(adapter.adapt(it.first()), it.substring(1 until it.length).toInt()) }

val initialPosition = ShipState(Direction.EAST, Coordinate(0, 0))

var currentPosition = initialPosition
for (instruction in instructions) {
    currentPosition = instruction.getNextPosition(currentPosition)
}
println("Manhattan distance: ${initialPosition.distanceTo(currentPosition)}")
