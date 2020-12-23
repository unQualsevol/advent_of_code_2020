import java.io.File

val foods = File("input").readLines().map {
    val ingredients = it.substringBefore('(').trim().split(" ")
    val allergens = it.substring(it.indexOf('(') + 1, it.lastIndex).replace("contains ", "").split(", ")
    Pair(ingredients, allergens)
}

val ingredients = foods.map { it.first }.flatten().toSet()
val allergens = foods.map { it.second }.flatten().toSet()

val allergenToFood = allergens.map { allergen ->
    allergen to foods.filter { food -> food.second.contains(allergen) }.map { food -> food.first.toMutableList() }
}.toMap()

val mapAllergenIngredient = mutableMapOf<String, MutableList<String>>()
var currentIngredients = ingredients.toMutableList()
for (allergen in allergenToFood) {
    for (ingredient in currentIngredients) {
        if (allergen.value.all { it.contains(ingredient) }) {
            mapAllergenIngredient.getOrPut(allergen.key, { mutableListOf() }).add(ingredient)
        }
    }
}

while (!mapAllergenIngredient.all { it.value.size == 1 }) {
    val uniqueAllergenIngredient = mapAllergenIngredient.filter { it.value.size == 1 }.map { it.value }.flatten()
    mapAllergenIngredient.filter {it.value.size > 1}.forEach {
        it.value.removeAll(uniqueAllergenIngredient)
    }
}
mapAllergenIngredient.forEach { println(it) }

val result = foods.map { it.first }.flatten().filterNot { mapAllergenIngredient.values.flatten().contains(it) }.count()
println("times do any of those ingredients appear $result")

println("canonical dangerous ingredient list: ${mapAllergenIngredient.toSortedMap().map { it.value }.flatten().joinToString (",")}")