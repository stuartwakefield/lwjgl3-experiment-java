package engine.renderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.*;
import java.nio.ByteBuffer;

public class Shader {

    private final int shader;

    public Shader(int shader) {
        this.shader = shader;
    }

    private void appendSource(ByteBuffer buffer, ByteBuffer lengthBuffer) {
        GL20.glShaderSource(this.shader, 1, buffer, lengthBuffer);
    }

    private void appendSource(String result) {
        GL20.glShaderSource(this.shader, result);
    }

    public void compile() {

        GL20.glCompileShader(this.shader);

        if (GL20.glGetShaderi(this.shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
            throw new IllegalStateException("Could not compile shader: " + GL20.glGetShaderInfoLog(this.shader));
    }

    public void attachTo(ShaderProgram program) {
        program.attach(this.shader);
    }

    public void close() {
        GL20.glDeleteShader(this.shader);
    }

    public static Shader loadFromResourceFile(String filePath, ShaderType type) {

        InputStream stream = Shader.class.getClassLoader().getResourceAsStream(filePath);
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[1024];

        Shader shader = null;

        try (Reader in = new InputStreamReader(stream, "UTF-8")) {

            while (true) {
                int count = in.read(buffer, 0, buffer.length);
                if (count < 0)
                    break;
                builder.append(buffer, 0, count);
            }

            String result = builder.toString();
            shader = type.create();
            shader.appendSource(result);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return shader;

    }

}
