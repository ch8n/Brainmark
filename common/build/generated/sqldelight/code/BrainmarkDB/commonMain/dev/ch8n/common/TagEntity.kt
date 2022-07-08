package dev.ch8n.common

import kotlin.String

public data class TagEntity(
  public val id: String,
  public val name: String
) {
  public override fun toString(): String = """
  |TagEntity [
  |  id: $id
  |  name: $name
  |]
  """.trimMargin()
}
