import java.io.File

val publicKeys = File("input").readLines().map { it.toLong() }
val cardSubjectNumber = 7L
val divisor = 20201227L

val loopSizes = publicKeys.map {
    var value = 1L
    var count = 0
    while (value != it) {
        value = (value * cardSubjectNumber) % divisor
        count++
    }
    count
}

var value = 1L
val iterations = loopSizes[0]
val publicKey = publicKeys[1]

for (i in 0 until iterations) {
    value = (value * publicKey) % divisor
}
println("The encryption key is: $value")