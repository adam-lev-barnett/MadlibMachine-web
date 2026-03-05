# Madlib Machine

Madlib Machine is a full-stack web application that:
- ingests a user input string
- blanks a user-defined number of nouns/adjectives/verbs/adverbs
- prompts users to replace those words
- returns a completed madlib with the replacement words
- saves completed madlibs to a database and associates them with authenticated users

[Link to web app](https://madlib-frontend-deploy.vercel.app/)
[Frontend Github](https://github.com/adam-lev-barnett/madlib-frontend)
[Original file-based CLI app](https://github.com/adam-lev-barnett/madlib-machine)

## What is a Madlib?
Madlibs are stories in which some words are replaced with a blank and the removed word's part of speech. The madlibber is prompted to come up with replacement words that match the parts of speech of each removed word. The concept comes from activity books by Leonard Stern and Roger Price, first published in 1958.

### Example
**Original text**: "Greetings, person. I ran to the gym today and chewed some gum. Do you want a baloney sandwich?"
**Blanked text**: "[pluralNoun], [noun]. I [verbPast] to the [noun] and [verbPast] some [noun]. Do you [verb] a [noun]"
**Filled text**: "Potatoes, cowboy. I tested to the space moon and folded some napkins. Do you carry a banana cabbage?"

## Tech stack
**Frontend**: React, TypeScript, Vite, CSS
**Backend**: Java 21, Spring Boot, Stanford CoreNLP
**Database**: PostgreSQL (Supabase)
**Auth**: Google OAuth 2.0 + JWT

## Program / Madlib terminology
- **Madlibifiable word**: any word with a part of speech the program is designed to detect and potentially blank (nouns, adjectives, verbs, adverbs, etc.)
- **Blanked**: a word that is removed from the original text and replaced with a text box representing its part of speech (ie., "[noun]")
- **Fill-in**: replace the blanked words (which are now part of speech blocks) with replacement words
- **Skipper**: a user-defined number that tells the program to skip every *n* madlibifiable words before blanking the next one.

## Features
- Blanks any string up to 10k characters, replacing every *skipper*-th eligible word with a part-of-speech block
- Returns the blanked text and a list of removed parts of speech to prompt user input
- Fills the blanked text with user-supplied replacement words and returns the completed madlib
- Saves all completed madlibs to PostgreSQL; associates them with the logged-in user when authenticated
- Google OAuth 2.0 login — users sign in with their Google account, backend issues a JWT for subsequent requests
- Authenticated users can retrieve only their own saved madlibs
- All completed madlibs (including anonymous) are viewable via an authenticated endpoint
- jUnit testing for madlib blanking and word replacement

## Installation
**Requirements**
- Java 21+
- Maven

### Environment variables
The following environment variables must be set (locally in `application-local.properties`, or in your deployment platform):

| Variable | Description |
|---|---|
| `DB_URL` | JDBC URL for PostgreSQL (`jdbc:postgresql://...`) |
| `DB_USERNAME` | Database username |
| `DB_PASSWORD` | Database password |
| `GOOGLE_CLIENT_ID` | From Google Cloud Console → OAuth 2.0 Credentials |
| `GOOGLE_CLIENT_SECRET` | Same as above |
| `JWT_SECRET` | Any long random string (32+ characters) |
| `FRONTEND_URL` | Frontend origin (e.g. `https://madlib-frontend-deploy.vercel.app`) |

### Running the service
Run locally from the project root:
```bash
mvn spring-boot:run
```

The service will start on port 8080 by default.

## Project structure

```
adam_barnett.madlibs.madlib_machine
├── madlib
│   ├── DTOs                    # Request/response records
│   │   ├── BlankMadlibRequest
│   │   ├── BlankMadlibResponse
│   │   ├── FillMadlibRequest
│   │   ├── FilledMadlibResponse
│   │   └── SavedMadlibResponse
│   └── mvc                     # Controller, service, entity, repository
│       ├── MadlibController
│       ├── MadlibService
│       ├── Madlib              # JPA entity (maps to madlibs table)
│       └── MadlibRepository
│
├── users
│   ├── User                    # JPA entity (maps to users table)
│   └── UserRepository
│
├── security
│   ├── JwtService              # Generates and validates JWTs
│   ├── JwtAuthFilter           # Reads Bearer token on every request
│   ├── OAuth2SuccessHandler    # Post-Google-login: upsert user, issue JWT, redirect
│   └── config
│       ├── SecurityConfig      # Auth rules, CORS, stateless session
│       ├── CorsConfig          # Allowed origins
│       └── OpenApiConfig       # Swagger/OpenAPI with OAuth2 auth support
│
├── madlibgeneration            # Logic for blanking and filling source text
│   ├── MadlibBlanker
│   ├── MadlibFiller
│   ├── PosMap                  # Which parts of speech are madlibifiable
│   ├── WordsToSkip             # Words never blanked regardless of POS
│   └── package-info.java
│
├── tagger                      # Stanford CoreNLP wrapper
│   ├── TextAnnotater
│   ├── TextAnnotationProperties
│   └── package-info.java
│
├── utility
│   └── exceptions              # Custom exceptions
│
└── MadlibMachineApplication    # Spring Boot entry point
```

## API endpoints

| Method | Path | Auth required | Description |
|---|---|---------------|---|
| `POST` | `/madlibs/madlibify` | No            | Submit source text → returns blanked text + POS list |
| `POST` | `/madlibs/fillMadlib` | No            | Submit blanked text + words → returns completed madlib (saved to DB) |
| `GET` | `/madlibs/all` | No            | Returns all saved madlibs (newest first) |
| `GET` | `/madlibs/myMadlibs` | Yes           | Returns only the authenticated user's madlibs |

Authenticated endpoints require an `Authorization: Bearer <token>` header. Obtain a token by completing the Google OAuth2 login flow (`/oauth2/authorization/google`).

### API documentation
[View the Javadoc](https://adam-lev-barnett.github.io/madlib-machine/)

### Updating madlibification rules
- To change which parts of speech are blanked, edit `madlibgeneration/PosMap.java`
- To change which words are never blanked, edit `madlibgeneration/WordsToSkip.java`

## Testing
To run all jUnit tests:
```bash
mvn test
```
### Tests include
- **MadlibBlankerTest**: validates correct madlib blanking
- **MadlibFillerTest**: verifies correct word substitution

## Future improvements
- Maintaining line breaks during madlibification
- Usernames and profiles for registered users
- AI integration for source text prompting if the user doesn't want to supply their own text

## License
This project is licensed under the MIT License
