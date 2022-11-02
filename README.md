<h1 align="center">Falling Words</h1>
 
Falling Words is a small language game. The player will see a word in language one" on the screen. While this word is displayed, a word in language two will fall down on the screen. The player will have to choose if the falling word is the correct or incorrect translation. A timer counts as the word falls to the bottom of the screen, before which the player should have given an answer. The player needs to answer, before the word reaches the bottom of the screen.
The Game is build with modern Android tech-stacks and MVVM architecture. Fetching data from the local json via repository pattern.


## Download
Go to the [Download Link](https://drive.google.com/file/d/1gzzpoDQQhulxT6rxXsAEd2ojfQe0BUKb/view?usp=share_link) to download the latest APK.

## Screenshots
<p align="center">
<img src="/preview/preview.png" width="32%"/>
<img src="/preview/preview1.png" width="32%"/>
<img src="/preview/preview2.png" width="32%"/>
<img src="/preview/preview3.png" width="32%"/>
<img src="/preview/video.gif" width="32%"/>
</p>

## Tech stack & Open-source libraries
- Minimum SDK level 23
- 100% [Kotlin](https://kotlinlang.org/) based + [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous.
- Google Material Design
- Hilt for dependency injection.
- JetPack
  - Lifecycle - dispose observing data when lifecycle state changes.
  - ViewModel - UI related data holder, lifecycle aware.
- Architecture
  - MVVM Architecture (View - ViewModel - Model)
  - Repository pattern

## Architecture
- The app developed using Clean MVVM architecture combined with Repository and Domains pattern to limit coupling between application layers and components which provides a better codebase to be more testable, maintainable and also scalable for new features and updates.

- GameViewModel contains the game logic and stores the data to survive the configuration changes so user can continue the game smoothly. Also fetching words list from the repository using Kotlin Coroutines and flows.

- For now app is created for the portrait mode with Adaptive UI, for that I used SP and DP Libray.

- App Contains Two Fragments
    - StartGameFragment: Which shows the game instructions and Start Game Button
    - FallingGameFragment: Shows the Game UI and Controls.

## Time Distribution

- I've spent about 7-8 hours on the app.
- <b>1 hour</b> for planning the game concept, base architecture with Dagger Hit setup.
- <b>1:30 hour</b> for building views and game UI controls.
- <b>2:30 hour</b> for developing game logic and the data layer.
- <b>1:30 hour</b> for writing tests both unit-testing and UI-testing.
- <b>30 minutes</b> for documentation.
- <b>1 hour</b> for Dev Testing.

## To Improve
Due to time constraints, I wish I could improve the app with the following:

- Introduce different levels of difficulty with easy, medicum and hard to speedup the falling word.
- Creating background service to sync new words from the server to the app’s local database.
- Improve user experience for better interaction by adding sounds and vibration effects, with correction if the chosen word is wrong.
- Add a user dictionary where all the correct words saved, so users can get back to them any time.
- Improve the scrore board screen and show the words with translations that played out during the session so. 
