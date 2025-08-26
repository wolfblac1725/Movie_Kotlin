package com.erik.canseco.movies.utility

object UtilityFun {
    fun formatYears(date: String): String{
        return date.substring(0,4)
    }

    fun toCategory(categoryType: TypeShared): String {
        return  when (categoryType) {
            is TypeShared.Popular -> Category.POPULAR
            is TypeShared.TopRated -> Category.TOP_RATED
            is TypeShared.NowPlaying -> Category.NOW_PLAYING
        }
    }
}