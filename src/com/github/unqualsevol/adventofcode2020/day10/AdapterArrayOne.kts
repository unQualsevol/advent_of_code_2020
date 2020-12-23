import java.io.File

val joltageRatings = File("input").readLines().map { it.toInt() }.sorted().toMutableList()
joltageRatings.add(0,0)
joltageRatings.add(joltageRatings.max()!!+3)

val differences = mutableListOf<Int>()
for(i in 0..joltageRatings.size-2) {
    differences.add(joltageRatings[i + 1] - joltageRatings[i])
}
val count1 = differences.count { it == 1 }
val count3 = differences.count { it == 3 }
println(" Count1: $count1 Count3: $count3 result: ${count1*count3}")