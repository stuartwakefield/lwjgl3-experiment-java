package engine.renderer;

import engine.geometry.Vec3;

public class Light {

    public final Vec3 direction;
    public final Vec3 color;
    public final float ambience;
    public final float diffusion;
    public final LightSpecularity specularity;

    public Light(Vec3 direction, Vec3 color, float ambience, float diffusion, LightSpecularity specularity) {
        this.direction = direction;
        this.color = color;
        this.ambience = ambience;
        this.diffusion = diffusion;
        this.specularity = specularity;
    }

}
