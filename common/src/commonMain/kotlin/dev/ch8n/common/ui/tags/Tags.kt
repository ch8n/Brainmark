package dev.ch8n.common.ui.tags




interface TagsPresenter {
    fun createTag(tagName: String)
    fun deleteTag(tagName: String)
    fun updateTag(tagName: String)
}

