# Madlib Machine

Madlib Machine is a full-stack web application that 
- ingests a user input string
- blanks a user-defined number of nouns/adjectives/verbs/adverbs
- prompts users to replace those words
- and returns a completed madlib with the replacement words.  

[Link to web app](https://madlib-frontend-deploy.vercel.app/)  
[Frontend Github](https://github.com/adam-lev-barnett/madlib-frontend)  
[Original file-based CLI app](https://github.com/adam-lev-barnett/madlib-machine)

## What is a Madlib?
Madlibs are stories in which some words are replaced with a blank and the removed word's part of speech. The madlibber is prompted to come up with replacement words that match the parts of speech of each removed word. The concept comes from activity books by Leonard Stern and Roger Price, first published in 1958.

### Example
**Original text**: "Greetings, person. I ran to the gym today and chewed some gum. Do you want a baloney sandwich?"  
**Blanked text**: "[pluralNoun], [noun]. I [verbPast] to the [noun] [noun] and [verbPast] some [noun]. Do you [verb] a [noun] [noun]"  
**Filled text**: "Potatoes, cowboy. I tested to the space moon and folded some napkins. Do you carry a banana cabbage?"

## Tech stack
**Frontend**: React, TypeScript, Vite, CSS  
**Backend**: Java, Spring Boot, Stanford CoreNLP

## Program / Madlib terminology
- **Madlibifiable word**: any word with a part of speech the program is designed to detect and potentially blank (nouns, adjectives, verbs, adverbs, etc.)
- **Blanked**: a word that is removed from the original text and replaced with a text box representing it's part of speech (ie., "[noun]")
- **Fill-in**: replace the blanked words (which are now part of speech blocks) with replacement words
- **Skipper**: a user-defined number that tells the program to skip every *n* madlibifiable words before blanking the next one.

## Features
- Creates a blanked madlib file from any string 10k characters or less, blanking every *skipper*-th word
- Prompts user to replace removed words by providing each removed word's part of speech and accepting user's response as the replacement word
- Produces filled-in madlib
- Collections in Madlib.java and MadlibBlanker.java to determine blankable parts of speech (like gerunds) and words that should never be blanked (verbs like "is")
- jUnit testing for madlib blanking and word replacement

## Installation
**Requirements**
-Java 17+
-Maven

### Running the Service
Run Locally from the project root:  
``bash
mvn spring-boot:run
``

The service will start on port 8080 by default.
## Project structure

```bash
adam_barnett.madlibs.madlib_machine
├── madlib # DTOs for payloads, service and controller layers
│   ├── BlankMadlibRequest
│   ├── BlankMadlibResponse
│   ├── FilledMadlibResponse
│   ├── FillMadlibRequest
│   ├── MadlibController
│   └── MadlibService
│
├── madlibgeneration # Logic for blanking and filling of submitted source text
│   ├── MadlibBlanker # Also contains map of words to explicitly not madlibify
│   ├── MadlibFiller
│   ├── PosMap # Determines which parts of speech to include in madlibification
│   └── package-info.java
│
├── tagger # Core NLP utilities including pipeline for string annotation and retrieving parts of speech
│   ├── TextAnnotater
│   ├── TextAnnotationProperties
│   └── package-info.java
│
├── utility # Custom exceptions
│   └── exceptions
│
├── CorsConfig
└── MadlibMachineApplication # Spring Boot's entry point for app initialization
```
### API documentation
[View the Javadoc](https://adam-lev-barnett.github.io/madlib-machine/)

### Updating madlibification rules
- To update the parts of speech that are parsed and possibly blanked, uncomment/comment the posMap in madlibgeneration/PosMap.java
- To update the words that should never be blanked, regardless of part of speech, uncomment/comment the wordsToSkip map in madlibgeneration/MadlibBlanker.java

## Testing
To run all jUnit tests:
```bash
mvn test
```
### Tests include
- **MadlibBlankerTest**: validates correct madlib blanking and file generation
- **MadlibFillerTest**: verifies correct word substitution and file generation

## Future improvements
- Maintaining line breaks during madlibification
- Upload already-blanked madlib and skip the initial blanking stage
- Store Madlib object, containing original, blanked, and filled-in texts as json

## License
This project is licensed under the MIT License
