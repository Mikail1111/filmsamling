import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MovieCollectionTest {

    private MovieCollection collection;

    @BeforeEach
    void setUp() {
        // Arrange: En ny samling før hver test
        collection = new MovieCollection();

        collection.addMovie("The Godfather", "Francis Ford Coppola",
                1972, true, 175, "Crime");

        collection.addMovie("Casablanca", "Michael Curtiz",
                1942, false, 102, "Drama");

        collection.addMovie("Godzilla", "Ishiro Honda",
                1954, false, 96, "Sci-fi");
    }

    @Test
    void newCollectionHasZeroMovies() {
        // Arrange
        MovieCollection emptyCollection = new MovieCollection();

        // Act
        int count = emptyCollection.getNumberOfMovies();

        // Assert
        assertEquals(0, count);
    }

    @Test
    void addOneMovie() {
        MovieCollection emptyCollection = new MovieCollection();

        emptyCollection.addMovie("Jaws", "Steven Spielberg",
                1975, true, 124, "Thriller");

        assertEquals(1, emptyCollection.getNumberOfMovies());
        assertEquals("Jaws",
                emptyCollection.getMovies().get(0).getTitle());
    }

    @Test
    void addSeveralMovies() {
        assertEquals(3, collection.getNumberOfMovies());
    }

    @Test
    void searchWithoutMatchReturnsEmptyList() {
        ArrayList<Movie> result = collection.searchMovies("Avatar");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void searchWithOneMatch() {
        ArrayList<Movie> result = collection.searchMovies("Casablanca");

        assertEquals(1, result.size());
        assertEquals("Casablanca", result.get(0).getTitle());
    }

    @Test
    void searchWithSeveralMatches() {
        ArrayList<Movie> result = collection.searchMovies("god");

        assertEquals(2, result.size());
    }

    @Test
    void searchIgnoresUpperAndLowerCase() {
        ArrayList<Movie> result = collection.searchMovies("GOD");

        assertEquals(2, result.size());
    }

    @Test
    void editMovieChangesInformation() {
        Movie movie = collection.searchMovies("Casablanca").get(0);

        boolean edited = collection.editMovie(
                movie, "Casablanca Updated", "New Director",
                1943, true, 110, "Romance"
        );

        assertTrue(edited);
        assertEquals("Casablanca Updated", movie.getTitle());
        assertEquals("New Director", movie.getDirector());
        assertEquals(1943, movie.getYearCreated());
        assertTrue(movie.isInColor());
        assertEquals(110, movie.getLengthInMinutes());
        assertEquals("Romance", movie.getGenre());
    }

    @Test
    void editMovieNotInCollectionReturnsFalse() {
        Movie unknownMovie = new Movie(
                "Jaws", "Steven Spielberg",
                1975, true, 124, "Thriller"
        );

        boolean edited = collection.editMovie(
                unknownMovie, "Changed Title", "New Director",
                2000, false, 100, "Drama"
        );

        assertFalse(edited);
        assertEquals("Jaws", unknownMovie.getTitle());
    }

    @Test
    void deleteMovieRemovesIt() {
        Movie movie = collection.searchMovies("Casablanca").get(0);

        boolean deleted = collection.deleteMovie(movie);

        assertTrue(deleted);
        assertEquals(2, collection.getNumberOfMovies());
        assertTrue(collection.searchMovies("Casablanca").isEmpty());
    }

    @Test
    void deleteMovieNotInCollectionReturnsFalse() {
        Movie unknownMovie = new Movie(
                "Jaws", "Steven Spielberg",
                1975, true, 124, "Thriller"
        );

        boolean deleted = collection.deleteMovie(unknownMovie);

        assertFalse(deleted);
        assertEquals(3, collection.getNumberOfMovies());
    }

    // De to datotests fra første halvdel af del 5

    @Test
    void moviesNeverWatchedAreIncluded() {
        MovieCollection emptyCollection = new MovieCollection();

        emptyCollection.addMovie("Titanic", "James Cameron",
                1997, true, 194, "Drama");

        LocalDate date = LocalDate.of(2025, 10, 26);

        ArrayList<Movie> result =
                emptyCollection.getMoviesNotWatchedSince(date);

        assertEquals(1, result.size());
        assertEquals("Titanic", result.get(0).getTitle());
    }

    @Test
    void onlyMoviesWatchedBeforeDateAreIncluded() {
        MovieCollection emptyCollection = new MovieCollection();

        emptyCollection.addMovie("Jaws", "Steven Spielberg",
                1975, true, 124, "Thriller");

        emptyCollection.addMovie("Titanic", "James Cameron",
                1997, true, 194, "Drama");

        emptyCollection.addMovie("Avatar", "James Cameron",
                2009, true, 162, "Science Fiction");

        ArrayList<Movie> movies = emptyCollection.getMovies();

        movies.get(0).markAsWatched(LocalDate.of(2025, 9, 30));
        movies.get(1).markAsWatched(LocalDate.of(2025, 10, 26));
        movies.get(2).markAsWatched(LocalDate.of(2025, 10, 27));

        LocalDate date = LocalDate.of(2025, 10, 26);

        ArrayList<Movie> result =
                emptyCollection.getMoviesNotWatchedSince(date);

        assertEquals(1, result.size());
        assertEquals("Jaws", result.get(0).getTitle());
    }
}