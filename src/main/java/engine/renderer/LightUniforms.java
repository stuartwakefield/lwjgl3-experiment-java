package engine.renderer;

import org.lwjgl.opengl.GL20;

public class LightUniforms {

    private final int directionUniformId;
    private final int colorUniformId;
    private final int ambienceUniformId;
    private final int diffusionUniformId;
    private final LightSpecularityUniforms specularity;

    public LightUniforms(int uniformId, int uniformId1, int uniformId2, int uniformId3, LightSpecularityUniforms lightSpecularityUniforms) {
        this.directionUniformId = uniformId;
        this.colorUniformId = uniformId1;
        this.ambienceUniformId = uniformId2;
        this.diffusionUniformId = uniformId3;
        this.specularity = lightSpecularityUniforms;
    }

    public void write(Light light) {
        GL20.glUniform3fv(this.directionUniformId, Buffer.floatBuffer(light.direction.values));
        GL20.glUniform3fv(this.colorUniformId, Buffer.floatBuffer(light.color.values));
        GL20.glUniform1f(this.ambienceUniformId, light.ambience);
        GL20.glUniform1f(this.diffusionUniformId, light.diffusion);
        this.specularity.write(light.specularity);
    }

}
