# Github Users List
 This is a project to learn more about Android fundamental. This project uses Github API to look up github users.
 
 To ensure the app runs properly, get the **API key** from [**Github**](https://github.com/settings/tokens) first and place it `api_key` variable in gradle.properties (Project Level).
 
 ![image](https://user-images.githubusercontent.com/16620379/162584022-b8a05352-c749-48c4-bfb6-00e5aac44299.png)
 
 ![image](https://user-images.githubusercontent.com/16620379/162584389-af522092-9519-45ec-b633-82971e6ac105.png)
 
 This project is created for **learning purposes only**.
 Feel free to clone this repository to learn.
 
 This project's features are:
 - Looking up github users through search query.
 - Displaying the user detail.
 - Favoriting user and save it into local database.
 - Querying the favorited users.
 
 This project is using:
 - [Shimmer from Facebook](https://facebook.github.io/shimmer-android) for loading animation.
 - [Android Material Library](https://github.com/material-components/material-components-android) for the view.
 - [RxJava2](https://github.com/ReactiveX/RxJava) for asynchronous task.
 - [Retrofit2](https://square.github.io/retrofit) for network request.
 - [GSON](https://github.com/google/gson) for JSON parsing.
 - [Android Jetpack Room](https://developer.android.com/jetpack/androidx/releases/room) for managing local database.
 - [Android Jetpack DataStore](https://developer.android.com/topic/libraries/architecture/datastore) for managing shared preferences.
 - [Glide](https://github.com/bumptech/glide) for image load library.
  
 This project is implementing:
 - MVVM as Design Pattern.
 - Shared ViewModel with the same ViewModel to share data between activity and the fragment in it.
 - Proper arrangement in package to ensure everything is easy to find.
