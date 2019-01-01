package engine.renderer;

import org.lwjgl.opengl.GL20;

public class LightSpecularityUniforms {

    private final int intensityUniformId;
    private final int powerUniformId;

    public LightSpecularityUniforms(int uniformId, int uniformId1) {
        this.intensityUniformId = uniformId;
        this.powerUniformId = uniformId1;
    }

    public void write(LightSpecularity specularity) {
        GL20.glUniform1f(this.intensityUniformId, specularity.intensity);
        GL20.glUniform1f(this.powerUniformId, specularity.power);
    }

}
