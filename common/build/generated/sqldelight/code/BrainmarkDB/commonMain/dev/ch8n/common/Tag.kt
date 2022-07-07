package dev.ch8n.common

import kotlin.String

public data class Tag(
  public val id: String,
  public val name: String
) {
  public override fun toString(): String = """
  |Tag [
  |  id: $id
  |  name: $name
  |]
  """.trimMargin()
}
