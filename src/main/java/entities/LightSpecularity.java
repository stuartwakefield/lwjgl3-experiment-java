package entities;

public class LightSpecularity {

    private final float intensity;
    private final float power;

    public LightSpecularity(float intensity, float power) {
        this.intensity = intensity;
        this.power = power;
    }

    public float getIntensity() {
        return intensity;
    }

    public float getPower() {
        return power;
    }

}
