package entities;

public class Camera {

    private final Pos eye;
    private final Pos center;
    private final Pos up;

    public Camera(Pos eye, Pos center, Pos up) {
        this.eye = eye;
        this.center = center;
        this.up = up;
    }

    public Pos getEye() {
        return this.eye;
    }

    public Pos getCenter() {
        return this.center;
    }

    public Pos getUp() {
        return this.up;
    }

}
