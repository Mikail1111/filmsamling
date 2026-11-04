package domainmodel;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Movie implements Comparable<Movie>
{
    private static final int FIRST_MOVIE_YEAR = 1888;

    private String title;
    private String director;
    private int yearCreated;
    private boolean inColor;
    private int lengthInMinutes;
    private String genre;
    private LocalDate lastWatched;

    public Movie(String title, String director, int yearCreated,
                 boolean inColor, int lengthInMinutes, String genre) {

        setTitle(title);
        setDirector(director);
        setYearCreated(yearCreated);
        setInColor(inColor);
        setLengthInMinutes(lengthInMinutes);
        setGenre(genre);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException(
                    "Titlen må ikke være tom.");
        }
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        if (director == null || director.isBlank()) {
            throw new IllegalArgumentException(
                    "Instruktøren må ikke være tom.");
        }
        this.director = director;
    }

    public int getYearCreated() {
        return yearCreated;
    }

    public void setYearCreated(int yearCreated) {
        int latestYear = LocalDate.now().getYear() + 1;

        if (yearCreated < FIRST_MOVIE_YEAR || yearCreated > latestYear) {
            throw new IllegalArgumentException(
                    "Årstallet skal være mellem "
                            + FIRST_MOVIE_YEAR + " og "
                            + latestYear + ".");
        }

        this.yearCreated = yearCreated;
    }

    public boolean isInColor() {
        return inColor;
    }

    public void setInColor(boolean inColor) {
        this.inColor = inColor;
    }

    public int getLengthInMinutes() {
        return lengthInMinutes;
    }

    public void setLengthInMinutes(int lengthInMinutes) {
        if (lengthInMinutes < 1) {
            throw new IllegalArgumentException(
                    "Filmens længde skal være mindst 1 minut.");
        }

        this.lengthInMinutes = lengthInMinutes;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        if (genre == null || genre.isBlank()) {
            throw new IllegalArgumentException(
                    "Genren må ikke være tom.");
        }

        this.genre = genre;
    }

    public LocalDate getLastWatched() {
        return lastWatched;
    }

    public boolean hasBeenWatched() {
        return lastWatched != null;
    }

    public void markAsWatched(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException(
                    "Datoen må ikke være tom.");
        }

        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Datoen må ikke ligge i fremtiden.");
        }

        lastWatched = date;
    }

    public long getDaysSinceWatched(LocalDate today) {
        return ChronoUnit.DAYS.between(lastWatched, today);
    }
    @Override
    public int compareTo(Movie other) {
        return title.compareToIgnoreCase(other.title);
    }
}

