package ui;
import domainmodel.Controller;
import domainmodel.Movie;

import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.io.FileNotFoundException;
import domainmodel.SortField;

public class UserInterface
{
    private Controller controller;
    private Scanner scanner;
    private final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("dd-MM-uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

    public UserInterface() {
        controller = new Controller();
        scanner = new Scanner(System.in);
    }

    public void startProgram() {
        System.out.println("Velkommen til min filmsamling!");

        loadMovies();

        boolean running = true;

        while (running) {
            showMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    createMovie();
                    break;
                case "2":
                    showAllMovies();
                    break;
                case "3":
                    searchMovies();
                    break;
                case "4":
                    editMovie();
                    break;
                case "5":
                    deleteMovie();
                    break;
                case "6":
                    markMovieAsWatched();
                    break;
                case "7":
                    showMoviesNotWatchedForDays();
                    break;
                case "8":
                    showMoviesSorted();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Ugyldigt valg. Prøv igen.");
            }
        }

        saveMovies();
        System.out.println("Farvel!");
    }

    private void loadMovies() {
        try {
            int count = controller.loadMovies();

            System.out.println(count + " film er indlæst.");

            int skipped = controller.getSkippedLineCount();

            if (skipped > 0) {
                System.out.println(
                        "Advarsel: " + skipped
                                + " linje(r) i filen kunne ikke læses "
                                + "og er sprunget over."
                );
            }

        } catch (FileNotFoundException e) {
            System.out.println(
                    "Der er ingen gemt filmsamling endnu "
                            + "– du starter med en tom samling."
            );
        }
    }
    private void saveMovies() {
        try {
            boolean saved = controller.saveMoviesIfChanged();

            if (saved) {
                System.out.println("Filmsamlingen er gemt.");
            } else {
                System.out.println(
                        "Ingen ændringer – intet at gemme."
                );
            }

        } catch (FileNotFoundException e) {
            System.out.println(
                    "Fejl: Filmsamlingen kunne ikke gemmes. "
                            + e.getMessage()
            );
        }
    }

    private void showMenu() {
        System.out.println("--- FILMSAMLING ---");
        System.out.println("1. Opret film");
        System.out.println("2. Vis alle film");
        System.out.println("3. Søg efter film");
        System.out.println("4. Rediger film");
        System.out.println("5. Slet film");
        System.out.println("6. Markér en film som set");
        System.out.println("7. Find film, du ikke har set længe");
        System.out.println("8. Vis film sorteret efter ...");
        System.out.println("0. Afslut");
        System.out.print("Vælg: ");
    }

    private void createMovie() {
        System.out.print("Titel: ");
        String title = scanner.nextLine();

        System.out.print("Instruktør: ");
        String director = scanner.nextLine();

        int year = readInt("Udgivelsesår: ");

        boolean inColor = readYesNo("Er filmen i farver? (ja/nej): ");

        int length = readInt("Længde i minutter: ");

        System.out.print("Genre: ");
        String genre = scanner.nextLine();

        try {
            controller.addMovie(title, director, year,
                    inColor, length, genre);

            System.out.println("Filmen er blevet tilføjet!");

        } catch (IllegalArgumentException e) {
            System.out.println(
                    "Filmen blev ikke oprettet: " + e.getMessage()
            );
        }
    }

    private void showAllMovies() {
        ArrayList<Movie> movies = controller.getMoviesSortedByTitle();

        if (movies.isEmpty()) {
            System.out.println("Filmsamlingen er tom.");
            return;
        }

        for (Movie movie : movies) {
            printMovie(movie);
        }
    }

    private void searchMovies() {
        System.out.print("Skriv en del af filmens titel: ");
        String searchText = scanner.nextLine();

        ArrayList<Movie> matches =
                controller.searchMovies(searchText);

        if (matches.isEmpty()) {
            System.out.println("Ingen film fundet.");
            return;
        }

        for (Movie movie : matches) {
            printMovie(movie);
        }
    }

    private void editMovie() {
        Movie movie = selectMovie();

        if (movie == null) {
            return;
        }

        System.out.println("Tryk Enter for at beholde den gamle værdi.");

        System.out.print("Titel (" + movie.getTitle() + "): ");
        String title = scanner.nextLine();
        if (title.isBlank()) {
            title = movie.getTitle();
        }

        System.out.print("Instruktør (" + movie.getDirector() + "): ");
        String director = scanner.nextLine();
        if (director.isBlank()) {
            director = movie.getDirector();
        }

        int year = readOptionalInt(
                "År (" + movie.getYearCreated() + "): ",
                movie.getYearCreated()
        );

        boolean inColor = readOptionalYesNo(
                "Farver (ja/nej, Enter = behold): ",
                movie.isInColor()
        );

        int length = readOptionalInt(
                "Længde (" + movie.getLengthInMinutes() + "): ",
                movie.getLengthInMinutes()
        );

        System.out.print("Genre (" + movie.getGenre() + "): ");
        String genre = scanner.nextLine();
        if (genre.isBlank()) {
            genre = movie.getGenre();
        }

        try {
            boolean success = controller.editMovie(movie, title,
                    director, year, inColor, length, genre);

            if (success) {
                System.out.println("Filmen er blevet opdateret!");
            } else {
                System.out.println("Filmen kunne ikke opdateres.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println(
                    "Filmen blev ikke opdateret: " + e.getMessage()
            );
        }
    }

    private void deleteMovie() {
        Movie movie = selectMovie();

        if (movie == null) {
            return;
        }

        boolean answer = readYesNo(
                "Er du sikker på, at du vil slette filmen? (ja/nej): "
        );

        if (answer) {
            if (controller.deleteMovie(movie)) {
                System.out.println("Filmen er blevet slettet!");
            } else {
                System.out.println("Filmen kunne ikke slettes.");
            }
        } else {
            System.out.println("Sletning annulleret.");
        }
    }

    private Movie selectMovie() {
        System.out.print("Søg efter filmens titel: ");
        String searchText = scanner.nextLine();

        ArrayList<Movie> matches =
                controller.searchMovies(searchText);

        if (matches.isEmpty()) {
            System.out.println("Ingen film fundet.");
            return null;
        }

        for (int i = 0; i < matches.size(); i++) {
            System.out.print((i + 1) + ". ");
            printMovie(matches.get(i));
        }

        int choice = readInt(
                "Vælg filmens nummer (0 for at annullere): "
        );

        while (choice < 0 || choice > matches.size()) {
            System.out.println(
                    "Vælg et nummer mellem 1 og "
                            + matches.size()
                            + " eller 0 for at fortryde."
            );

            choice = readInt(
                    "Vælg filmens nummer (0 for at annullere): "
            );
        }

        if (choice == 0) {
            System.out.println("Handlingen blev annulleret.");
            return null;
        }

        return matches.get(choice - 1);
    }

    private void printMovie(Movie movie) {
        String color = movie.isInColor() ? "farver" : "sort-hvid";

        System.out.println(
                movie.getTitle() + " | " +
                        movie.getDirector() + " | " +
                        movie.getYearCreated() + " | " +
                        color + " | " +
                        movie.getLengthInMinutes() + " min | " +
                        movie.getGenre()
        );

        if (movie.hasBeenWatched()) {
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd-MM-yyyy");

            String date = movie.getLastWatched().format(formatter);
            long days = movie.getDaysSinceWatched(LocalDate.now());

            System.out.println("Sidst set: " + date
                    + " (for " + days + " dage siden)");
        } else {
            System.out.println("Sidst set: aldrig");
        }

        System.out.println();
    }

    private void markMovieAsWatched() {
        Movie movie = selectMovie();

        if (movie == null) {
            return;
        }

        LocalDate date = readDate(
                "Hvornår så du den? (dd-mm-åååå, Enter = i dag): "
        );

        try {
            controller.markAsWatched(movie, date);

            System.out.println(
                    movie.getTitle() + " er markeret som set "
                            + date.format(dateFormatter) + "."
            );

        } catch (IllegalArgumentException e) {
            System.out.println(
                    "Filmen blev ikke markeret som set: "
                            + e.getMessage()
            );
        }
    }

    private void showMoviesNotWatchedForDays() {
        int days = readInt("Hvor mange dage siden mindst? ");

        while (days < 0) {
            System.out.println("Antal dage må ikke være negativt.");
            days = readInt("Hvor mange dage siden mindst? ");
        }

        ArrayList<Movie> movies =
                controller.getMoviesNotWatchedForDays(days);

        if (movies.isEmpty()) {
            System.out.println("Ingen film fundet.");
            return;
        }

        System.out.println("Film, du ikke har set i mere end "
                + days + " dage eller aldrig har set:");

        for (Movie movie : movies) {
            printMovie(movie);
        }
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(
                        " " + input + " er ikke et helt tal. Prøv igen."
                );
            }
        }
    }

    private boolean readYesNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("ja")
                    || input.equalsIgnoreCase("j")) {
                return true;
            }

            if (input.equalsIgnoreCase("nej")
                    || input.equalsIgnoreCase("n")) {
                return false;
            }

            System.out.println("Svar ja eller nej.");
        }
    }

    private int readOptionalInt(String prompt, int oldValue) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return oldValue;
            }

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(
                        " " + input + "  er ikke et helt tal. Prøv igen."
                );
            }
        }
    }

    private boolean readOptionalYesNo(String prompt, boolean oldValue) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return oldValue;
            }

            if (input.equalsIgnoreCase("ja")
                    || input.equalsIgnoreCase("j")) {
                return true;
            }

            if (input.equalsIgnoreCase("nej")
                    || input.equalsIgnoreCase("n")) {
                return false;
            }

            System.out.println("Svar ja eller nej, eller tryk Enter for at beholde den gamle værdi.");
        }
    }
    private LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return LocalDate.now();
            }

            try {
                return LocalDate.parse(input, dateFormatter);

            } catch (DateTimeParseException e) {
                System.out.println(
                        " " + input
                                + " er ikke en dato på formen "
                                + "dd-mm-åååå. Prøv igen."
                );
            }
        }
    }
    private void showMoviesSorted() {
        System.out.println("\nSortér efter:");
        System.out.println("1. Titel");
        System.out.println("2. Instruktør");
        System.out.println("3. Årstal");
        System.out.println("4. Længde");
        System.out.println("5. Genre");
        System.out.println("0. Annuller");

        int choice = readInt("Vælg: ");

        if (choice == 0) {
            return;
        }

        SortField field;

        switch (choice) {
            case 1 -> field = SortField.TITLE;
            case 2 -> field = SortField.DIRECTOR;
            case 3 -> field = SortField.YEAR;
            case 4 -> field = SortField.LENGTH;
            case 5 -> field = SortField.GENRE;
            default -> {
                System.out.println("Ugyldigt valg.");
                return;
            }
        }

        ArrayList<Movie> movies = controller.getMoviesSortedBy(field);

        if (movies.isEmpty()) {
            System.out.println("Filmsamlingen er tom.");
            return;
        }

        for (Movie movie : movies) {
            System.out.println();
            System.out.println(movie.getTitle()
                    + " (" + movie.getYearCreated() + ")");
            System.out.println("  Instruktør: " + movie.getDirector());
            System.out.println("  Genre: " + movie.getGenre());
            System.out.println("  Længde: " + movie.getLengthInMinutes()
                    + " minutter, "
                    + (movie.isInColor() ? "farver" : "sort-hvid"));

            if (movie.hasBeenWatched()) {
                System.out.println("  Sidst set: " + movie.getLastWatched());
            } else {
                System.out.println("  Sidst set: aldrig");
            }
        }
    }
}
