package dev.ch8n.common.data.model

import com.benasher44.uuid.uuid4

data class Tags(
    val id: String,
    val name: String,
    val color: String,
) {
    companion object {

        val New: Tags
            get() = Tags(
                id = uuid4().toString(),
                name = "",
                color = "#49D643"
            )

        val TAG_KOTLIN: Tags
            get() = Tags(
                id = uuid4().toString(),
                name = "Kotlin",
                color = "#49D643"
            )

        val TAG_JAVA: Tags
            get() = Tags(
                id = uuid4().toString(),
                name = "Java",
                color = "#F53900"
            )
    }
}