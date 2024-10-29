package com.erik.canseco.movies.core.component

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.Image
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.toBitmap
import com.erik.canseco.movies.R
import com.erik.canseco.movies.movielist.data.remote.MovieApi
import com.erik.canseco.movies.movielist.domain.model.Movie
import com.erik.canseco.movies.movielist.domain.util.RatingBar
import com.erik.canseco.movies.movielist.domain.util.Screen
import com.erik.canseco.movies.movielist.domain.util.getAverageColor


@Composable
fun MovieItem (
    movie: Movie,
    navHostController: NavHostController
) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .width(200.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.secondaryContainer,
                        Color.LightGray
                    )
                )
            )
            .clickable {
                navHostController.navigate(Screen.Details.route + "/${movie.id}")
            }
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(MovieApi.IMAGE_URL + movie.poster_path)
                .build(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .height(250.dp)
                .clip(RoundedCornerShape(22.dp)),
            contentScale = ContentScale.Crop,
            contentDescription = movie.title,
            error = {
                Icon(
                    modifier = Modifier.size(70.dp),
                    imageVector = Icons.Rounded.ImageNotSupported,
                    contentDescription = movie.title,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            modifier= Modifier.padding(start = 26.dp, end = 8.dp),
            text = movie.title,
            fontSize = 15.sp,
            color = Color.White,
            maxLines = 1
        )
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, bottom = 12.dp, top = 4.dp),
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
                color = Color.White,
                maxLines = 1
            )
        }
    }
}