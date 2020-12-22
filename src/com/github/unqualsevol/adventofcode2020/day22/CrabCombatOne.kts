import java.io.File

data class Player(val id: Int, val deck: MutableList<Int>) {
    fun score(): Long = deck.reversed().mapIndexed { index, card -> (index + 1).toLong() * card.toLong() }.sum()
    fun hasCards(): Boolean = deck.isNotEmpty()
    fun deal(): Int = deck.removeAt(0)
    fun placeToBottom(firstCard: Int, secondCard: Int) {
        deck.add(firstCard)
        deck.add(secondCard)
    }
}

val input = File("input").readLines()
val indexOfSpace = input.indexOf("")

val playerOne = Player(1, input.subList(1, indexOfSpace).map { it.toInt() }.toMutableList())
val playerTwo = Player(2, input.subList(indexOfSpace + 2, input.size).map { it.toInt() }.toMutableList())

val winner = play(playerOne, playerTwo)
val result = winner.score()
println("Winner score is: $result")

fun play(playerOne: Player, playerTwo: Player): Player {
    while (playerOne.hasCards() && playerTwo.hasCards()) {
        val cardOne = playerOne.deal()
        val cardTwo = playerTwo.deal()
        if (cardOne > cardTwo) {
            playerOne.placeToBottom(cardOne, cardTwo)
        } else {
            playerTwo.placeToBottom(cardTwo, cardOne)
        }
    }
    return if(playerOne.hasCards()) playerOne else playerTwo
}
