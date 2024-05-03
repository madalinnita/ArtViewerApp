# ArtViewer App

Technologies used: 
- MVVM Architecture
- Hilt for dependency injection
- Clean Architecture with use cases
- Coroutines
- Retrofit
- Flow
- For navigation: Jetpack navigation components with Navigation Graphs
- UI: Jetpack Compose
- Room for caching API results (Single source of thruth) used with Paging 3 - RemoteMediator for paging data
- Tests using MockK for ViewModel and RemoteMediator

# How to use
- Preferrably, the device to be in Light mode as night mode can be improved.

# Known possible improvements
- Night mode can be improved in terms of coloring
- Hardcoded texts and dimensions can be migrated to resource folder
- Gradle versioning can be improved with having a separate file for versions and with a better structure
- Internet connection currently shows just a Toast message when Paging cannot load more data, UI can be handled more gracefully in this case. 

Demo video: https://youtube.com/shorts/hQQGcKjdgDw?feature=shared

Project was done using: Android Studio Iguana | 2023.2.1 Patch 2

