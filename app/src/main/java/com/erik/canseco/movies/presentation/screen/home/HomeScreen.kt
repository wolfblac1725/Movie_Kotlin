package com.erik.canseco.movies.presentation.screen.home

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.erik.canseco.movies.ads.AdMobBanner
import com.erik.canseco.movies.ads.AdsConsentManager
import com.erik.canseco.movies.R
import com.erik.canseco.movies.ads.InterstitialAdManager
import com.erik.canseco.movies.domain.model.Movie
import com.erik.canseco.movies.presentation.component.BottomNavigationBar
import com.erik.canseco.movies.presentation.component.ListViewComponent
import com.google.android.ump.UserMessagingPlatform
import com.google.android.gms.ads.MobileAds

@Composable
fun HomeScreenRoot(
    modifier: Modifier = Modifier,
    onNavigate: (String) -> Unit
) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val context = LocalContext.current
    val privacyOptionsRequired by AdsConsentManager.privacyOptionsRequired.collectAsState()

    HomeScreen(
        modifier = modifier,
        state = homeViewModel.state,
        movies = homeViewModel.movies.collectAsLazyPagingItems(),
        privacyOptionsRequired = privacyOptionsRequired,
        onOpenPrivacyOptions = {
            (context as? Activity)?.let { activity ->
                UserMessagingPlatform.showPrivacyOptionsForm(activity) { formError ->
                    formError?.let {
                        Log.w("MovieApp", "Privacy options form error: ${it.message}")
                    }
                    val consentInformation =
                        UserMessagingPlatform.getConsentInformation(activity)
                    val canRequestAds = consentInformation.canRequestAds()
                    AdsConsentManager.updateConsent(
                        canRequestAds,
                        consentInformation.privacyOptionsRequirementStatus ==
                            com.google.android.ump.ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED
                    )
                    if (canRequestAds && AdsConsentManager.markSdkInitialized()) {
                        MobileAds.initialize(activity) {
                            InterstitialAdManager.preload(activity)
                        }
                    }
                }
            }
        },
        action = { action ->
            when(action) {
                is ActionHome.DetailMovie -> {
                    onNavigate(action.movie)
                }
                else ->{
                    homeViewModel.onAction(action)
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeState,
    modifier: Modifier = Modifier,
    movies: LazyPagingItems<Movie>,
    privacyOptionsRequired: Boolean,
    onOpenPrivacyOptions: () -> Unit,
    action: (ActionHome) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text("Movie")
                },
                actions = {
                    if (privacyOptionsRequired) {
                        TextButton(onClick = onOpenPrivacyOptions) {
                            Text(stringResource(R.string.privacy_options))
                        }
                    }
                }
            )
        },
        content = { padding ->

            ListViewComponent(
                column = 2,
                movies = movies,
                action = action,
                contentPadding = padding,
                modifier = Modifier
            )
           /* when(state.typeShared){
                TypeShared.Popular -> {
                    if(state.moviesPopular.isLoading)
                        Loader()
                    else if (state.moviesPopular.error != null)
                        Text(text = state.moviesPopular.error)
                    else
                        ListViewComponent(
                            column = 2,
                            movies = state.moviesPopular.movies,
                            action = action,
                            modifier = Modifier.padding(padding)
                        )
                }
                TypeShared.TopRated -> {
                    if(state.moviesTopRated.isLoading)
                        Loader()
                    else if (state.moviesTopRated.error != null)
                        Text(text = state.moviesTopRated.error)
                    else
                        ListViewComponent(
                            column = 2,
                            movies = state.moviesTopRated.movies,
                            action = action,
                            modifier = Modifier.padding(padding)
                        )
                }
                TypeShared.NowPlaying -> {
                    if(state.moviesNowPlaying.isLoading)
                        Loader()
                    else if (state.moviesNowPlaying.error != null)
                        Text(text = state.moviesNowPlaying.error)
                    else
                        ListViewComponent(
                            column = 2,
                            movies = state.moviesNowPlaying.movies,
                            action = action,
                            modifier = Modifier.padding(padding)
                        )
                }
            }*/
        },
        bottomBar = {
            Column {
                BottomNavigationBar(onEvent = action)
                AdMobBanner()
            }
        }
    )
}