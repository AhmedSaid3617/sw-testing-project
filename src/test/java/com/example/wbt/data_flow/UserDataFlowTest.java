package com.example.wbt.data_flow;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.User;
import com.example.UserException;

/**
 * All-DU-Paths tests for {@link com.example.User#User(String, String, java.util.List)}.
 *
 * Comprehensive coverage of all definition-use paths through sequential validations:
 * - Def(name) -> P-use(name regex) both true/false paths
 * - Def(id) -> P-use(format regex) both paths -> P-use(length check) both paths
 * - Success path with all C-uses for field assignments
 * Each validation creates different DU-paths to cover.
 */
public class UserDataFlowTest {

    @Test
    void ctor_puseNameInvalid_throws() {
        // P-use(!name.matches(...)) true -> throws.
        assertThrows(UserException.class, () -> new User(" Hassan", "123456789", new ArrayList<>()));
        assertThrows(UserException.class, () -> new User("Hassan1", "123456789", new ArrayList<>()));
    }

    @Test
    void ctor_puseIdFormatInvalid_throws() {
        // P-use(!id.matches(...)) true -> throws.
        assertThrows(UserException.class, () -> new User("Amer", "ABC123456", new ArrayList<>()));
        assertThrows(UserException.class, () -> new User("Amer", "123-456789", new ArrayList<>()));
    }

    @Test
    void ctor_puseIdLengthInvalid_throws() {
        // P-use(id.length() != 9) true -> throws.
        assertThrows(UserException.class, () -> new User("Gamal", "12345678", new ArrayList<>()));
        assertThrows(UserException.class, () -> new User("Gamal", "1234567890", new ArrayList<>()));
    }

    @Test
    void ctor_success_setsFields() throws Exception {
        // Success path: predicates false -> C-use(this.field = param) assignments.
        User user = new User("Amer", "123456789", new ArrayList<>());
        assertEquals("Amer", user.getName());
        assertEquals("123456789", user.getId());
        assertNotNull(user.getLikedMovies());
        assertEquals(0, user.getLikedMovies().size());
    }

    @Test
    void ctor_nameStartsWithSpace_fails() {
        // DU-path: def(name) -> P-use(!name.matches(...)) true (starts with space) -> throw.
        assertThrows(UserException.class, () -> new User(" Hassan", "123456789", new ArrayList<>()));
    }

    @Test
    void ctor_nameContainsDigit_fails() {
        // DU-path: def(name) -> P-use(!name.matches(...)) true (has digit) -> throw.
        assertThrows(UserException.class, () -> new User("Hassan1", "123456789", new ArrayList<>()));
    }

    @Test
    void ctor_nameContainsSpecialChar_fails() {
        // DU-path: def(name) -> P-use(!name.matches(...)) true (special char) -> throw.
        assertThrows(UserException.class, () -> new User("Hassan!", "123456789", new ArrayList<>()));
    }

    @Test
    void ctor_nameValid_singleWord() throws Exception {
        // DU-path: def(name) -> P-use(regex) false (single word) -> continue to id check.
        User user = new User("Hassan", "123456789", new ArrayList<>());
        assertEquals("Hassan", user.getName());
    }

    @Test
    void ctor_nameValid_multipleWords() throws Exception {
        // DU-path: def(name) -> P-use(regex) false (multiple words with spaces) -> continue.
        User user = new User("Alice Bob Carol", "123456789", new ArrayList<>());
        assertEquals("Alice Bob Carol", user.getName());
    }

    @Test
    void ctor_nameValid_mixedCase() throws Exception {
        // DU-path: def(name) -> P-use(regex) false (mixed case letters) -> continue.
        User user = new User("AlIcE", "123456789", new ArrayList<>());
        assertEquals("AlIcE", user.getName());
    }

    @Test
    void ctor_idStartsWithLetter_formatFails() {
        // DU-path: def(id) -> P-use(!id.matches("[0-9]+[A-Za-z]?")) true (starts with letter) -> throw.
        assertThrows(UserException.class, () -> new User("Alice", "ABC123456", new ArrayList<>()));
    }

    @Test
    void ctor_idContainsSpecialChar_formatFails() {
        // DU-path: def(id) -> P-use(format regex) true (has special char) -> throw.
        assertThrows(UserException.class, () -> new User("Alice", "123-45678", new ArrayList<>()));
    }

    @Test
    void ctor_idMultipleLetters_formatFails() {
        // DU-path: def(id) -> P-use(format regex) true (multiple letters at end) -> throw.
        assertThrows(UserException.class, () -> new User("Alice", "12345678AB", new ArrayList<>()));
    }

    @Test
    void ctor_idOnlyDigits_validFormat() throws Exception {
        // DU-path: def(id) -> P-use(format regex) false (only digits) -> P-use(length check).
        User user = new User("Alice", "123456789", new ArrayList<>());
        assertEquals("123456789", user.getId());
    }

    @Test
    void ctor_idDigitsWithOneLetter_validFormat() throws Exception {
        // DU-path: def(id) -> P-use(format regex) false (digits + one letter) -> P-use(length check).
        User user = new User("Alice", "12345678A", new ArrayList<>());
        assertEquals("12345678A", user.getId());
    }

    @Test
    void ctor_idLengthTooShort() {
        // DU-path: def(id) -> format valid -> P-use(id.length() != 9) true (length < 9) -> throw.
        assertThrows(UserException.class, () -> new User("Alice", "12345678", new ArrayList<>()));
    }

    @Test
    void ctor_idLengthTooLong() {
        // DU-path: def(id) -> format valid -> P-use(id.length() != 9) true (length > 9) -> throw.
        assertThrows(UserException.class, () -> new User("Alice", "1234567890", new ArrayList<>()));
    }

    @Test
    void ctor_idLengthExactly9_passes() throws Exception {
        // DU-path: def(id) -> format valid -> P-use(length != 9) false -> field assignments.
        User user = new User("Alice", "123456789", new ArrayList<>());
        assertEquals(9, user.getId().length());
    }

    @Test
    void ctor_allValidationsPass_fieldsAssigned() throws Exception {
        // DU-path: all validations false -> C-use(this.name, this.id, this.likedMoviesIDs).
        User user = new User("Bob Smith", "98765432X", new ArrayList<>(List.of("M001")));
        assertEquals("Bob Smith", user.getName());
        assertEquals("98765432X", user.getId());
        assertEquals(1, user.getLikedMovies().size());
    }
}
