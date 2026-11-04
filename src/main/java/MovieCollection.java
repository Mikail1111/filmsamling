import java.util.ArrayList;
import java.time.LocalDate;

public class MovieCollection
{
    // Finder alle film, hvor søgeteksten indgår i titlen – uanset store og små bogstaver
    private ArrayList<Movie> movies;


    // Constructor
    public MovieCollection()
    {
        movies = new ArrayList<>();
    }
    // Her tilføjer vi film
    public void addMovie(String title, String director, int yearCreated, boolean inColor, int lenghtInMinutes, String genre)
    {
        Movie movie = new Movie(title, director, yearCreated, inColor, lenghtInMinutes, genre);

        movies.add(movie);
    }
    public void addMovie(Movie movie) {
        movies.add(movie);
    }

    // Her henter vi alle film
    public ArrayList<Movie> getMovies()
    {
        return new ArrayList<>(movies);
    }

    // Her søger vi efter Filmens titel
    public ArrayList<Movie> searchMovies(String searchText)
    {
        ArrayList<Movie> matches = new ArrayList<>();

        String text = searchText.toLowerCase();

        for (Movie movie : movies)
        {
            if (movie.getTitle().toLowerCase().contains(text))
            {
                matches.add(movie);
            }
        }

        return matches;
    }


    // Her redigere vi en film
    public boolean editMovie(Movie movie, String title, String director, int yearCreated, boolean inColor, int lengthInMinutes, String genre)
    {

        if (!movies.contains(movie))
        {
            return false;
        }

        // Tjek ALLE nye værdier, før den oprindelige film ændres
        new Movie(title, director, yearCreated,
                inColor, lengthInMinutes, genre);

        // Alle værdier er gyldige, så filmen kan redigeres
        movie.setTitle(title);
        movie.setDirector(director);
        movie.setYearCreated(yearCreated);
        movie.setInColor(inColor);
        movie.setLengthInMinutes(lengthInMinutes);
        movie.setGenre(genre);

        return true;
    }

    // Her sletter vi en film
    public boolean deleteMovie(Movie movie)
    {
        return movies.remove(movie);

    }

    public ArrayList<Movie> getMoviesNotWatchedSince(LocalDate date) {
        ArrayList<Movie> result = new ArrayList<>();

        for (Movie movie : movies) {
            if (!movie.hasBeenWatched() || movie.getLastWatched().isBefore(date)) {
                result.add(movie);
            }
        }

        return result;
    }

    public int getNumberOfMovies() {
        return movies.size();
    }
}


