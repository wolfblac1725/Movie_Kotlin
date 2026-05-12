package com.erik.canseco.movies.presentation.screen.actordetail

import android.R.attr.name
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.erik.canseco.movies.BuildConfig
import com.erik.canseco.movies.R
import com.erik.canseco.movies.data.remote.MovieApi
import com.erik.canseco.movies.domain.model.Person
import com.erik.canseco.movies.presentation.component.Loader
import com.erik.canseco.movies.ui.theme.AppTypography
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DetailPersonalScreenRoot(
    modifier: Modifier = Modifier,
    actorId: Int,
    viewModel: DetailPersonalViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    LaunchedEffect(actorId) {
        viewModel.getPersonal(actorId)
        viewModel.getPersonalMovies(actorId)
    }
    val state = viewModel.state
    DetailPersonalScreen(
        modifier = modifier,
        state = state,
        onAction = { action ->
            when (action) {
                is DetailPersonalAction.Back -> {
                    onBack()
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPersonalScreen(
    modifier: Modifier = Modifier,
    state: DetailPersonalState,
    onAction:(DetailPersonalAction) -> Unit = {}
) {
    val person = state.actor
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(person?.name?:"", fontSize = 20.sp)
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(DetailPersonalAction.Back) }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        content = { padding ->
            Box{
                if(state.isLoading){
                    Loader()
                }
                Column(
                    modifier = Modifier.fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                ) {
                    val imageUrl = BuildConfig.IMAGE_URL + person?.profilePath
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUrl)
                            .crossfade(true)
                            .build(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.5f),
                        contentScale = ContentScale.Fit,
                        contentDescription = person?.name,
                        error = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    modifier = Modifier.size(70.dp),
                                    imageVector = Icons.Rounded.ImageNotSupported,
                                    contentDescription = person?.name,
                                )
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    if(person?.biography != null && person.biography.isNotEmpty()){
                        Text(
                            text = stringResource(R.string.biography),
                            fontSize = 19.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text =  person.biography ,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 16.dp),
                            style = TextStyle.Default.copy(
                                lineBreak = LineBreak.Paragraph,
                                hyphens = Hyphens.Auto
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    state.movies?.let { movies ->
                        Text(
                            text = stringResource(R.string.movies),
                            fontSize = 19.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        DetailPersonMovies(
                            movies = movies,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }

            }


        })


}

@Preview
@Composable
private fun DetailPersonalScreenPreview() {
    DetailPersonalScreen(
        state = DetailPersonalState().copy(
            actor = Person(
                id = 1,
                name = "John Doe",
                biography = "Marlon Brando Jr. (April 3, 1924 – July 1, 2004) was an American actor. Considered one of the most influential actors of the 20th century, he received numerous accolades throughout his career which spanned six decades, including two Academy Awards, two Golden Globe Awards, and three British Academy Film Awards. Brando was also an activist for many causes, notably the civil rights movement and various Native American movements. Having studied with Stella Adler in the 1940s, he is credited with being one of the first actors to bring the Stanislavski system of acting and method acting, derived from the Stanislavski system, to mainstream audiences.\\n\\nHe initially gained acclaim and his first Academy Award nomination for Best Actor in a Leading Role for reprising the role of Stanley Kowalski in the 1951 film adaptation of Tennessee Williams' play A Streetcar Named Desire, a role that he originated successfully on Broadway. He received further praise, and a first Academy Award and Golden Globe Award, for his performance as Terry Malloy in On the Waterfront, and his portrayal of the rebellious motorcycle gang leader Johnny Strabler in The Wild One proved to be a lasting image in popular culture. Brando received Academy Award nominations for playing Emiliano Zapata in Viva Zapata! (1952); Mark Antony in Joseph L. Mankiewicz's 1953 film adaptation of Shakespeare's Julius Caesar; and Air Force Major Lloyd Gruver in Sayonara (1957), an adaptation of James A. Michener's 1954 novel.\\n\\nThe 1960s saw Brando's career take a commercial and critical downturn. He directed and starred in the cult western One-Eyed Jacks, a critical and commercial flop, after which he delivered a series of notable box-office failures, beginning with Mutiny on the Bounty (1962). After ten years of underachieving, he agreed to do a screen test as Vito Corleone in Francis Ford Coppola's The Godfather (1972). He got the part and subsequently won his second Academy Award and Golden Globe Award in a performance critics consider among his greatest. He declined the Academy Award due to alleged mistreatment and misportrayal of Native Americans by Hollywood. The Godfather was one of the most commercially successful films of all time, and alongside his Oscar-nominated performance in Last Tango in Paris (1972), Brando reestablished himself in the ranks of top box-office stars.\\n\\nAfter a hiatus in the early 1970s, Brando was generally content with being a highly paid character actor in supporting roles, such as Jor-El in Superman (1978), as Colonel Kurtz in Apocalypse Now (1979), and Adam Steiffel in The Formula (1980), before taking a nine-year break from film. According to the Guinness Book of World Records, Brando was paid a record \$3.7 million (\$16 million in inflation-adjusted dollars) and 11.75% of the gross profits for 13 days' work on Superman.\\n\\nBrando was ranked by the American Film Institute as the fourth-greatest movie star among male movie stars whose screen debuts occurred in or before 1950. He was one of only six actors named in 1999 by Time magazine in its list of the 100 Most Important People of the Century. In this list, Time also designated Brando as the \\\"Actor of the Century\\\".",
                profilePath = "/profile_path.jpg"
            )
        ),
        onAction = {}
    )
}