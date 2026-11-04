import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class MovieTest
{
    @Test
    void newMovieHasNotBeenWatched() {
        Movie movie = new Movie(
                "Jaws", "Steven Spielberg",
                1975, true, 124, "Thriller"
        );

        assertFalse(movie.hasBeenWatched());
        assertNull(movie.getLastWatched());
    }

    @Test
    void markAsWatchedSavesDate() {
        Movie movie = new Movie(
                "Jaws", "Steven Spielberg",
                1975, true, 124, "Thriller"
        );

        LocalDate date = LocalDate.of(2025, 10, 26);

        movie.markAsWatched(date);

        assertEquals(date, movie.getLastWatched());
    }

    @Test
    void daysSinceWatchedCountsAcrossMonths() {
        Movie movie = new Movie(
                "Jaws", "Steven Spielberg",
                1975, true, 124, "Thriller"
        );

        movie.markAsWatched(LocalDate.of(2025, 9, 30));

        long days = movie.getDaysSinceWatched(
                LocalDate.of(2025, 10, 26)
        );

        assertEquals(26, days);
    }

    @Test
    void constructorSetsAllAttributes() {
        // Arrange + Act
        Movie movie = new Movie(
                "Jaws", "Steven Spielberg",
                1975, true, 124, "Thriller"
        );

        // Assert
        assertEquals("Jaws", movie.getTitle());
        assertEquals("Steven Spielberg", movie.getDirector());
        assertEquals(1975, movie.getYearCreated());
        assertTrue(movie.isInColor());
        assertEquals(124, movie.getLengthInMinutes());
        assertEquals("Thriller", movie.getGenre());
    }

    @Test
    void setTitleChangesTitle() {
        // Arrange
        Movie movie = new Movie(
                "Jaws", "Steven Spielberg",
                1975, true, 124, "Thriller"
        );

        // Act
        movie.setTitle("Dødens gab");

        // Assert
        assertEquals("Dødens gab", movie.getTitle());
    }
}

