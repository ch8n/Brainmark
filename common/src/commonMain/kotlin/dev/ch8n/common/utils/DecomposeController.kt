package dev.ch8n.common.utils

import com.arkivanov.decompose.ComponentContext




abstract class DecomposeController(
    componentContext: ComponentContext
) : ComponentContext by componentContext