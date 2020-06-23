# Kueue

Kueue is an android app that lets you add tracks directly to the Spotify Queue.

## Installation

Fire up a terminal and type the following command :
```
git clone https://github.com/pramodbhadana/Kueue.git
```
or 
```
Open Android Studio 

Go to File --> New --> Project from Version Control --> Github 
 
Enter https://github.com/pramodbhadana/Kueue.git into the Git Repository URL field, follow other instructions and you are good to go.
```

### Configuring project to use API keys

1. Register an app and generate a client ID at https://developer.spotify.com/dashboard/login

2. Add an empty text file : /app/src/main/assets/privateKeys.txt

Add your secret API keys here. The client ID generated in step one goes here : 

```
spotify_client_id = your_spotify_client_id
```
----------
