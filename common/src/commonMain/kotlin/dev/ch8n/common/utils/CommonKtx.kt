package dev.ch8n.common.utils

import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

fun Flow<*>.onceIn(coroutineScope: CoroutineScope) {
    val flow = this
    coroutineScope.launch { flow.firstOrNull() }
}

fun String.isValidUrl(): Boolean {
    val result = kotlin.runCatching { Url(this) }
    return result.isSuccess
}

fun String.removeWhiteSpace(): String {
    return this.toCharArray().filter { it != ' ' }.joinToString(separator = "")
}

fun String.characterAreSame(that: String): Boolean {
    val input1 = this.removeWhiteSpace().lowercase()
    val input2 = that.removeWhiteSpace().lowercase()
    return input1 == input2
}