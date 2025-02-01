# Security Concerns

## Overall Application
- Overall application variable publicity needs tightened up. Some functions and variables are public when they could be more restricted.
- Getters and Setters on various class variables also deserve greater security (null, validity checks etc.)

## Spotify API
- Spotify API Authentication could be upgraded to <a href="https://developer.spotify.com/documentation/web-api/tutorials/code-pkce-flow">PKCE Authentication Flow</a>. However, the currently implemented API authentication method is 2nd only to this PKCE method and still remains highly secure on its own.
- The Current API Token fetching function makes a perhaps insecure assumption that the parameters passed to it are in fact valid Autherization Codes and States, when this could potentially not be the case.
- The application's authorization scope is currently wider than what it actually uses, meaning that it has more access to Spotify user information than is perhaps necessary. Unless these endpoints are implemented in future, scope permissions should be restricted.
  
## SQLite Database
- Authorization and Refresh tokens are stored as plain text in the database, and should likely be encrypted in some fashion. The tokens expire after 1 hour, and they do not present a huge security risk otherwise, so this is not a big issue overall.
## Misc.
- The user's discord ID is stored as a public static variable in SpotifyAuth in order to fix a program flow issue with authenticating in the browser and returning to discord. This ID is publicly available elsewhere and is a minimal security risk, but this design decision was not one I took pride in.
