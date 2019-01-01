package engine;

import engine.geometry.Vec3;

public class Color {

    private final float r;
    private final float g;
    private final float b;

    public Color(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Vec3 toVec3() {
        return new Vec3(this.r, this.g, this.b);
    }

    public static Color rgb(int r, int g, int b) {
        return new Color(r / 255, g / 255, b / 255);
    }

}
