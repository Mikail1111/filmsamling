package domainmodel;

import datasource.FileHandler;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.time.LocalDate;

public class Controller
{
    private MovieCollection movieCollection;
    private FileHandler fileHandler;
    private boolean changed;

    public Controller() {
        movieCollection = new MovieCollection();
        fileHandler = new FileHandler("movies.csv");
        changed = false;
    }

    public void addMovie(String title, String director,
                         int yearCreated, boolean inColor,
                         int lengthInMinutes, String genre) {

        movieCollection.addMovie(title, director,
                yearCreated, inColor, lengthInMinutes, genre);

        changed = true;
    }

    public ArrayList<Movie> getMovies() {
        return movieCollection.getMovies();
    }

    public ArrayList<Movie> searchMovies(String searchText) {
        return movieCollection.searchMovies(searchText);
    }

    public boolean editMovie(Movie movie, String title,
                             String director, int yearCreated,
                             boolean inColor, int lengthInMinutes,
                             String genre) {

        boolean success = movieCollection.editMovie(
                movie, title, director, yearCreated,
                inColor, lengthInMinutes, genre
        );

        if (success) {
            changed = true;
        }

        return success;
    }

    public boolean deleteMovie(Movie movie) {
        boolean success = movieCollection.deleteMovie(movie);

        if (success) {
            changed = true;
        }

        return success;
    }

    public void markAsWatched(Movie movie, LocalDate date) {
        movie.markAsWatched(date);
        changed = true;
    }

    public ArrayList<Movie> getMoviesNotWatchedForDays(int days) {
        LocalDate limit = LocalDate.now().minusDays(days);
        return movieCollection.getMoviesNotWatchedSince(limit);
    }

    public int loadMovies() throws FileNotFoundException {
        ArrayList<Movie> loadedMovies = fileHandler.loadMovies();

        for (Movie movie : loadedMovies) {
            movieCollection.addMovie(movie);
        }

        return loadedMovies.size();
    }

    public boolean saveMoviesIfChanged()
            throws FileNotFoundException {

        if (!changed) {
            return false;
        }

        fileHandler.saveMovies(movieCollection.getMovies());
        changed = false;

        return true;
    }

    public int getSkippedLineCount() {
        return fileHandler.getSkippedLineCount();
    }
}
