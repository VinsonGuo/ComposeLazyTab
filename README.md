# LazyCompose

LazyCompose is a lightweight library that provides lazy loading functionality for Jetpack Compose UI components, with a special focus on tab-based interfaces.

## Features

- 🚀 **Lazy Loading**: Load content only when it's needed, improving initial load time and memory usage
- 🔄 **Preloading**: Optional preloading of adjacent tabs for smoother user experience
- 💾 **Tab Persistence**: Configurable tab persistence to keep loaded tabs in memory
- 🎯 **Efficient Rendering**: Only renders tabs that have been loaded
- 👁️ **Visibility Control**: Dedicated `Visibility` component for clean tab visibility management
- ⚙️ **Customizable**: Easily integrate with your existing Compose UI

## Installation

Add the JitPack repository to your build file:

```gradle
allprojects {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency:

```gradle
dependencies {
    implementation 'com.github.VinsonGuo:ComposeLazyTab:1.0.0'
}
```

## Usage

### Basic Usage

```kotlin
// In your composable
var selectedTabIndex by remember { mutableStateOf(0) }

LazyTabContainer(
    selectedTabIndex = selectedTabIndex,
    tabContents = listOf(
        { HomeScreen() },
        { ProfileScreen() },
        { SettingsScreen() }
    )
)
```

### With Preloading & Persistence Options

```kotlin
LazyTabContainer(
    selectedTabIndex = selectedTabIndex,
    preloadAdjacent = true,  // Preload adjacent tabs for smoother transitions
    persistTabs = false,     // Unload tabs when not visible to save memory
    tabContents = listOf(
        { HomeScreen() },
        { ProfileScreen() },
        { SettingsScreen() }
    )
)
```

### Using with Bottom Navigation

```kotlin
Scaffold(
    bottomBar = {
        BottomNavigation {
            BottomNavigationItem(
                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                label = { Text("Home") },
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 }
            )
            // Other navigation items...
        }
    }
) { paddingValues ->
    LazyTabContainer(
        selectedTabIndex = selectedTabIndex,
        paddingValues = paddingValues,
        tabContents = listOf(
            { HomeScreen() },
            { ProfileScreen() },
            { SettingsScreen() }
        )
    )
}
```

## Sample App

Check out the [sample app](app/) to see LazyCompose in action. The sample app demonstrates lazy loading behavior with simulated loading times for each tab.


### Using the Visibility Component

The `Visibility` component is a helpful utility for controlling the visibility of content:

```kotlin
// Show or hide content based on a condition
Visibility(
    visible = isContentVisible,
    content = {
        // Your content here
        Text("This content can be shown or hidden")
    }
)

// Perfect for use in custom tab implementations
Row {
    tabs.forEachIndexed { index, tab ->
        Visibility(
            visible = selectedTabIndex == index,
            content = { 
                tab.content() 
            }
        )
    }
}
```

