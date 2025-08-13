package com.ayaan.attorneyi.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    @SerialName("totalArticles") val totalArticles: Int,
    @SerialName("articles") val articles: List<Article>
)

@Serializable
data class Article(
    @SerialName("title") val title: String,
    @SerialName("description") val description: String?,
    @SerialName("url") val url: String,
    @SerialName("image") val image: String?,
    @SerialName("publishedAt") val publishedAt: String,
    @SerialName("source") val source: Source
)

@Serializable
data class Source(
    @SerialName("name") val name: String,
    @SerialName("url") val url: String?
)
