package engine.renderer;

import engine.geometry.Mat4;
import engine.geometry.Vec3;

import static engine.geometry.Mat4.*;
import static engine.geometry.Mat4.mult;

public class Camera {

    private final Vec3 eye;
    private final Vec3 center;
    private final Vec3 up;

    public Camera(Vec3 eye, Vec3 center, Vec3 up) {
        this.eye = eye;
        this.center = center;
        this.up = up;
    }


    public Mat4 getViewProjection(int width, int height) {

        Mat4 scale = scale(0.5f);
        Mat4 lookAt = lookAt(eye, center, up);
        Mat4 perspective = perspective((float) Math.PI / 6, width / height, 0.1f, 1000);

        Mat4 view = mult(lookAt, scale);
        Mat4 projection = perspective;

        Mat4 vp = mult(projection, view);

        return vp;

    }

    public Vec3 getEye() {
        return this.eye;
    }
}
