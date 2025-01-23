package com.mobile.animauxdomestiques.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter
import com.mobile.animauxdomestiques.R

@Composable
fun ImagePicker(
    imageUri: String?,
    modifier: Modifier
) {
    Image(
        painter = rememberAsyncImagePainter(
            model = imageUri,
            error = painterResource(id = R.drawable.default_animal_image)
        ),
        contentDescription = "Animal Image",
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}
