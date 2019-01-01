package engine.renderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class GLAttribute {

    private final Integer id;

    public GLAttribute(Integer id) {
        this.id = id;
    }

    public void setFloatPointer(Integer count) {
        GL20.glVertexAttribPointer(this.id, count, GL11.GL_FLOAT, false, 0, 0);
    }

    public void enable() {
        GL20.glEnableVertexAttribArray(this.id);
    }

    public void disable() {
        GL20.glDisableVertexAttribArray(this.id);
    }
}
