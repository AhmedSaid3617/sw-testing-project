package com.example.wbt.data_flow;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.User;
import com.example.UserException;
import com.example.UserParser;

/**
 * All-DU-Paths tests for {@link com.example.UserParser}.
 *
 * Comprehensive coverage of definition-use paths in:
 * - parseUsers: def(i) with step 2 -> different iteration counts and boundary checks
 * - parseUsers: def(parts) -> P-use(length check) true/false paths
 * - addLikedMovies: def(parts) -> P-use(digit check) -> loop over ids
 * Each test covers specific DU-paths through the for-loop with i+=2 stepping.
 */
public class UserParserDataFlowTest {

    @Test
    void parseUsers_pusePartsLengthNot2_true_throws() {
        // P-use(parts.length != 2) true -> throws invalid format.
        UserParser parser = new UserParser();

        String data = "Alice,123456789,EXTRA\n" +
                      "M001\n";

        assertThrows(UserException.class, () -> parser.parseUsers(data));
    }

    @Test
    void parseUsers_puseHasLikedLine_false_throws() {
        // P-use(i+1 < length) false -> throws missing liked movies line.
        UserParser parser = new UserParser();

        String data = "Alice,123456789\n";
        assertThrows(UserException.class, () -> parser.parseUsers(data));
    }

    @Test
    void parseUsers_addLikedMovies_puseFirstTokenHasDigit_false_throws() {
        // In addLikedMovies: P-use(parts[0].matches(".*\\d.*") == false) true -> throws.
        UserParser parser = new UserParser();

        String data = "Alice,123456789\n" +
                      "MOVIE\n";

        assertThrows(UserException.class, () -> parser.parseUsers(data));
    }

    @Test
    void parseUsers_emptyInput_handlesGracefully() {
        // DU-path: empty string splits to [""], length=1 -> tries to parse -> throws on bad format.
        // Note: UserParser doesn't handle empty input gracefully - it tries to parse the empty string.
        UserParser parser = new UserParser();
        assertThrows(UserException.class, () -> parser.parseUsers(""));
    }

    @Test
    void parseUsers_singleUser_oneIteration() throws Exception {
        // DU-path: def(i=0) -> one iteration -> redef(i=2) -> P-use(i < length) false -> exit.
        UserParser parser = new UserParser();

        String data = "Alice,123456789\n" +
                      "M001\n";

        List<User> users = parser.parseUsers(data);
        assertEquals(1, users.size());
        assertEquals("123456789", users.get(0).getId());
    }

    @Test
    void parseUsers_twoUsers_twoIterations_coversIReDefAndSecondIterationLikedMovies() throws Exception {
        // DU-path: def(i=0) -> iteration 1 -> redef(i=2) -> iteration 2 -> redef(i=4) -> exit.
        // Also covers the i+1 < length boundary twice.
        UserParser parser = new UserParser();

        String data = "Alice,123456789\n" +
                      "M001,M002\n" +
                      "Bob,987654321\n" +
                      "M003\n";

        List<User> users = parser.parseUsers(data);
        assertEquals(2, users.size());
        assertEquals(List.of("M001", "M002"), users.get(0).getLikedMovies());
        assertEquals(List.of("M003"), users.get(1).getLikedMovies());
    }

    @Test
    void parseUsers_partsLengthEquals2_success() throws Exception {
        // DU-path: def(parts) -> P-use(parts.length != 2) false -> continue processing.
        UserParser parser = new UserParser();

        String data = "Alice,123456789\n" +
                      "M001\n";

        List<User> users = parser.parseUsers(data);
        assertEquals(1, users.size());
    }

    @Test
    void parseUsers_partsLengthOne_throws() {
        // DU-path: def(parts) -> P-use(parts.length != 2) true (length=1) -> throw.
        UserParser parser = new UserParser();

        String data = "AliceOnly\n" +
                      "M001\n";

        assertThrows(UserException.class, () -> parser.parseUsers(data));
    }

    @Test
    void parseUsers_partsLengthThree_throws() {
        // DU-path: def(parts) -> P-use(parts.length != 2) true (length=3) -> throw.
        UserParser parser = new UserParser();

        String data = "Alice,123456789,Extra\n" +
                      "M001\n";

        assertThrows(UserException.class, () -> parser.parseUsers(data));
    }

    @Test
    void parseUsers_boundaryCheck_iPlusOneEqualsLength() throws Exception {
        // DU-path: def(i) -> P-use(i+1 < length) true (exactly equal at boundary) -> call addLikedMovies.
        UserParser parser = new UserParser();

        String data = "Alice,123456789\n" +
                      "M001\n";

        List<User> users = parser.parseUsers(data);
        assertEquals(1, users.size());
        assertEquals(List.of("M001"), users.get(0).getLikedMovies());
    }

    @Test
    void addLikedMovies_singleMovieId() throws Exception {
        // DU-path: def(parts) with one element -> loop one iteration -> C-use(user.addLikedMovie).
        UserParser parser = new UserParser();

        String data = "Alice,123456789\n" +
                      "M001\n";

        List<User> users = parser.parseUsers(data);
        assertEquals(List.of("M001"), users.get(0).getLikedMovies());
    }

    @Test
    void addLikedMovies_multipleMovieIds() throws Exception {
        // DU-path: def(parts) with multiple elements -> loop multiple iterations.
        UserParser parser = new UserParser();

        String data = "Alice,123456789\n" +
                      "M001,M002,M003,M004\n";

        List<User> users = parser.parseUsers(data);
        assertEquals(List.of("M001", "M002", "M003", "M004"), users.get(0).getLikedMovies());
    }

    @Test
    void addLikedMovies_firstTokenNoDigit_throws() {
        // DU-path: def(parts) -> P-use(parts[0].matches(".*\\d.*") == false) true -> throw.
        UserParser parser = new UserParser();

        String data = "Alice,123456789\n" +
                      "INVALID\n";

        assertThrows(UserException.class, () -> parser.parseUsers(data));
    }

    @Test
    void addLikedMovies_firstTokenHasDigit_success() throws Exception {
        // DU-path: def(parts) -> P-use(parts[0].matches(".*\\d.*") == false) false -> continue to loop.
        UserParser parser = new UserParser();

        String data = "Alice,123456789\n" +
                      "M001,M002\n";

        List<User> users = parser.parseUsers(data);
        assertEquals(2, users.get(0).getLikedMovies().size());
    }
}
