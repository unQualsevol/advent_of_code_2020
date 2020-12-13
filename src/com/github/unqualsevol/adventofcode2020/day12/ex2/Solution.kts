import java.io.File
import kotlin.math.absoluteValue

enum class Turn {
    D90 {
        override fun left(waypoint: Coordinate): Coordinate = Coordinate(-waypoint.y, waypoint.x)

        override fun right(waypoint: Coordinate): Coordinate = Coordinate(waypoint.y, -waypoint.x)
    },
    D180 {
        override fun left(waypoint: Coordinate): Coordinate = Coordinate(-waypoint.x, -waypoint.y)

        override fun right(waypoint: Coordinate): Coordinate = Coordinate(-waypoint.x, -waypoint.y)
    },
    D270 {
        override fun left(waypoint: Coordinate): Coordinate = Coordinate(waypoint.y, -waypoint.x)

        override fun right(waypoint: Coordinate): Coordinate = Coordinate(-waypoint.y, waypoint.x)
    };

    abstract fun left(waypoint: Coordinate): Coordinate
    abstract fun right(waypoint: Coordinate): Coordinate
}

class TurnAdapter {

    fun toTurn(value: Int) : Turn {
        return when (value) {
            90 -> Turn.D90
            180 -> Turn.D180
            270 -> Turn.D270
            else -> throw IllegalArgumentException("Invalid value $value, must be 90, 180 or 270 ")
        }
    }
}

enum class Action {
    NORTH {
        override fun getNextState(shipState: ShipState, value: Int): ShipState =
                shipState.copy(waypoint = shipState.waypoint.copy(y = shipState.waypoint.y + value))
    },
    SOUTH {
        override fun getNextState(shipState: ShipState, value: Int): ShipState =
                shipState.copy(waypoint = shipState.waypoint.copy(y = shipState.waypoint.y - value))
    },
    EAST {
        override fun getNextState(shipState: ShipState, value: Int): ShipState =
                shipState.copy(waypoint = shipState.waypoint.copy(x = shipState.waypoint.x + value))
    },
    WEST {
        override fun getNextState(shipState: ShipState, value: Int): ShipState =
                shipState.copy(waypoint = shipState.waypoint.copy(x = shipState.waypoint.x - value))
    },
    LEFT {
        override fun getNextState(shipState: ShipState, value: Int): ShipState =
                shipState.copy(waypoint = TurnAdapter().toTurn(value).left(shipState.waypoint))
    },
    RIGHT {
        override fun getNextState(shipState: ShipState, value: Int): ShipState =
                shipState.copy(waypoint = TurnAdapter().toTurn(value).right(shipState.waypoint))
    },
    FORWARD {
        override fun getNextState(shipState: ShipState, value: Int): ShipState =
                shipState.copy(position = shipState.position.plus(shipState.waypoint.times(value)))
    };

    abstract fun getNextState(shipState: ShipState, value: Int): ShipState


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


data class Coordinate(val x: Int, val y: Int) {
    fun plus(other: Coordinate): Coordinate = Coordinate(this.x + other.x, this.y + other.y)

    fun times(value: Int): Coordinate = Coordinate(this.x * value, this.y * value)
}

data class ShipState(val position: Coordinate, val waypoint: Coordinate) {
    fun distanceTo(other: ShipState): Int {
        return (this.position.x - other.position.x).absoluteValue + (this.position.y - other.position.y).absoluteValue
    }
}

data class Instruction(val action: Action, val value: Int) {
    fun getNextPosition(shipState: ShipState): ShipState {
        return this.action.getNextState(shipState, this.value)
    }
}

val adapter = ActionAdapter()

var instructions = File("../input").readLines().map { Instruction(adapter.adapt(it.first()), it.substring(1 until it.length).toInt()) }

val initialPosition = ShipState(Coordinate(0, 0), Coordinate(10, 1))

var currentPosition = initialPosition
println(currentPosition)
for (instruction in instructions) {
    currentPosition = instruction.getNextPosition(currentPosition)
    println("after instruction: $instruction the position is $currentPosition")
}
println("Manhattan distance: ${initialPosition.distanceTo(currentPosition)}")
