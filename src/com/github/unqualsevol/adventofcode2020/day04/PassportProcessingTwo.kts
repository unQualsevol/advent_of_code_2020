import java.io.File

val fields = listOf("""byr:(200[0-2]|19[2-9]\d)\s""", """iyr:20(20|1\d)\s""", """eyr:20(30|2\d)\s""", """hgt:(1([5-8]\d|9[0-3])cm|(59|6\d|7[0-6])in)\s""", """hcl:#[0-9,a-f]{6}\s""", """ecl:(amb|blu|brn|gry|grn|hzl|oth)\s""", """pid:\d{9}\s""")

fun countFields(passportData: String): Int {
    var matches = 0
    for (field in fields) {
        val regex = field.toRegex()
        print("${field.subSequence(0,4)} ${regex.findAll(passportData).count()} ")
        if (regex.containsMatchIn(passportData)) {
            matches++
        }
    }
    println()
    return matches
}

var count = 0
var currentBlock = ""
File("input").forEachLine {
    val currentLine = it
    if (currentLine.isBlank()) {
        val countFields = countFields("$currentBlock ")
        println("block: $currentBlock fields: $countFields")
        if(countFields == fields.size) {
            count++
        }
        currentBlock = ""
    } else {
        currentBlock += " $currentLine"
    }
}
println("total with 7 fields: $count")
