package com.ramanie.threeddrodown

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DropDown( text: String, modifier: Modifier = Modifier,
              initiallyOpen: Boolean = false, content: @Composable () -> Unit ){
    var isOpen by remember {
        mutableStateOf(initiallyOpen)
    }

    // animateFloatAsState() will animate the alpha value from the old to the new based on the isOpen value
    val alpha = animateFloatAsState(targetValue = if (isOpen) 1f else 0f, animationSpec = tween( durationMillis = 250 ))
    // since we're rotating the dropdown on the x-axis, the open state will be animated to 0 degrees and the hidden state will be -90 tilting it back 90 from the origin
    val rotateX = animateFloatAsState(targetValue = if (isOpen) 0f else -90f, animationSpec = tween( durationMillis = 250 ))
    
    Column(modifier = modifier.fillMaxWidth()) {
        Row(modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = text, color = Color.DarkGray, fontSize = 16.sp)
            Icon(imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Open or close the dropdown",
                tint = Color.DarkGray,
                modifier = Modifier
                    .clickable { isOpen = !isOpen }
                    .scale(
                        scaleX = 1f,
                        // if the dropdown is open then the y should be inversed else it should be in its normal state
                        // NOTE : this could have been done in the imageVector param, by loading a diff image based on the open state
                        // but that'd force us to load another img which is resource heavy
                        scaleY = if (isOpen) -1f else 1f
                    ))
        }
        Spacer(modifier = Modifier.height(10.dp))
        // this is the container for whatever will be shown when the dropdown is open
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth().graphicsLayer {
                // by default the pivot is at the center of the box, so the line below will move it
                // it'll move the pivot to the center of the x-axis and the top of the y-axis
                transformOrigin = TransformOrigin(0.5f, 0f)
                rotationX = rotateX.value
            }.alpha(alpha.value)){
            content()
        }
    }
}