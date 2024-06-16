# Dazn Take-Home Coding Challenge Solution<br/><br/>

### This document describes how to set up and run the project:
<br/>
1. Clone or download the sources from this repository to any convenient location.<br/><br/>
2. Open Android Studio (Jellyfish is recommended but not mandatory).<br/><br/>
3. In the Android Studio (AS) welcome screen, press Open and choose the folder from Step 1.<br/><br/>
4. When the Gradle sync is finished, you will be able to run the application either on a simulator or on a real device.<br/><br/><br/>

### Host url configuration
Api endpoint is placed in build.gradle of app level. Different environments (development, staging, production) often require different API endpoints. By defining API host in build.gradle, we can easily switch between these environments without changing the source code.<br/><br/><br/>

### Unit Tests
The unit tests can be found under the "com.kurshin.daznchallenge" package marked as "(test)".<br/><br/>
Feel free to run the whole package with coverage in AS and enjoy the numbers.<br/><br/><br/>

### Bonus
The app fully supports screen rotation, ensuring a seamless user experience. When the screen is rotated or data is updated (such as on the schedule screen), the current scroll position is maintained, preventing any loss of user progress. Additionally, the video playback state is preserved during screen rotation, allowing the video to continue from where it left off.<br/><br/><br/>

I wish you enjoy the app and the code. Happy reviewing! 😎
