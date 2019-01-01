package entities;

import engine.Color;
import engine.geometry.Vec3;

public class Light {

    private final Vec3 direction;
    private final Color color;
    private final float ambience;
    private final float diffusion;
    private final LightSpecularity specularity;

    public Light(Vec3 direction, Color color, float ambience, float diffusion, LightSpecularity specularity) {
        this.direction = direction;
        this.color = color;
        this.ambience = ambience;
        this.diffusion = diffusion;
        this.specularity = specularity;
    }

    public Vec3 getDirection() {
        return direction;
    }

    public Color getColor() {
        return color;
    }

    public float getAmbience() {
        return ambience;
    }

    public float getDiffusion() {
        return diffusion;
    }

    public LightSpecularity getSpecularity() {
        return specularity;
    }

}
