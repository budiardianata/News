# News App
This is simple News app built to demonstrate use of Modern Android development tools.
All data is fetched from [News API](https://newsapi.org/).

## Build Instructions
1. Clone the repository
2. Create a file `local.properties` in the root directory of the project and add the following line:

    `NEWS_API_KEY = YOUR_API_KEY`
    
    *Note: Replace `YOUR_API_KEY` with your API key from [News API](https://newsapi.org/)*

3. Build the project

## Architecture
This app uses [***MVVM (Model View View-Model)***](https://developer.android.com/jetpack/docs/guide#recommended-app-arch) architecture.

## Tech Stack
- [Kotlin](https://kotlinlang.org/) - First class and official programming language for Android development.
- [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - For asynchronous and more..
- [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-flow/) - A cold asynchronous data stream that sequentially emits values and completes normally or with an exception.
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - Collection of libraries that help you design robust, testable, and maintainable apps.
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Stores UI-related data that isn't destroyed on UI changes. 
  - [ViewBinding](https://developer.android.com/topic/libraries/view-binding) - Generates a binding class for each XML layout file present in that module and allows you to more easily write code that interacts with views.
  - [Room](https://developer.android.com/topic/libraries/architecture/room) - SQLite object mapping library.
  - [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) - Android Paging library to load and display pages of data from a larger Dataset.
- [Dependency Injection](https://developer.android.com/training/dependency-injection) - 
  - [Hilt-Dagger](https://dagger.dev/hilt/) - Standard library to incorporate Dagger dependency injection into an Android application.
- [Retrofit](https://square.github.io/retrofit/) - A type-safe HTTP client for Android and Java.
- [Glide](https://coil-kt.github.io/coil/) - An image loading library for Android.
- [Material Components for Android](https://github.com/material-components/material-components-android) - Modular and customizable Material Design UI components for Android.
- [mockk](https://mockk.io/), [Espresso](https://developer.android.com/training/testing/espresso) And More...

## Package Structure
    
    com.test.news           # Root Package
    .
    ├── data                # For data handling including repository Impl
    │   ├── mapper          # model class mapper
    │   └── source          # Remote Data Handlers
    |       ├── local       # Local Persistence Database. Room (SQLite) database
    |       └── remote      # Remote Data Handlers (Retrofit) 
    |
    ├── di                  # Dependency Injection
    |
    ├── domain              
    |   ├── model           # Model classes
    |   └── repositories    # Repositories Interface
    |
    ├── ui                  # Activity/View layer
    │   ├── list            # List Fragment and ViewModel
    │   └── detail          # Detail Screen Fragment
    |
    └── util                # Utility Classes / Kotlin extensions

