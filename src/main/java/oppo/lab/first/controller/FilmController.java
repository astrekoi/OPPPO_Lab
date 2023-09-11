package oppo.lab.first.controller;

import oppo.lab.first.common.CircularLinkedList;
import oppo.lab.first.exceptions.InvalidAnimationTypeException;
import oppo.lab.first.model.Film;
import oppo.lab.first.model.FeatureFilm;
import oppo.lab.first.model.TVSeries;
import oppo.lab.first.model.CartoonMovie;
import oppo.lab.first.model.FilmType;

import java.io.*;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class FilmController {
    private final CircularLinkedList<Film> films = new CircularLinkedList<>();

    public void processFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ", 2);
                String command = parts[0];
                switch (command) {
                    case "ADD" -> {
                        try {
                            addFilm(parts[1]);
                        } catch (InvalidAnimationTypeException e) {
                            logError(e);
                        }
                    }
                    case "REM" -> removeFilms(parts[1]);
                    case "PRINT" -> printFilms(fileName);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void logError(Exception e) {
        String timeStamp = new SimpleDateFormat("dd-HH-mm").format(new Date());
        String logFileName = getWorkPath() + "logs\\" + timeStamp + ".txt";
        try (FileWriter fw = new FileWriter(logFileName, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            String errorTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
            String errorMessage = errorTime + " Ошибка: " + e.getMessage();
            out.println(errorMessage);
            out.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void logError(String e) {
        String timeStamp = new SimpleDateFormat("dd-HH-mm").format(new Date());
        String logFileName = getWorkPath() + "logs\\" + timeStamp + ".txt";
        try (FileWriter fw = new FileWriter(logFileName, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            String errorTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
            String errorMessage = errorTime + " Ошибка: " + e;
            out.println(errorMessage);
            out.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void addFilm(String data) {
        String[] parts = data.split(", ");
        if (parts.length < 2) {
            logError("Ошибка в добавлении");
            return;
        }
        String type = parts[0];
        String name = parts[1];
        Film film = null;
        if (type.equals(FilmType.FEATURE_FILM.getDisplayName())) {
            FeatureFilm featureFilm = new FeatureFilm();
            featureFilm.setName(name);
            featureFilm.setDirectorName(parts[2]);
            film = featureFilm;
        } else if (type.equals(FilmType.TV_SERIES.getDisplayName())) {
            TVSeries tvSeries = new TVSeries();
            tvSeries.setName(name);
            tvSeries.setDirectorName(parts[2]);
            tvSeries.setNumberOfEpisodes(Integer.parseInt(parts[3]));
            film = tvSeries;
        } else if (type.equals(FilmType.CARTOON_MOVIE.getDisplayName())) {
            CartoonMovie cartoonMovie = new CartoonMovie();
            cartoonMovie.setName(name);
            try {
                cartoonMovie.setAnimationType(CartoonMovie.AnimationType.fromDisplayName(parts[2]));
            } catch (IllegalArgumentException e) {
                throw new InvalidAnimationTypeException("Invalid animation type: " + parts[2]);
            }
            film = cartoonMovie;
        }
        if (film != null) {
            films.add(film);
        }
    }

    private void removeFilms(String condition) {
        String[] parts = condition.split(" ");
        String field = parts[0];
        String operator = parts[1];
        String value = parts[2];

        for (int i = 0; i < films.size(); i++) {
            Film film = films.remove();
            boolean toRemove = false;
            if (field.equals("название")) {
                if (operator.equals("has")) {
                    toRemove = film.getName().contains(value);
                } else if (operator.equals("hasnt")) {
                    toRemove = !film.getName().contains(value);
                }
            }
            if (!toRemove) {
                films.add(film);
            }
        }
    }

    private void printFilms(String filename) {
        String outputFileName = Paths.get(filename).getFileName().toString();
        String outputPath = getWorkPath() + "output\\"  + outputFileName;
        try (FileWriter fw = new FileWriter(outputPath, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            out.println(currentTime + " PRINT:");
            for (int i = 0; i < films.size(); i++) {
                Film film = films.remove();
                out.println(film);
                films.add(film);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private String getWorkPath() {
        return  System.getProperty("user.dir") + "\\src\\main\\java\\oppo\\lab\\first\\";
    }
}
