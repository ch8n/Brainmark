package dev.ch8n.common.utils

fun String.removeWhiteSpace(): String {
    return this.toCharArray().filter { it != ' ' }.joinToString(separator = "")
}

fun String.characterAreSame(that: String): Boolean {
    val input1 = this.removeWhiteSpace().lowercase()
    val input2 = that.removeWhiteSpace().lowercase()
    return input1 == input2
}