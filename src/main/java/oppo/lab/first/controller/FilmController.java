package oppo.lab.first.controller;

import oppo.lab.first.common.CircularLinkedList;
import oppo.lab.first.exceptions.InvalidAnimationTypeException;
import oppo.lab.first.model.Film;
import oppo.lab.first.model.FeatureFilm;
import oppo.lab.first.model.TVSeries;
import oppo.lab.first.model.CartoonMovie;
import oppo.lab.first.model.FilmType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
                            System.err.println(e.getMessage());
                        }
                    }
                    case "REM" -> removeFilms(parts[1]);
                    case "PRINT" -> printFilms();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addFilm(String data) {
        String[] parts = data.split(", ");
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

    private void printFilms() {
        System.out.println("----PRINT----");
        for (int i = 0; i < films.size(); i++) {
            Film film = films.remove();
            System.out.println(film);
            films.add(film);
        }
    }
}
