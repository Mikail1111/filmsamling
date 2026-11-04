import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class FileHandlerTest {

    private static final String TEST_FILE = "test-movies.csv";

    @AfterEach
    void deleteTestFile() {
        new File(TEST_FILE).delete();
    }

    @Test
    void savedMoviesCanBeLoadedAgain()
            throws FileNotFoundException {

        // Arrange
        FileHandler fileHandler = new FileHandler(TEST_FILE);

        ArrayList<Movie> movies = new ArrayList<>();

        Movie movie1 = new Movie(
                "Jaws", "Steven Spielberg",
                1975, true, 124, "Thriller"
        );

        movie1.markAsWatched(LocalDate.of(2025, 10, 30));

        Movie movie2 = new Movie(
                "Adams æbler", "Anders Thomas Jensen",
                2005, true, 94, "Komedie"
        );

        movies.add(movie1);
        movies.add(movie2);

        // Act
        fileHandler.saveMovies(movies);
        ArrayList<Movie> loadedMovies = fileHandler.loadMovies();

        // Assert
        assertEquals(2, loadedMovies.size());

        Movie loaded1 = loadedMovies.get(0);

        assertEquals("Jaws", loaded1.getTitle());
        assertEquals("Steven Spielberg", loaded1.getDirector());
        assertEquals(1975, loaded1.getYearCreated());
        assertTrue(loaded1.isInColor());
        assertEquals(124, loaded1.getLengthInMinutes());
        assertEquals("Thriller", loaded1.getGenre());
        assertEquals(
                LocalDate.of(2025, 10, 30),
                loaded1.getLastWatched()
        );

        Movie loaded2 = loadedMovies.get(1);

        assertEquals("Adams æbler", loaded2.getTitle());
        assertEquals("Anders Thomas Jensen", loaded2.getDirector());
        assertEquals(2005, loaded2.getYearCreated());
        assertTrue(loaded2.isInColor());
        assertEquals(94, loaded2.getLengthInMinutes());
        assertEquals("Komedie", loaded2.getGenre());
        assertFalse(loaded2.hasBeenWatched());
    }

    @Test
    void emptyListCanBeSavedAndLoaded()
            throws FileNotFoundException {

        FileHandler fileHandler = new FileHandler(TEST_FILE);

        ArrayList<Movie> movies = new ArrayList<>();

        fileHandler.saveMovies(movies);

        ArrayList<Movie> loadedMovies = fileHandler.loadMovies();

        assertTrue(loadedMovies.isEmpty());
    }

    @Test
    void missingFileThrowsException() {
        FileHandler fileHandler = new FileHandler(TEST_FILE);

        new File(TEST_FILE).delete();

        assertThrows(
                FileNotFoundException.class,
                () -> fileHandler.loadMovies()
        );
    }

    @Test
    void invalidLinesAreSkipped()
            throws FileNotFoundException {

        // Arrange
        try (PrintStream output =
                     new PrintStream(new File(TEST_FILE))) {

            output.println(
                    "title;director;yearCreated;inColor;"
                            + "lengthInMinutes;genre;lastWatched"
            );

            // Gyldig film
            output.println(
                    "Jaws;Steven Spielberg;1975;"
                            + "true;124;Thriller;"
            );

            // Ugyldigt årstal
            output.println(
                    "Test;Instruktør;abc;true;90;Drama;"
            );

            // For få felter
            output.println("Ødelagt;Linje");
        }

        FileHandler fileHandler = new FileHandler(TEST_FILE);

        // Act
        ArrayList<Movie> loadedMovies = fileHandler.loadMovies();

        // Assert
        assertEquals(1, loadedMovies.size());
        assertEquals("Jaws", loadedMovies.get(0).getTitle());
        assertEquals(2, fileHandler.getSkippedLineCount());
    }
}