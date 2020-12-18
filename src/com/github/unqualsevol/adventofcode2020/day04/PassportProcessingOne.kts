import java.io.File

val fields = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")
val optionalFields = listOf("cid")

fun countFields(passportData: String): Int {
    var matches = 0;
    for (field in fields) {
        val regex = """$field:""".toRegex()
        if (regex.containsMatchIn(passportData)) {
            matches++;
        }
    }
    return matches;
}

var count = 0;
var currentBlock = "";
File("input").forEachLine {
    val currentLine = it
    if (currentLine.isBlank()) {
        val countFields = countFields(currentBlock)
        if(countFields == fields.size) {
            count++
        }
        currentBlock = ""
    } else {
        currentBlock += " $currentLine"
    }
}
println("total with 7 fields: $count")
