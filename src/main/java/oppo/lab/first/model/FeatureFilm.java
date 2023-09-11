package oppo.lab.first.model;

public class FeatureFilm extends Film {
    private String directorName;

    public FeatureFilm() {}

    public FeatureFilm(String name, String directorName) {
        super.setName(name);
        this.directorName = directorName;
    }

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }

    @Override
    public String toString() {
        return "FeatureFilm{" +
                "name='" + super.getName() + '\'' +
                ", directorName='" + directorName + '\'' +
                '}';
    }
}
