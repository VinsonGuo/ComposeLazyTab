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


```kotlin
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
```

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
Box {
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

