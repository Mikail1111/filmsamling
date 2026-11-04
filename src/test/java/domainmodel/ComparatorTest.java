package domainmodel;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ComparatorTest {

    private final Movie jaws = new Movie(
            "Jaws", "Steven Spielberg",
            1975, true, 124, "Thriller"
    );

    private final Movie psycho = new Movie(
            "Psycho", "Alfred Hitchcock",
            1960, false, 109, "Horror"
    );

    private final Movie casablanca = new Movie(
            "Casablanca", "Michael Curtiz",
            1942, false, 102, "Drama"
    );

    @Test
    void titleComparatorSortsAlphabetically() {
        TitleComparator comparator = new TitleComparator();

        assertTrue(comparator.compare(casablanca, jaws) < 0);
        assertTrue(comparator.compare(jaws, casablanca) > 0);

        Movie uppercase = new Movie(
                "JAWS", "Test", 2000, true, 90, "Drama"
        );

        assertEquals(0, comparator.compare(jaws, uppercase));
    }

    @Test
    void directorComparatorSortsAlphabetically() {
        DirectorComparator comparator = new DirectorComparator();

        assertTrue(comparator.compare(psycho, casablanca) < 0);
        assertTrue(comparator.compare(casablanca, psycho) > 0);
    }

    @Test
    void yearComparatorSortsOldestFirst() {
        YearComparator comparator = new YearComparator();

        assertTrue(comparator.compare(casablanca, psycho) < 0);
        assertTrue(comparator.compare(jaws, psycho) > 0);
    }

    @Test
    void lengthComparatorSortsShortestFirst() {
        LengthComparator comparator = new LengthComparator();

        assertTrue(comparator.compare(casablanca, jaws) < 0);
        assertTrue(comparator.compare(jaws, casablanca) > 0);
    }

    @Test
    void genreComparatorSortsAlphabetically() {
        GenreComparator comparator = new GenreComparator();

        assertTrue(comparator.compare(casablanca, psycho) < 0);
        assertTrue(comparator.compare(psycho, casablanca) > 0);
    }

    @Test
    void movieCompareToSortsByTitle() {
        assertTrue(casablanca.compareTo(jaws) < 0);
        assertTrue(jaws.compareTo(casablanca) > 0);

        Movie uppercase = new Movie(
                "JAWS", "Test", 2000, true, 90, "Drama"
        );

        assertEquals(0, jaws.compareTo(uppercase));
    }

    @Test
    void movieCollectionSortsByTitle() {
        MovieCollection collection = new MovieCollection();

        collection.addMovie(jaws);
        collection.addMovie(psycho);
        collection.addMovie(casablanca);

        ArrayList<Movie> sorted =
                collection.getMoviesSortedByTitle();

        assertEquals("Casablanca", sorted.get(0).getTitle());
        assertEquals("Jaws", sorted.get(1).getTitle());
        assertEquals("Psycho", sorted.get(2).getTitle());
    }

    @Test
    void sortingDoesNotChangeOriginalOrder() {
        MovieCollection collection = new MovieCollection();

        collection.addMovie(jaws);
        collection.addMovie(psycho);
        collection.addMovie(casablanca);

        collection.getMoviesSortedByTitle();

        ArrayList<Movie> original = collection.getMovies();

        assertEquals("Jaws", original.get(0).getTitle());
        assertEquals("Psycho", original.get(1).getTitle());
        assertEquals("Casablanca", original.get(2).getTitle());
    }

    @Test
    void controllerSortsMoviesByYear() {
        Controller controller = new Controller();

        controller.addMovie(
                "Jaws", "Steven Spielberg",
                1975, true, 124, "Thriller"
        );

        controller.addMovie(
                "Psycho", "Alfred Hitchcock",
                1960, false, 109, "Horror"
        );

        controller.addMovie(
                "Casablanca", "Michael Curtiz",
                1942, false, 102, "Drama"
        );

        ArrayList<Movie> sorted =
                controller.getMoviesSortedBy(SortField.YEAR);

        assertEquals(1942, sorted.get(0).getYearCreated());
        assertEquals(1960, sorted.get(1).getYearCreated());
        assertEquals(1975, sorted.get(2).getYearCreated());
    }
}