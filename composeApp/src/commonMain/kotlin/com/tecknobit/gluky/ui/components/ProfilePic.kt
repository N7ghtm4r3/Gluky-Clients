package com.tecknobit.gluky.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import kotlin.random.Random

/**
 * `imageLoader` the image loader used by coil library to load the image and by-passing the https self-signed certificates
 */
lateinit var imageLoader: ImageLoader

/**
 * Custom [AsyncImage] used to display the profile pic of the [localUser] or the member of a team
 *
 * @param modifier The modifier to apply to the component
 * @param profilePic The data of the profile pic to display
 * @param size The size of the profile pic
 * @param onClick The action to execute when the component has been clicked
 */
@Composable
fun ProfilePic(
    modifier: Modifier = Modifier,
    size: Dp,
    onClick: () -> Unit,
) {
    AsyncImage(
        modifier = modifier
            .clip(CircleShape)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.inversePrimary,
                shape = CircleShape
            )
            .size(size)
            .clickable {
                onClick()
            },
        model = ImageRequest.Builder(LocalPlatformContext.current)
            //.data(localUser.profilePic) TODO: USE THIS INSTEAD
            .data("https://picsum.photos/id/${Random.nextInt(100)}/200/300")
            .crossfade(true)
            .crossfade(500)
            .build(),
        imageLoader = imageLoader,
        contentScale = ContentScale.Crop,
        // TODO: TO SET
        // error = painterResource(Res.drawable.logo),
        contentDescription = "User profile picture"
    )
}