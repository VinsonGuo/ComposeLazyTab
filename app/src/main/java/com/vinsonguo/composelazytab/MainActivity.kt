package com.vinsonguo.composelazytab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vinsonguo.composelazytab.ui.theme.ComposeLazyTabTheme
import com.vinsonguo.composetablazy.LazyTabContainer
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeLazyTabTheme {
                LazyComposeSample()
            }
        }
    }
}

@Composable
fun LazyComposeSample() {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabItems = listOf(
        TabItem("Home", Icons.Default.Home),
        TabItem("Profile", Icons.Default.Person),
        TabItem("Settings", Icons.Default.Settings)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index }
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyTabContainer(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            selectedTabIndex = selectedTabIndex,
            persistTabs = true, // Keep tabs in memory after loading
            preloadAdjacent = false, // Not to preload adjacent tabs
            tabContents = listOf(
                { HomeTabContent() },
                { ProfileTabContent() },
                { SettingsTabContent() }
            )
        )
    }
}

@Composable
fun HomeTabContent() {
    LoadingContent(
        color = Color(0xFF81C784),
        title = "Home Tab"
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(List(1000) { "Home Item $it" }) { setting ->
                Text(
                    text = setting,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun ProfileTabContent() {
    LoadingContent(
        color = Color(0xFF64B5F6),
        title = "Profile Tab",
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(List(1000) { "Profile Item $it" }) { setting ->
                Text(
                    text = setting,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun SettingsTabContent() {
    LoadingContent(
        color = Color(0xFFFFB74D),
        title = "Settings Tab",
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(List(1000) { "Setting Item $it" }) { setting ->
                Text(
                    text = setting,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

/**
 * A wrapper component that shows loading indicator on first load
 * and then displays the content afterwards.
 */
@Composable
fun LoadingContent(
    color: Color,
    title: String,
    loadingTime: Long = 2000, // Simulated loading time in milliseconds
    content: @Composable () -> Unit,
) {
    var isLoading by remember { mutableStateOf(true) }
    // Load content only once
    LaunchedEffect(Unit) {
        delay(loadingTime)
        isLoading = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color.copy(alpha = 0.2f))
    ) {
        // Show loading on first load
        if (isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Loading $title...",
                    fontSize = 18.sp,
                    color = color.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator(color = color)
            }
        } else {
            // Show actual content after loading
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Top bar showing tab name
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }
                // Tab content
                Box(modifier = Modifier.weight(1f)) {
                    content()
                }
                // Indicator showing this content was loaded once
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.05f))
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✓ Content loaded only once",
                        color = color.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

data class TabItem(
    val title: String,
    val icon: ImageVector,
)

@Preview(showBackground = true)
@Composable
fun LazyComposeSamplePreview() {
    ComposeLazyTabTheme {
        LazyComposeSample()
    }
}