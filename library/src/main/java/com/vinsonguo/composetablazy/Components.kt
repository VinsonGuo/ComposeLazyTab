package com.vinsonguo.composetablazy

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.zIndex

/**
 * A tab container that supports lazy loading of content.
 *
 * @param modifier Modifier for the container
 * @param selectedTabIndex The currently selected tab index
 * @param preloadAdjacent Whether to preload adjacent tabs (defaults to false)
 * @param persistTabs Whether to keep previously loaded tabs in memory (defaults to true)
 * @param tabContents List of composable content for each tab
 */
@Composable
fun LazyTabContainer(
    modifier: Modifier,
    selectedTabIndex: Int,
    preloadAdjacent: Boolean = false,
    persistTabs: Boolean = true,
    tabContents: List<@Composable BoxScope.() -> Unit>,
) {
    // Track which tabs have been loaded
    val loadedTabs = remember { mutableStateMapOf<Int, Boolean>() }
    // Calculate which tabs should be loaded based on current selection and options
    val tabsToLoad by remember(selectedTabIndex, preloadAdjacent) {
        derivedStateOf {
            val result = mutableSetOf(selectedTabIndex)
            // Preload adjacent tabs if enabled
            if (preloadAdjacent) {
                if (selectedTabIndex > 0) result.add(selectedTabIndex - 1)
                if (selectedTabIndex < tabContents.size - 1) result.add(selectedTabIndex + 1)
            }
            result
        }
    }
    // Load required tabs
    LaunchedEffect(tabsToLoad) {
        tabsToLoad.forEach { index ->
            loadedTabs[index] = true
        }
    }
    // Clean up tabs that should be unloaded if not persisting
    if (!persistTabs) {
        LaunchedEffect(tabsToLoad) {
            val indicesToRemove = loadedTabs.keys.filter { it !in tabsToLoad }
            indicesToRemove.forEach { loadedTabs.remove(it) }
        }
    }

    Box(modifier = modifier) {
        tabContents.forEachIndexed { index, content ->
            // Only render tabs that should be loaded
            if (loadedTabs[index] == true) {
                Visibility(selectedTabIndex == index, content = content)
            }
        }
    }
}

/**
 * A utility component that controls the visibility of its content.
 *
 * This component provides a convenient way to show or hide content without removing it from the
 * composition, which is useful for animating visibility changes or implementing tab-based UIs.
 * The invisible content remains in the composition but is made transparent and non-interactive.
 *
 * Features:
 * - Controls visual visibility through alpha transparency
 * - Maintains proper z-index ordering between visible and invisible content
 * - Disables clicks on invisible content while allowing clicks on visible content
 * - Preserves the composable's composition state when toggling visibility
 *
 * @param visible Boolean that controls whether the content is visible (true) or invisible (false)
 * @param content The composable content that will be shown or hidden based on the visibility parameter
 */
@Composable
fun Visibility(visible: Boolean, content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = Modifier
            // Higher z-index for visible content ensures it appears above invisible content
            .zIndex(if (visible) 100f else 0f)
            .fillMaxSize()
            // Attach a transparent, no-ripple clickable modifier that is only enabled when visible
            // This prevents click-through to elements below while keeping the Box interactive
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = visible
            ) {}
            // Control visibility through alpha channel - fully opaque when visible, transparent when not
            .graphicsLayer {
                alpha = if (visible) 1f else 0f
            },
        content = content
    )
}