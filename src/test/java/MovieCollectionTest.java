import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MovieCollectionTest
{
    @Test
    void moviesNeverWatchedAreIncluded() {
        MovieCollection collection = new MovieCollection();

        collection.addMovie("Titanic", "James Cameron",
                1997, true, 194, "Drama");

        LocalDate date = LocalDate.of(2025, 10, 26);

        ArrayList<Movie> result =
                collection.getMoviesNotWatchedSince(date);

        assertEquals(1, result.size());
        assertEquals("Titanic", result.get(0).getTitle());
    }

    @Test
    void onlyMoviesWatchedBeforeDateAreIncluded() {
        MovieCollection collection = new MovieCollection();

        collection.addMovie("Jaws", "Steven Spielberg",
                1975, true, 124, "Thriller");

        collection.addMovie("Titanic", "James Cameron",
                1997, true, 194, "Drama");

        collection.addMovie("Avatar", "James Cameron",
                2009, true, 162, "Science Fiction");

        ArrayList<Movie> movies = collection.getMovies();

        movies.get(0).markAsWatched(LocalDate.of(2025, 9, 30));
        movies.get(1).markAsWatched(LocalDate.of(2025, 10, 26));
        movies.get(2).markAsWatched(LocalDate.of(2025, 10, 27));

        LocalDate date = LocalDate.of(2025, 10, 26);

        ArrayList<Movie> result =
                collection.getMoviesNotWatchedSince(date);

        assertEquals(1, result.size());
        assertEquals("Jaws", result.get(0).getTitle());
    }
}
