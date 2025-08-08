package com.erik.canseco.movies.presentation.screen.moviedetail

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.erik.canseco.movies.R
import com.erik.canseco.movies.domain.model.Movie
import com.erik.canseco.movies.domain.util.RatingBar
import com.erik.canseco.movies.movielist.data.remote.MovieApi
import com.erik.canseco.movies.ui.theme.MoviesTheme

@Composable
fun DetailMovieScreenRoot(
    viewModel: DetailMovieViewModel = hiltViewModel(),
    movie: Movie,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    viewModel.setMovie(movie)
    viewModel.getCast(movie.id)
    val state = viewModel.state
    DetailMovieScreen(
        state = state,
        modifier = modifier,
        onAction = { action ->
            when (action) {
                is DetailMovieAction.Back -> {
                    onBack()
                }
                else -> {

                }
            }
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailMovieScreen(
    state: DetailState,
    modifier: Modifier = Modifier,
    onAction: (DetailMovieAction) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(state.movie?.title ?: "", fontSize = 20.sp,)
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(DetailMovieAction.Back) }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        content = { padding ->
            Column(modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(MovieApi.IMAGE_URL + state.movie?.backdrop_path)
                        .crossfade(true)
                        .build(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    contentScale = ContentScale.Crop,
                    contentDescription = state.movie?.title,
                    error = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier.size(70.dp),
                                imageVector = Icons.Rounded.ImageNotSupported,
                                contentDescription = state.movie?.title,
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ){
                    Box(
                        modifier = Modifier
                            .width(160.dp)
                            .height(240.dp)
                    ) {
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(MovieApi.IMAGE_URL + state.movie?.poster_path)
                                .crossfade(true)
                                .build(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop,
                            contentDescription = state.movie?.title,
                            error = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                        .background(MaterialTheme.colorScheme.primaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        modifier = Modifier.size(70.dp),
                                        imageVector = Icons.Rounded.ImageNotSupported,
                                        contentDescription = state.movie?.title,
                                    )
                                }
                            }
                        )
                    }
                    state.movie?.let { movie ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp)
                        ){
                            Text(
                                text = movie.title,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Row (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RatingBar(
                                    starsModifier = Modifier.size(18.dp),
                                    rating = movie.vote_average / 2
                                )
                                Text(
                                    modifier= Modifier.padding(start = 4.dp),
                                    text = movie.vote_average.toString().take(3),
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    maxLines = 1
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = stringResource(R.string.language) +movie.original_language,
                                fontSize = 17.sp,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = stringResource(R.string.release_date) +movie.release_date,
                                fontSize = 17.sp,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = movie.vote_count.toString() + " " + stringResource(R.string.votes),
                                fontSize = 17.sp,
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = "overview:",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                    text = state.movie?.overview ?: "",
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = "cast:",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(16.dp))
                DetailCastItem(state.cast)
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    )
}


@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun DetailMovieScreenPreview() {
    MoviesTheme {
        val sampleMovie = Movie(
            adult = false,
            backdrop_path = "/sample_backdrop.jpg",
            genre_ids = listOf(28, 12),
            id = 1,
            original_language = "en",
            original_title = "Sample Movie Title Preview",
            overview = "This is a sample overview for the movie. It's a great film with action and adventure.",
            popularity = 7.5,
            poster_path = "/hZkgoQYus5vegHoetLkJoBv9oA.jpg", // Un poster_path real para que Coil lo cargue si es accesible
            release_date = "2023-01-01",
            title = "Sample Movie Title Preview",
            video = false,
            vote_average = 8.2,
            vote_count = 1500,
            category = "popular"
        )
        DetailMovieScreen(
            state = DetailState(movie = sampleMovie),
            modifier = Modifier,
            onAction = {}
        )
    }
}
