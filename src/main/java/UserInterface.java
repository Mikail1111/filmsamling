import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserInterface
{
    private Controller controller;
    private Scanner scanner;

    public UserInterface() {
        controller = new Controller();
        scanner = new Scanner(System.in);
    }

    public void startProgram() {
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
                case "0":
                    System.out.println("Programmet afsluttes.");
                    running = false;
                    break;
                default:
                    System.out.println("Ugyldigt valg. Prøv igen.");
            }
        }
    }

    private void showMenu() {
        System.out.println("--- FILMSAMLING ---");
        System.out.println("1. Opret film");
        System.out.println("2. Vis alle film");
        System.out.println("3. Søg efter film");
        System.out.println("4. Rediger film");
        System.out.println("5. Slet film");
        System.out.println("6. Markér en film som set i dag");
        System.out.println("7. Find film, du ikke har set længe");
        System.out.println("0. Afslut");
        System.out.print("Vælg: ");
    }

    private void createMovie() {
        System.out.print("Titel: ");
        String title = scanner.nextLine();

        System.out.print("Instruktør: ");
        String director = scanner.nextLine();

        System.out.print("Udgivelsesår: ");
        int year = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Er filmen i farver? (ja/nej): ");
        boolean inColor = scanner.nextLine().trim()
                .equalsIgnoreCase("ja");

        System.out.print("Længde i minutter: ");
        int length = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Genre: ");
        String genre = scanner.nextLine();

        controller.addMovie(title, director, year,
                inColor, length, genre);

        System.out.println("Filmen er blevet tilføjet!");
    }

    private void showAllMovies() {
        ArrayList<Movie> movies = controller.getMovies();

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

        System.out.print("År (" + movie.getYearCreated() + "): ");
        String yearInput = scanner.nextLine().trim();
        int year = yearInput.isEmpty()
                ? movie.getYearCreated()
                : Integer.parseInt(yearInput);

        System.out.print("Farver (ja/nej): ");
        String colorInput = scanner.nextLine().trim();
        boolean inColor = colorInput.isEmpty()
                ? movie.isInColor()
                : colorInput.equalsIgnoreCase("ja");

        System.out.print("Længde (" + movie.getLengthInMinutes() + "): ");
        String lengthInput = scanner.nextLine().trim();
        int length = lengthInput.isEmpty()
                ? movie.getLengthInMinutes()
                : Integer.parseInt(lengthInput);

        System.out.print("Genre (" + movie.getGenre() + "): ");
        String genre = scanner.nextLine();
        if (genre.isBlank()) {
            genre = movie.getGenre();
        }

        boolean success = controller.editMovie(movie, title,
                director, year, inColor, length, genre);

        if (success) {
            System.out.println("Filmen er blevet opdateret!");
        } else {
            System.out.println("Filmen kunne ikke opdateres.");
        }
    }

    private void deleteMovie() {
        Movie movie = selectMovie();

        if (movie == null) {
            return;
        }

        System.out.print("Er du sikker på, at du vil slette filmen? (ja/nej): ");
        String answer = scanner.nextLine().trim();

        if (answer.equalsIgnoreCase("ja")) {
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

        System.out.print("Vælg filmens nummer (0 for at annullere): ");
        int choice = Integer.parseInt(scanner.nextLine().trim());

        if (choice == 0) {
            return null;
        }

        if (choice < 1 || choice > matches.size()) {
            System.out.println("Ugyldigt filmnummer.");
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

        controller.markAsWatched(movie);

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MM-yyyy");

        System.out.println(movie.getTitle()
                + " er markeret som set "
                + movie.getLastWatched().format(formatter) + ".");
    }

    private void showMoviesNotWatchedForDays() {
        System.out.print("Hvor mange dage siden mindst? ");
        int days = Integer.parseInt(scanner.nextLine().trim());

        if (days < 0) {
            System.out.println("Antal dage må ikke være negativt.");
            return;
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
}
