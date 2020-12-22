import java.io.File

data class Player(val id: Int, val deck: MutableList<Int>) {
    fun score(): Int = deck.reversed().mapIndexed { index, card -> (index + 1) * card }.sum()
    fun hasCards(): Boolean = deck.isNotEmpty()
    fun hasAtLeast(cardNumber: Int): Boolean = deck.size >= cardNumber
    fun deal(): Int = deck.removeAt(0)
    fun subGameCards(number: Int): MutableList<Int> = deck.take(number).toMutableList()
    fun takeAll(): MutableList<Int> = deck.toMutableList()
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
    //storage of previous rounds
    val previousConfigurations = mutableListOf<Pair<Player, Player>>()
    while (playerOne.hasCards() && playerTwo.hasCards()) {
        //check if current game is in storage
        if (previousConfigurations.contains(Pair(playerOne, playerTwo))) {
            return playerOne
        }
        //save round to storage
        previousConfigurations.add(Pair(playerOne.copy(deck = playerOne.takeAll()), playerTwo.copy(deck = playerTwo.takeAll())))
        val cardOne = playerOne.deal()
        val cardTwo = playerTwo.deal()
        var winnerCondition = cardOne > cardTwo
        //if both has at least their cards in deck
        if (playerOne.hasAtLeast(cardOne) && playerTwo.hasAtLeast(cardTwo)) {
            //sub game is with the cards size on each player
            val winner = play(playerOne.copy(deck = playerOne.subGameCards(cardOne)), playerTwo.copy(deck = playerTwo.subGameCards(cardTwo)))
            winnerCondition = winner.id == playerOne.id

        }
        //then return winner of play
        if (winnerCondition) {
            playerOne.placeToBottom(cardOne, cardTwo)
        } else {
            playerTwo.placeToBottom(cardTwo, cardOne)
        }
    }
    return if (playerOne.hasCards()) playerOne else playerTwo
}
