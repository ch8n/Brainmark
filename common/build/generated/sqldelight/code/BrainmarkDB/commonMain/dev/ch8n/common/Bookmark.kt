package dev.ch8n.common

import kotlin.Boolean
import kotlin.Long
import kotlin.String

public data class Bookmark(
  public val id: String,
  public val url: String,
  public val createdAt: Long,
  public val remindAt: Long,
  public val isReviewed: Boolean?,
  public val notes: String?
) {
  public override fun toString(): String = """
  |Bookmark [
  |  id: $id
  |  url: $url
  |  createdAt: $createdAt
  |  remindAt: $remindAt
  |  isReviewed: $isReviewed
  |  notes: $notes
  |]
  """.trimMargin()
}
