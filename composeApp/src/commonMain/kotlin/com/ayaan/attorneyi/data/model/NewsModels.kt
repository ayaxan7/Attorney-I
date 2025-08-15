package com.ayaan.attorneyi.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LegalNewsResponse(
    @SerialName("status") val status: String,
    @SerialName("data") val data: LegalNewsData
)

@Serializable
data class LegalNewsData(
    @SerialName("articles") val articles: List<LegalArticle>,
    @SerialName("count") val count: Int
)

@Serializable
data class LegalArticle(
    @SerialName("articleId") val articleId: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String?,
    @SerialName("url") val url: String,
    @SerialName("image") val image: String?,
    @SerialName("publishedAt") val publishedAt: String,
    @SerialName("source") val source: LegalSource,
    @SerialName("tags") val tags: List<String> = emptyList(),
    @SerialName("fetchedAt") val fetchedAt: String? = null
)

@Serializable
data class LegalSource(
    @SerialName("id") val id: String? = null,
    @SerialName("name") val name: String,
    @SerialName("url") val url: String?
)

// Error response model
@Serializable
data class LegalNewsErrorResponse(
    @SerialName("status") val status: String,
    @SerialName("message") val message: String
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

// Legal tag enum for type safety
enum class LegalTag(val value: String) {
    CORPORATE("Corporate"),
    CRIMINAL("Criminal"),
    INTERNATIONAL("International"),
    PRIVACY("Privacy");

    companion object {
        fun fromString(value: String): LegalTag? {
            return entries.find { it.value.equals(value, ignoreCase = true) }
        }
    }
}
