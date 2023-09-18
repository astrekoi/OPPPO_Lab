package oppo.lab.first.controller;

import oppo.lab.first.common.CircularLinkedList;
import oppo.lab.first.exceptions.InvalidAnimationTypeException;
import oppo.lab.first.model.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class FilmController {
    private final CircularLinkedList<Film> films;
    private BufferedReader reader;
    private String workPath;

    public FilmController(CircularLinkedList<Film> films) {
        this.films = films;
    }

    public FilmController() {
        this.films = new CircularLinkedList<>();
    }

    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    public void processFile(String fileName) {
        try {
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
                    case "REM" -> {
                        if (parts.length > 2) removeFilms(parts[1]);
                    }
                    case "PRINT" -> printFilms(fileName);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void logError(Exception e) {
        String timeStamp = new SimpleDateFormat("dd-HH-mm").format(new Date());
        String logFileName = this.workPath == null || this.workPath.isEmpty() ?
                useDefaultWorkPath() + "logs\\" + timeStamp + ".txt" :
                getWorkPath() + "logs\\" + timeStamp + ".txt";
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
        String logFileName = this.workPath == null || this.workPath.isEmpty() ?
                useDefaultWorkPath() + "logs\\" + timeStamp + ".txt" :
                getWorkPath() + "logs\\" + timeStamp + ".txt";
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

    public void addFilm(String data) throws InvalidAnimationTypeException {
        Film film = parseFilm(data);
        if (film != null) {
            films.add(film);
        }
    }

    private Film parseFilm(String data) throws InvalidAnimationTypeException {
        String[] parts = data.split(", ");
        final int TYPE_INDEX = 0;
        if (parts.length < 2) {
            logError("Invalid ADD");
            return null;
        }
        String type = parts[TYPE_INDEX];
        Film film = null;
        if (type.equals(FilmType.FEATURE_FILM.getDisplayName())) {
            film = addFeatureFilm(parts);
        } else if (type.equals(FilmType.TV_SERIES.getDisplayName())) {
            film = addTVSeries(parts);
        } else if (type.equals(FilmType.CARTOON_MOVIE.getDisplayName())) {
            film = addCartoonMovie(parts);
        }
        return film;
    }

    private FeatureFilm addFeatureFilm(String[] parts) {
        final int NAME_INDEX = 1;
        final int DIRECTOR_NAME_INDEX = 2;
        FeatureFilm featureFilm = new FeatureFilm();
        featureFilm.setName(parts[NAME_INDEX]);
        featureFilm.setDirectorName(parts[DIRECTOR_NAME_INDEX]);
        return featureFilm;
    }

    private TVSeries addTVSeries(String[] parts) {
        final int NAME_INDEX = 1;
        final int DIRECTOR_NAME_INDEX = 2;
        final int NUMBER_OF_EPISODES_INDEX = 3;
        TVSeries tvSeries = new TVSeries();
        tvSeries.setName(parts[NAME_INDEX]);
        tvSeries.setDirectorName(parts[DIRECTOR_NAME_INDEX]);
        tvSeries.setNumberOfEpisodes(Integer.parseInt(parts[NUMBER_OF_EPISODES_INDEX]));
        return tvSeries;
    }

    private CartoonMovie addCartoonMovie(String[] parts) throws InvalidAnimationTypeException {
        final int NAME_INDEX = 1;
        final int ANIMATION_TYPE_INDEX = 2;
        CartoonMovie cartoonMovie = new CartoonMovie();
        cartoonMovie.setName(parts[NAME_INDEX]);
        try {
            cartoonMovie.setAnimationType(CartoonMovie.AnimationType.fromDisplayName(parts[ANIMATION_TYPE_INDEX]));
        } catch (IllegalArgumentException e) {
            throw new InvalidAnimationTypeException("Invalid animation type: " + parts[ANIMATION_TYPE_INDEX]);
        }
        return cartoonMovie;
    }

    public void removeFilms(String condition) {
        String[] parts = condition.split(" ");
        if (parts.length < 2) {
            logError("Invalid REM");
            return;
        }
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

    private void printFilms(String fileName) {
        String outputFileName = fileName;
        String outputPath = this.workPath == null || this.workPath.isEmpty() ?
                useDefaultWorkPath() + "output\\" + outputFileName :
                getWorkPath() + "output\\" + outputFileName;
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

    public String getWorkPath() {
        return System.getProperty("user.dir") + workPath;
    }

    public void setWorkPath(String path) {
        this.workPath = path;
    }

    public String useDefaultWorkPath(){
        return System.getProperty("user.dir") + "\\src\\main\\java\\oppo\\lab\\first\\";
    }
}
