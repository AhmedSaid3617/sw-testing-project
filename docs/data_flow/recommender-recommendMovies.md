# Data-Flow Testing Report: `Recommender.recommendMovies(User)`

## Target

- Class: `com.example.Recommender`
- Method: `List<Movie> recommendMovies(User user)`

## Why this is one of the 3 most complex methods

This method is dominated by nested loops and multiple predicate-controlled branches:

- Builds a derived list (`likedMovies`) from IDs
- Builds a de-duplicated set-like list (`likedGenres`) with a `contains()` predicate
- Iterates all movies and performs a nested genre match with early-exit (`break`) and skip (`continue`)

That combination creates many def-clear paths for the main data structures and loop predicates.

## Control-Flow Diagram (CFG)

```mermaid
flowchart TD
  A([Entry]) --> B[Def: likedMoviesIDs = user.getLikedMovies()]
  B --> C[Def: likedMovies = new ArrayList()]
  C --> D{for each id in likedMoviesIDs}
  D -- iter --> E[C-use: data.getMovieById(id), C-use: likedMovies.add(movie)]
  E --> D
  D -- done --> F[Def: likedGenres = new ArrayList()]
  F --> G{for each movie in likedMovies}
  G -- iter --> H[Def: genres = movie.getGenres()]
  H --> I{for each genre in genres}
  I -- iter --> J{P-use: !likedGenres.contains(genre)}
  J -- true --> K[C-use: likedGenres.add(genre)] --> I
  J -- false --> I
  I -- done --> G
  G -- done --> L[Def: recommendations = new ArrayList()]
  L --> M{for each movie in data.getMovies()}
  M -- iter --> N{P-use: likedMovies.contains(movie)}
  N -- true --> M
  N -- false --> O{for each genre in movie.getGenres()}
  O -- iter --> P{P-use: likedGenres.contains(genre)}
  P -- true --> Q[C-use: recommendations.add(movie)\nC-use: break] --> M
  P -- false --> O
  O -- done --> M
  M -- done --> Z([Return recommendations])
```

## Def–Use Table

Legend:

- **Def**: assignment / initialization
- **P-use**: predicate use
- **C-use**: computational use

Rows below are _statement-oriented_ (they follow the method’s flow). Variables appear under **Def**, **P-use**, and/or **C-use** as appropriate.

| Step (code shape)                                                         | Def               | P-use                  | C-use                       |
| ------------------------------------------------------------------------- | ----------------- | ---------------------- | --------------------------- |
| `likedMoviesIDs = user.getLikedMovies()`                                  | `likedMoviesIDs`  |                        | `user`                      |
| `likedMovies = new ArrayList<>()`                                         | `likedMovies`     |                        |                             |
| `for (String id : likedMoviesIDs)`                                        | `id`              |                        | `likedMoviesIDs`            |
| `likedMovies.add(data.getMovieById(id))`                                  |                   |                        | `likedMovies`, `data`, `id` |
| `likedGenres = new ArrayList<>()`                                         | `likedGenres`     |                        |                             |
| `for (Movie movie : likedMovies)`                                         | `movie`           |                        | `likedMovies`               |
| `genres = movie.getGenres()`                                              | `genres`          |                        | `movie`                     |
| `for (String genre : genres)`                                             | `genre`           |                        | `genres`                    |
| `if (!likedGenres.contains(genre)) likedGenres.add(genre)`                |                   | `likedGenres`, `genre` | `likedGenres`, `genre`      |
| `recommendations = new ArrayList<>()`                                     | `recommendations` |                        |                             |
| `for (Movie movie : data.getMovies())`                                    | `movie` (redef)   |                        | `data`                      |
| `if (likedMovies.contains(movie)) continue`                               |                   | `likedMovies`, `movie` |                             |
| `for (String genre : movie.getGenres())`                                  | `genre` (redef)   |                        | `movie`                     |
| `if (likedGenres.contains(genre)) { recommendations.add(movie); break; }` |                   | `likedGenres`, `genre` | `recommendations`, `movie`  |
| `return recommendations`                                                  |                   |                        | `recommendations`           |

## Proposed Testcases (All-Defs / All-Uses / All-DU-Paths)

These are _behavioral_ testcases (store contents + user liked IDs → expected recommendations) that are sufficient to satisfy the data-flow criteria.

### All-Defs (each definition reaches at least one use)

- **AD-1 empty liked IDs**: user liked `[]`, store has movies → returns `[]`.
- **AD-2 one liked ID and one candidate match**: store has liked movie + one genre-matching movie → returns `[match]`.

### All-Uses (exercise each reachable P-use/C-use)

- **AU-1 unique genre add path**: liked movie has repeated genre tokens; ensures `!likedGenres.contains(genre)` is both true and false during liked-genres construction.
- **AU-2 skip liked movie**: store iteration hits a liked movie → exercises `continue`.
- **AU-3 no genre matches**: liked genres don’t match any candidate → exercises inner predicate always false.
- **AU-4 first-genre match vs later-genre match**: candidate has multiple genres where match occurs at index 0 in one test and at a later index in another.

### All-DU-Paths (ADUP)

- **ADUP-1 likedMoviesIDs def→use with 0 and >0 iterations**: one test with empty liked IDs and one with at least 2 liked IDs.
- **ADUP-2 likedGenres def→use across both sites**: ensure `likedGenres` is used in both the “build unique genres” predicate and the “recommend match” predicate.
- **ADUP-3 recommendations def→use across add+break and return**: at least one test that adds and breaks, and one that returns empty.

## Test Mapping (All-Defs / All-Uses / All-DU-Paths)

All coverage for this method lives in:

- `src/test/java/com/example/wbt/data_flow/RecommenderDataFlowTest.java`

Notable paths:

- `likedMoviesIDs` empty (0-iteration loops): `recommendMovies_emptyLikedMoviesIds_zeroIterationsInFirstLoops_returnsEmpty`
- Skip liked movie via `continue` + recommend via match + `break`: `recommendMovies_exercises_skipLiked_continue_and_recommend_break`
- Unique genre add vs duplicate skip: `recommendMovies_buildsLikedGenres_uniqueAddPath`, `recommendMovies_duplicateGenres_skipsDuplicateAdd`
- No genre match (inner predicate always false): `recommendMovies_noGenreMatches`
- Match on first genre vs later genre: `recommendMovies_multipleGenres_firstMatches`, `recommendMovies_multipleGenres_laterMatches`
