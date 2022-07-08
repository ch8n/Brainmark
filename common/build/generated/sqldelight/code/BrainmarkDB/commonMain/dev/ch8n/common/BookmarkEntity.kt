package dev.ch8n.common

import com.squareup.sqldelight.ColumnAdapter
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List

public data class BookmarkEntity(
  public val id: String,
  public val url: String,
  public val createdAt: Long,
  public val remindAt: Long,
  public val isReviewed: Boolean?,
  public val notes: String?,
  public val tags: List<String>
) {
  public override fun toString(): String = """
  |BookmarkEntity [
  |  id: $id
  |  url: $url
  |  createdAt: $createdAt
  |  remindAt: $remindAt
  |  isReviewed: $isReviewed
  |  notes: $notes
  |  tags: $tags
  |]
  """.trimMargin()

  public class Adapter(
    public val tagsAdapter: ColumnAdapter<List<String>, String>
  )
}
