package com.erik.canseco.movies.data.remote.response

data class CombinedCreditPersonDto(
    val cast: List<CastCreditPersonDto> = emptyList(),
    val crew: List<CrewCreditPersonDto> = emptyList(),
    val id: Int = 0
)
data class CastCreditPersonDto(
    val adult: Boolean = false,
    val backdrop_path: String? = "",
    val character: String = "",
    val credit_id: String = "",
    val episode_count: Int = 0,
    val first_air_date: String = "",
    val genre_ids: List<Int> = emptyList(),
    val id: Int = 0,
    val media_type: String = "",
    val name: String = "",
    val order: Int = 0,
    val origin_country: List<String> = emptyList(),
    val original_language: String = "",
    val original_name: String = "",
    val original_title: String = "",
    val overview: String = "",
    val popularity: Double = 0.0,
    val poster_path: String? = "",
    val release_date: String = "",
    val title: String = "",
    val video: Boolean = false,
    val vote_average: Double = 0.0,
    val vote_count: Int = 0
)

data class CrewCreditPersonDto(
    val adult: Boolean = false,
    val backdrop_path: String? = "",
    val credit_id: String = "",
    val department: String = "",
    val episode_count: Int = 0,
    val first_air_date: String = "",
    val genre_ids: List<Int> = emptyList(),
    val id: Int = 0,
    val job: String = "",
    val media_type: String = "",
    val name: String = "",
    val origin_country: List<String> = emptyList(),
    val original_language: String = "",
    val original_name: String = "",
    val original_title: String = "",
    val overview: String = "",
    val popularity: Double = 0.0,
    val poster_path: String? = "",
    val release_date: String = "",
    val title: String = "",
    val video: Boolean = false,
    val vote_average: Double = 0.0,
    val vote_count: Int = 0
)