package dev.ch8n.common.data.model

import com.benasher44.uuid.uuid4

data class Tags(
    val id: String,
    val name: String
) {
    companion object {
        val EMPTY: Tags
            get() = Tags(
                id = uuid4().toString(),
                name = ""
            )
    }
}