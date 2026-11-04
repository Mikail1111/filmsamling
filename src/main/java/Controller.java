import java.util.ArrayList;
import java.time.LocalDate;

public class Controller
{
    private MovieCollection movieCollection;

    public Controller() {
        movieCollection = new MovieCollection();
    }

    // Tilføjer en film
    public void addMovie(String title, String director,
                         int yearCreated, boolean inColor,
                         int lengthInMinutes, String genre) {

        movieCollection.addMovie(title, director, yearCreated,
                inColor, lengthInMinutes, genre);
    }

    // Henter alle film
    public ArrayList<Movie> getMovies() {
        return movieCollection.getMovies();
    }

    // Søger efter en film
    public ArrayList<Movie> searchMovies(String searchText) {
        return movieCollection.searchMovies(searchText);
    }

    // Redigerer en film
    public boolean editMovie(Movie movie, String title,
                             String director, int yearCreated,
                             boolean inColor, int lengthInMinutes,
                             String genre) {

        return movieCollection.editMovie(movie, title, director,
                yearCreated, inColor, lengthInMinutes, genre);
    }

    // Sletter en film
    public boolean deleteMovie(Movie movie) {
        return movieCollection.deleteMovie(movie);
    }

    // Her kan vi finde film der ikke er blevet set længe
    public void markAsWatched(Movie movie, LocalDate date)
    {
        movie.markAsWatched(date);
    }
    public ArrayList<Movie> getMoviesNotWatchedForDays(int days) {
        LocalDate limit = LocalDate.now().minusDays(days);

        return movieCollection.getMoviesNotWatchedSince(limit);
    }
}
