package engine.renderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.util.HashSet;
import java.util.Set;

public class ShaderProgram {

    private final int program;
    private Set<Shader> shaders;

    public ShaderProgram() {
        this.shaders = new HashSet<Shader>();
        this.program = GL20.glCreateProgram();
    }

    public void attach(Shader shader) {
        shader.attachTo(this);
    }

    public void attach(int shader) {

        if (this.shaders.contains(shader))
            throw new IllegalArgumentException("Shader is already attached");

        GL20.glAttachShader(this.program, shader);

    }

    public void detach(Shader shader) {

    }

    public void link() {
        GL20.glLinkProgram(this.program);

        if (GL20.glGetProgrami(this.program, GL20.GL_LINK_STATUS) == GL11.GL_FALSE)
            throw new IllegalStateException("Could not link program: " + GL20.glGetProgramInfoLog(this.program));
    }

    public void validate() {
        GL20.glValidateProgram(this.program);

        if (GL20.glGetProgrami(this.program, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE)
            throw new IllegalStateException("Could not validate program: " + GL20.glGetProgramInfoLog(this.program));
    }

    public void use() {
        GL20.glUseProgram(this.program);
    }

    public void close() {
        GL20.glDeleteProgram(this.program);
    }

    public int getUniformId(String name) {
        int result = GL20.glGetUniformLocation(this.program, name);

        if (result == -1)
            throw new IllegalStateException("Could not find uniform: " + name);

        return result;
    }

    public int getAttributeId(String name) {
        int result = GL20.glGetAttribLocation(this.program, name);

        if (result == -1)
            throw new IllegalStateException("Could not find attribute: " + name);

        return result;
    }
}
