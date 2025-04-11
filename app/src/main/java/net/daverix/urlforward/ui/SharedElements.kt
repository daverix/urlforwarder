package net.daverix.urlforward.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.compositionLocalOf

@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope> {
    error("SharedTransitionScope not set")
}

val LocalAnimationScope = compositionLocalOf<AnimatedVisibilityScope> {
    error("AnimatedVisibilityScope not set")
}
