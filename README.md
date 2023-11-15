# Content Listing

## [Introduction](#introduction)
This project shows various Romantic Comedy movies. The user can do below tasks.
1. View the movie poster and the title of the movie.
2. Search for the movie by title (with min 3 chars search text).
3. Scroll through to view more movies with pagination support.

## [Description](#description)
This project follows MVVM pattern with view binding. The project is written in Kotlin 
and uses Android Architecture Components. The project fetches mock response given with
the project requirement and displays the movies in a grid manner with the help of recycler 
view. The project uses View Model to fetch the images from the json response which is stored
in the assets folder. The project uses drawable folder to store the images and use them based
on the poster name given in the response for each content.

### GridView (Recycler View):
Grid Layout Manager is passed to recycler views layout manager to show the content in a grid,
used configuration change method of Android activity (made changes in AndroidManifest.xml) to
receive onConfigurationChange. In Portrait mode, a span count is kept as 3 and for landscape mode
span count is kept as 7 as per the requirement. 

### Pagination:
Pagination support is given using NestedScrollView on top of Recycler View. Whenever the current
set of content is shown to the user with the grid view (Recycler view), a new page response is fetched,
and shown to the user. As per the requirement as of now only 3 pages are supported.

### Search:
Search support is given using EditText and TextWatcher. Whenever the user enters a search text,
an observable field of search text is passed to movie adapter which is holding view and data for the
grid view to show the searched matching text in YELLOW color. The search text is used on the current
list of content name to filter out the matching content name and same matched content only is shown 
to the user.

In case, if the search text is not matched with any of the content name, a empty screen with message
is shown to the user.

In case, if the search text is less than 3 characters, a snackbar is shown to the user to enter search
text more than 3 chars.

### View binding:
Used view binding to bind the views in the layout xml to the activity. This helps in referring the
views in the activity without using findViewById.

### Back button handling:
Standard Back button handling is done using onBackPressed() method of activity. Whenever the user presses 
back button on title bar or Android bottom bar back button, the user is shown with toast message to
confirm app exit with one more time back press.

### Screenshot:
Added screenshots of the app in the screenshots folder.

## [Testing](#testing)
The project uses JUnit4 for Instrumentation testing. Instrumentation tests have been added for
MainActivity & ContentListViewModel. Unit tests have been added for ImageUtils which provides
the image resId given the image name from the content response.

## [Coding](#coding)
The code is written in Kotlin.
IDE used - Android Studio (Giraffe)
Android Studio Version: (Giraffe) 2022.3.1 Patch 3
JRE - 17
AGP (Android Gradle Plugin): 8.1.0

### Attached Screenshot:
![Alt text](/Screenshots/Screenshot_20231114_234409_ContentListing.jpg?raw=true "App Image 1")
![Alt text](/Screenshots/Screenshot_20231114_234438_ContentListing.jpg?raw=true "App Image 2")
![Alt text](/Screenshots/Screenshot_20231114_234608_ContentListing.jpg?raw=true "App Image 3")
