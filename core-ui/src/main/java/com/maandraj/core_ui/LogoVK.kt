package com.maandraj.core_ui

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maandraj.core_ui.samples.CircleImageView


@Composable
fun LogoVK(visible : Boolean = true,size: Int = 128) {
    if(visible)
        CircleImageView(
            imageId = R.drawable.ic_vk_logo,
            modifier = Modifier
                .size(size.dp),
            contentDescription = "VK logo"
        )
}