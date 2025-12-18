# Data-Flow Testing Report: `MovieParser.parseMovies(String)`

## Target

- Class: `com.example.MovieParser`
- Method: `List<Movie> parseMovies(String moviesFileData)`

## Why this is one of the 3 most complex methods

This method combines:

- A `while` loop with manual index management (`i` is incremented in multiple places)
- Multiple guard/exception branches (missing header fields, missing genres line, invalid genres)
- Nested iteration for genre normalization

It has several independent decision points and multiple redefinitions of `i`, which creates many definition-clear paths.

## Control-Flow Diagram (CFG)

```mermaid
flowchart TD
  A([Entry]) --> B[Def: lines = split(moviesFileData)\nDef: i = 0]
  B --> C{P-use: i < lines.length}
  C -- false --> Z([Return movies_list])
  C -- true --> D[Def: line = lines[i]]
  D --> E[Def: titleAndId = line.split(',',2)]
  E --> F{P-use: titleAndId.length < 2}
  F -- true --> G[Def: i = i+1] --> C
  F -- false --> H[Def: title = titleAndId[0].trim()\nDef: id = titleAndId[1].trim()]
  H --> I[Def: i = i+1]
  I --> J{P-use: i >= lines.length}
  J -- true --> K[[Throw MovieException\n"Genres are missing for movie {id}"]]
  J -- false --> L[Def: line = lines[i]\nDef: genresArray = line.split(',')]
  L --> M{P-use: genresArray.length > 1}
  M -- false --> O[Def: genres = new ArrayList()]
  M -- true --> N{P-use: genresArray[1] matches ".*\\d.*"}
  N -- true --> P[[Throw MovieException\n"Genres are invalid for movie {id}"]]
  N -- false --> O
  O --> Q[For each genre in genresArray:\nC-use: genres.add(genre.trim())]
  Q --> R[Def: movie = new Movie(title,id,genres)\nC-use: movies_list.add(movie)]
  R --> S[Def: i = i+1] --> C
```

## Def–Use Table

Legend:

- **Def**: assignment / initialization
- **P-use**: predicate use (in a condition)
- **C-use**: computational use (value used in computation / call / return)

Rows below are _statement-oriented_ (they follow the method’s flow). Variables appear under **Def**, **P-use**, and/or **C-use** as appropriate.

| Step (code shape)                                           | Def            | P-use         | C-use                   |
| ----------------------------------------------------------- | -------------- | ------------- | ----------------------- |
| `lines = moviesFileData.split("\n")`                        | `lines`        |               | `moviesFileData`        |
| `i = 0`                                                     | `i`            |               |                         |
| `while (i < lines.length)`                                  |                | `i`, `lines`  |                         |
| `line = lines[i]`                                           | `line`         |               | `lines`, `i`            |
| `titleAndId = line.split(",", 2)`                           | `titleAndId`   |               | `line`                  |
| `if (titleAndId.length < 2)`                                |                | `titleAndId`  |                         |
| `i++; continue;` (skip bad header)                          | `i` (redef)    |               |                         |
| `title = titleAndId[0].trim()`                              | `title`        |               | `titleAndId`            |
| `id = titleAndId[1].trim()`                                 | `id`           |               | `titleAndId`            |
| `i++` (advance to genres line)                              | `i` (redef)    |               |                         |
| `if (i >= lines.length) throw ...`                          |                | `i`, `lines`  | `id` (in message)       |
| `line = lines[i]` (genres line)                             | `line` (redef) |               | `lines`, `i`            |
| `genresArray = line.split(",")`                             | `genresArray`  |               | `line`                  |
| `if (genresArray.length > 1)`                               |                | `genresArray` |                         |
| `if (genresArray[1].matches(".*\\d.*")) throw ...`          |                | `genresArray` | `id` (in message)       |
| `genres = new ArrayList<>()`                                | `genres`       |               |                         |
| `for (String genre : genresArray) genres.add(genre.trim())` |                |               | `genresArray`, `genres` |
| `movie = new Movie(title, id, genres)`                      | `movie`        |               | `title`, `id`, `genres` |
| `movies_list.add(movie)`                                    |                |               | `movies_list`, `movie`  |
| `i++` (next movie)                                          | `i` (redef)    |               | `i`                     |
| `return movies_list`                                        |                |               | `movies_list`           |

## Proposed Testcases (All-Defs / All-Uses / All-DU-Paths)

These are _black-box_ testcases (input string → expected outcome) that are sufficient to satisfy the data-flow criteria for this method.

### All-Defs (each definition reaches at least one use)

- **AD-1 valid single movie**: `"Matrix,M001\nAction\n"` → returns list size 1.
- **AD-2 skip bad header then parse**: `"BadLine\nThe Matrix,TM001\nAction,SciFi\n"` → returns list size 1.
- **AD-3 multi-movie / i redefinitions**: `"Movie One,MO001\nAction\nMovie Two,MT002\nDrama\n"` → returns list size 2.

### All-Uses (exercise each reachable P-use/C-use)

- **AU-1 title/id split predicate true**: line without comma triggers `titleAndId.length < 2` path.
- **AU-2 missing genres line**: header present but EOF next → throws `MovieException` “Genres are missing…”.
- **AU-3 genres length > 1 but digit check false**: `"The Matrix,TM001\nAction,SciFi\n"` → parses normally.
- **AU-4 digit check true**: `"The Matrix,TM001\nAction,Sc1Fi\n"` → throws `MovieException` “Genres are invalid…”.
- **AU-5 genres length == 1**: `"Matrix,M001\nAction\n"` → parses normally.

### All-DU-Paths (ADUP)

- **ADUP-1 i def→use through loop back-edge**: multi-movie input to force multiple `while` iterations.
- **ADUP-2 i def→use to `i >= lines.length` throw**: header-only input to reach missing-genres throw.
- **ADUP-3 genresArray def→use through both branches**: one test with single genre token (length==1), one with multiple tokens (length>1), and one that throws on digit check.

## Test Mapping (All-Defs / All-Uses / All-DU-Paths)

All coverage for this method lives in:

- `src/test/java/com/example/wbt/data_flow/MovieParserDataFlowTest.java`

Notable paths:

- Skip bad header line (`titleAndId.length < 2`) then successfully parse: `parseMovies_puseTitleAndIdLength_true_skipsLine_thenParsesNext`
- Missing genres line (`i >= lines.length`) throw: `parseMovies_puseMissingGenres_true_throws`
- Invalid genres second token contains digits throw: `parseMovies_puseInvalidGenres_secondTokenHasDigit_throws`
- `genresArray.length > 1` true and digit-regex false: `parseMovies_genresWithMultipleTokens_noDigit`
- Multi-iteration with `i` redefinitions: `parseMovies_iIncrementedTwicePerIteration`
