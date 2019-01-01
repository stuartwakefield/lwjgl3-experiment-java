package engine.renderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

public class TextureBuffer {

    private final Integer id;

    public TextureBuffer(Integer id) {
        this.id = id;
    }

    public void bind(GLUniform sampler) {
        bind(sampler, 0);
    }

    public void bind(GLUniform sampler, int unit) {
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + unit);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.id);
        sampler.setInt(unit);
    }

}
