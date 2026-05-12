package com.erik.canseco.movies.utility.mappers

import com.erik.canseco.movies.data.remote.MovieApi
import com.erik.canseco.movies.data.remote.response.PersonDto
import com.erik.canseco.movies.domain.model.Person

fun PersonDto.toPerson(): Person {
    return Person(
        id = id,
        alsoKnownAs = also_known_as,
        name = name,
        biography = biography,
        birthday = birthday,
        deathday = deathday,
        gender = gender,
        homepage = homepage,
        imdbId = imdb_id,
        placeOfBirth = place_of_birth,
        popularity = popularity,
        profilePath = profile_path
    )
}