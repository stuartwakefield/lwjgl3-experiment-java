package engine.renderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import java.nio.IntBuffer;

public class GLElementBuffer {

    private final int id;

    public GLElementBuffer(int id) {
        this.id = id;
    }

    public void setStaticDrawData(IntBuffer buffer) {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.id);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    public void draw(int elementCount) {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.id);
        GL11.glDrawElements(GL11.GL_TRIANGLES, elementCount, GL11.GL_UNSIGNED_INT, 0);
    }

    public static GLElementBuffer create() {
        return new GLElementBuffer(GL15.glGenBuffers());
    }

}
