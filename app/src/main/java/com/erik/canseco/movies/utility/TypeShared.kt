package com.erik.canseco.movies.utility

sealed class TypeShared {
    data object Popular: TypeShared()
    data object TopRated: TypeShared()
    data object NowPlaying: TypeShared()

}