package oppo.lab.first.model;

public enum FilmType {
    CARTOON_MOVIE("Мультфильм"),
    FEATURE_FILM("Игровой фильм"),
    TV_SERIES("Сериал");

    private final String displayName;

    FilmType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
