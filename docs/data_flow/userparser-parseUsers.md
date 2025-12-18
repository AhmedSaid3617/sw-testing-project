# Data-Flow Testing Report: `UserParser.parseUsers(String)`

## Target

- Class: `com.example.UserParser`
- Method: `List<User> parseUsers(String usersFileData)`

## Why this is one of the 3 most complex methods

This method combines:

- A counted loop with a non-standard step (`i += 2`), creating distinct boundary behaviors
- Multiple exception branches based on input structure
- A call to a helper (`addLikedMovies`) that contains its own predicate + loop

Together this yields multiple definition/uses for `i`, `parts`, `user`, and the `(i+1 < length)` predicate.

## Control-Flow Diagram (CFG)

```mermaid
flowchart TD
  A([Entry]) --> B[Def: lines = split(usersFileData)\nDef: length = lines.length]
  B --> C[Def: i = 0]
  C --> D{P-use: i < length}
  D -- false --> Z([Return users_list])
  D -- true --> E[Def: parts = lines[i].split(',')]
  E --> F{P-use: parts.length != 2}
  F -- true --> G[[Throw UserException\n"Invalid user data format at line {i+1}"]]
  F -- false --> H[Def: user = new User(parts[0], parts[1], new ArrayList())\nC-use: users_list.add(user)]
  H --> I{P-use: i+1 < length}
  I -- false --> J[[Throw UserException\n"Liked movies are invalid for user {user.id}"]]
  I -- true --> K[Call addLikedMovies(lines[i+1], user)]
  K --> L[Def: i = i + 2] --> D

  subgraph addLikedMovies(l,user)
    K1[Def: parts = l.split(',')] --> K2{P-use: parts[0] hasDigit == false}
    K2 -- true --> K3[[Throw UserException\n"Liked movies are invalid for user {user.id}"]]
    K2 -- false --> K4[For each id in parts:\nC-use: user.addLikedMovie(id)]
  end
```

## Def–Use Table

Legend:

- **Def**: assignment / initialization
- **P-use**: predicate use
- **C-use**: computational use

Rows below are _statement-oriented_ (they follow the method’s flow). Variables appear under **Def**, **P-use**, and/or **C-use** as appropriate.

| Step (code shape)                                        | Def      | P-use         | C-use                |
| -------------------------------------------------------- | -------- | ------------- | -------------------- |
| `lines = usersFileData.split("\n")`                      | `lines`  |               | `usersFileData`      |
| `length = lines.length`                                  | `length` |               | `lines`              |
| `for (i = 0; i < length; i += 2)`                        | `i`      | `i`, `length` |                      |
| `parts = lines[i].split(",")`                            | `parts`  |               | `lines`, `i`         |
| `if (parts.length != 2) throw ...`                       |          | `parts`       | `i` (in message)     |
| `user = new User(parts[0], parts[1], new ArrayList<>())` | `user`   |               | `parts`              |
| `users_list.add(user)`                                   |          |               | `users_list`, `user` |
| `if (i+1 < length)`                                      |          | `i`, `length` |                      |
| `addLikedMovies(lines[i+1], user)`                       |          |               | `lines`, `i`, `user` |
| `else throw ...` (missing liked-movies line)             |          |               | `user` (in message)  |
| `return users_list`                                      |          |               | `users_list`         |

Helper `addLikedMovies(l, user)` (called by `parseUsers`):

| Step (code shape)                                | Def     | P-use   | C-use               |
| ------------------------------------------------ | ------- | ------- | ------------------- |
| `parts = l.split(",")`                           | `parts` |         | `l`                 |
| `if (parts[0] hasDigit == false) throw ...`      |         | `parts` | `user` (in message) |
| `for (String id : parts) user.addLikedMovie(id)` |         |         | `parts`, `user`     |

## Proposed Testcases (All-Defs / All-Uses / All-DU-Paths)

These are _black-box_ testcases (input string → expected outcome) that are sufficient to satisfy the data-flow criteria for `parseUsers` and its helper call.

### All-Defs (each definition reaches at least one use)

- **AD-1 single valid user**: `"Alice,123456789\nM001\n"` → returns 1 user with liked `[M001]`.
- **AD-2 two valid users (2 iterations)**: `"Alice,123456789\nM001,M002\nBob,987654321\nM003\n"` → returns 2 users.

### All-Uses (exercise each reachable P-use/C-use)

- **AU-1 parts length != 2**: `"Alice,123456789,EXTRA\nM001\n"` → throws “Invalid user data format…”.
- **AU-2 missing liked-movies line**: `"Alice,123456789\n"` → throws “Liked movies are invalid…”.
- **AU-3 addLikedMovies digit predicate fails**: `"Alice,123456789\nMOVIE\n"` (no digit anywhere in first token) → throws.
- **AU-4 addLikedMovies multiple ids**: `"Alice,123456789\nM001,M002,M003\n"` → liked list has 3 entries.

### All-DU-Paths (ADUP)

- **ADUP-1 i def→use across loop back-edge**: two-users input to cover `i` redefinition via `i += 2` and the next iteration’s uses.
- **ADUP-2 (i+1 < length) true and false**: one valid user (true) + one-header-only input (false) to exercise both predicate outcomes.
- **ADUP-3 liked-line parts def→use**: one test where `parts[0]` triggers the throw, and one where it flows through the `for` loop and adds multiple IDs.

## Test Mapping (All-Defs / All-Uses / All-DU-Paths)

All coverage for this method lives in:

- `src/test/java/com/example/wbt/data_flow/UserParserDataFlowTest.java`

Notable paths:

- `parts.length != 2` throw: `parseUsers_pusePartsLengthNot2_true_throws`, `parseUsers_partsLengthOne_throws`, `parseUsers_partsLengthThree_throws`
- `(i+1 < length)` false throw (missing liked-movies line): `parseUsers_puseHasLikedLine_false_throws`
- `addLikedMovies` digit predicate throw: `parseUsers_addLikedMovies_puseFirstTokenHasDigit_false_throws`
- Multi-user / two iterations (covers `i` redefinition and second iteration DU-paths): `parseUsers_twoUsers_twoIterations_coversIReDefAndSecondIterationLikedMovies`
