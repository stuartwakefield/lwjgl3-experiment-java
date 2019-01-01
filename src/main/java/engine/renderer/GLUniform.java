package engine.renderer;

import engine.geometry.Mat4;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;

public class GLUniform {

    private final Integer id;

    public GLUniform(Integer id) {
        this.id = id;
    }

    public void setFloatMatrix4(Mat4 matrix) {
        setFloatMatrix4(matrix.toBuffer());
    }

    public void setFloatMatrix4(FloatBuffer floatBuffer) {
        GL20.glUniformMatrix4fv(this.id, false, floatBuffer);
    }

    public void setInt(Integer value) {
        GL20.glUniform1i(this.id, value);
    }

    public void setFloat2(float w, float h) {
        GL20.glUniform2f(this.id, w, h);
    }
}
