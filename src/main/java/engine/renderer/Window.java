package engine.renderer;

import engine.input.Cursor;
import engine.input.CursorPos;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;

import java.nio.IntBuffer;

public class Window {

    public final Long id;

    public Window(Long id) {
        this.id = id;
    }

    public void close() {
        GLFW.glfwDestroyWindow(this.id);
    }

    public FrameSize getWindowSize() {

        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);

        GLFW.glfwGetWindowSize(this.id, widthBuffer, heightBuffer);

        int width = widthBuffer.get(0);
        int height = heightBuffer.get(0);

        return new FrameSize(width, height);

    }

    public FrameSize getFramebufferSize() {

        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);

        GLFW.glfwGetFramebufferSize(this.id, widthBuffer, heightBuffer);

        int width = widthBuffer.get(0);
        int height = heightBuffer.get(0);

        return new FrameSize(width, height);
    }

    public CursorPos getCursorPosition() {
        return new Cursor(this.id).pos();
    }

    public Window setCentered() {

        GLFWVidMode mode = PrimaryMonitor.getVideoMode();
        FrameSize size = getWindowSize();

        // Place window in the center of the screen
        GLFW.glfwSetWindowPos(
                this.id,
                (mode.width() - size.width()) / 2,
                (mode.height() - size.height()) / 2
        );

        return this;
    }

    public static Window create(Integer width, Integer height, String title) {

        if (GLFW.glfwInit() == GLFW.GLFW_FALSE)
            throw new IllegalStateException("Could not initialize GLFW");

        GLFW.glfwDefaultWindowHints();

        // Turn on multisample anti-aliasing
        GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 4);

        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);

        Integer monitor = 0;
        Integer share = 0;

        Long window = GLFW.glfwCreateWindow(width, height, title, monitor, share);
        if (window == 0)
            throw new RuntimeException("Failed to create a GLFW window");

        return new Window(window);
    }

    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(this.id) == GLFW.GLFW_FALSE;
    }

    public void swapBuffers() {
        GLFW.glfwSwapBuffers(this.id);
    }

    public void setGLFWCallback(GLFWKeyCallback callback) {
        GLFW.glfwSetKeyCallback(this.id, callback);
    }

    public void setShouldClose(boolean value) {
        GLFW.glfwSetWindowShouldClose(this.id, value ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }
}
