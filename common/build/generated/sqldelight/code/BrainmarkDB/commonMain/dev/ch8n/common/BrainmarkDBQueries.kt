package dev.ch8n.common

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.Unit
import kotlin.collections.List

public interface BrainmarkDBQueries : Transacter {
  public fun <T : Any> getAllTags(mapper: (id: String, name: String) -> T): Query<T>

  public fun getAllTags(): Query<TagEntity>

  public fun <T : Any> getTagById(id: String, mapper: (id: String, name: String) -> T): Query<T>

  public fun getTagById(id: String): Query<TagEntity>

  public fun <T : Any> getBookmarks(mapper: (
    id: String,
    url: String,
    createdAt: Long,
    remindAt: Long,
    isReviewed: Boolean?,
    notes: String?,
    tags: List<String>
  ) -> T): Query<T>

  public fun getBookmarks(): Query<BookmarkEntity>

  public fun <T : Any> getBookmarksById(id: String, mapper: (
    id: String,
    url: String,
    createdAt: Long,
    remindAt: Long,
    isReviewed: Boolean?,
    notes: String?,
    tags: List<String>
  ) -> T): Query<T>

  public fun getBookmarksById(id: String): Query<BookmarkEntity>

  public fun upsertTag(id: String, name: String): Unit

  public fun deleteTag(id: String): Unit

  public fun upsertBookmark(
    id: String,
    url: String,
    createdAt: Long,
    remindAt: Long,
    isReviewed: Boolean?,
    notes: String?,
    tags: List<String>
  ): Unit

  public fun deleteBookmark(id: String): Unit
}
