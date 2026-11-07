package com.erik.canseco.movies.presentation.screen.home

import com.erik.canseco.movies.utility.TypeShared

data class HomeState(
    val selectedCategory: TypeShared = TypeShared.TopRated,
)
