package oppo.lab.first.model;

public class TVSeries extends Film {
    private String directorName;
    private Integer numberOfEpisodes;

    public TVSeries() {}

    public TVSeries(String name, String directorName, Integer numberOfEpisodes) {
        super.setName(name);
        this.directorName = directorName;
        this.numberOfEpisodes = numberOfEpisodes;
    }

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }

    public Integer getNumberOfEpisodes() {
        return numberOfEpisodes;
    }

    public void setNumberOfEpisodes(Integer numberOfEpisodes) {
        this.numberOfEpisodes = numberOfEpisodes;
    }

    @Override
    public String toString() {
        return "TVSeries{" +
                "name='" + super.getName() + '\'' +
                ", directorName='" + directorName + '\'' +
                ", numberOfEpisodes=" + numberOfEpisodes +
                '}';
    }
}
