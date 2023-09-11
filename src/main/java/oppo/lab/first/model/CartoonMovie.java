package oppo.lab.first.model;

public class CartoonMovie extends Film {
    private AnimationType animationType;

    public AnimationType getAnimationType() {
        return animationType;
    }

    public void setAnimationType(AnimationType animationType) {
        this.animationType = animationType;
    }

    public CartoonMovie() {}

    public CartoonMovie(String name, String animationType) {
        super.setName(name);
        this.animationType = AnimationType.valueOf(animationType.toUpperCase());
    }

    public CartoonMovie(String name, AnimationType animationType) {
        super.setName(name);
        this.animationType = animationType;
    }

    public enum AnimationType {
        DRAWN("Рисованный"),
        PUPPET("Кукольный"),
        CLAYMATION("Пластилиновый");

        private final String displayName;

        AnimationType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public static AnimationType fromDisplayName(String displayName) {
            for (AnimationType animationType : values()) {
                if (animationType.getDisplayName().equals(displayName)) {
                    return animationType;
                }
            }
            throw new IllegalArgumentException("No enum constant with display name " + displayName);
        }
    }

    @Override
    public String toString() {
        return "CartoonMovie{" +
                "name='" + super.getName() + '\'' +
                ", animationType=" + animationType.getDisplayName() +
                '}';
    }
}
