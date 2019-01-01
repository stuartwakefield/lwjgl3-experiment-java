package engine.renderer;

import org.lwjgl.opengl.GL20;

public enum ShaderType {

    VERTEX(GL20.GL_VERTEX_SHADER),
    FRAGMENT(GL20.GL_FRAGMENT_SHADER);

    protected final int type;

    ShaderType(int type) {
        this.type = type;
    }

    public Shader create() {

        int shader = GL20.glCreateShader(this.type);

        return new Shader(shader);
    }
}
