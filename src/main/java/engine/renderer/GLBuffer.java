package engine.renderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class GLBuffer {

    private final Integer id;

    public GLBuffer(Integer id) {
        this.id = id;
    }

    public void setStaticDrawData(FloatBuffer buffer) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.id);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    public void setFloatPointer(GLAttribute attribute, Integer count) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.id); // model.vbo
        attribute.setFloatPointer(count);

    }

    public static GLBuffer create() {
        return new GLBuffer(GL15.glGenBuffers());
    }

    public void destroy() {

    }
}
