package dev.ch8n.common.utils

import androidx.compose.runtime.Composable
import dev.ch8n.common.ui.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlin.coroutines.CoroutineContext

abstract class UiController(
    rootNavController: NavController
) : CoroutineScope,
    NavController by rootNavController {

    private var parentJob = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + parentJob

    @Composable
    abstract fun Render()

    fun cancel() {
        parentJob.cancelChildren()
    }
}