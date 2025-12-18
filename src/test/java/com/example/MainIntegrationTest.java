package com.example;

import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mockito;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MainIntegrationTest {

    @Test
    void testMainTopDownIntegration() throws Exception {

        // --- mocks ---
        Parser parser = mock(Parser.class);
        ParseResult parseResult = mock(ParseResult.class);
        DataStore dataStore = mock(DataStore.class);
        Recommender recommender = mock(Recommender.class);
        RecommendationWriter writer = mock(RecommendationWriter.class);

        User user = new User("Mohamed","123456789", List.of("TM001"));
        Movie movie = new Movie("The Matrix","TM001" ,List.of("Action","Sci-Fi","Thriller"));
        


        // --- behavior ---
        when(parser.parse()).thenReturn(parseResult);
        when(dataStore.getUsers()).thenReturn(List.of(user));
        when(recommender.recommendMovies(user)).thenReturn(List.of(movie));

        // --- spy Main to inject mocks ---
        Main main = Mockito.spy(new Main());

        doReturn(parser).when(main)
                .createParser(anyString(), anyString());
        doReturn(dataStore).when(main)
                .createDataStore(parseResult);
        doReturn(recommender).when(main)
                .createRecommender(dataStore);
        doReturn(writer).when(main)
                .createWriter(anyString());

        // --- run ---
        main.run("users.txt", "movies.txt", "out.txt");

        // --- verify interactions ---
        verify(parser).parse();
        verify(dataStore).getUsers();
        verify(recommender).recommendMovies(user);
        verify(writer).writeRecommendations(user, List.of(movie));
    }
}
