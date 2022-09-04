package dev.ch8n.common.utils

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlin.coroutines.CoroutineContext


abstract class DecomposeController(
    componentContext: ComponentContext
) : ComponentContext by componentContext, CoroutineScope {
    private var parentJob = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + parentJob

    fun cancel() {
        parentJob.cancelChildren()
    }
}
