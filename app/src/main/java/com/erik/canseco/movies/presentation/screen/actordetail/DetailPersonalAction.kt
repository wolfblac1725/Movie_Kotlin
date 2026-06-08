package com.erik.canseco.movies.presentation.screen.actordetail

sealed interface DetailPersonalAction {
    data object Back: DetailPersonalAction
}