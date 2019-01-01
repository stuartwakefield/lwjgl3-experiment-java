package engine;

import engine.input.CursorPos;
import engine.modelling.Cuboid;
import engine.modelling.Model;
import engine.modelling.Pyramid;
import engine.renderer.*;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Setup {

    private static Logger logger = LoggerFactory.getLogger(Setup.class);

    private final Integer width;
    private final Integer height;

    private Window window;
    private Renderer renderer;

    public Setup(Integer width, Integer height) {
        this.width = width;
        this.height = height;
    }

    public void init() {

        System.setProperty("java.awt.headless", "true");

        GLFWErrorCallback.createPrint(System.err).set();

        this.window = Window.create(this.width, this.height, "LWJGL3 Experiment");

        window.setGLFWCallback(new GLFWKeyCallback() {

            @Override
            public void invoke(long windowId, int keyCode, int scanCode, int action, int modifiers) {
                if (keyCode == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE)
                    window.setShouldClose(true);
            }

        });

        window.setCentered();
        FrameSize size = window.getWindowSize();

        GLFWVidMode mode = PrimaryMonitor.getVideoMode();

        logger.info("Vidmode width: {} px, height: {} px", mode.width(), mode.height());
        logger.info("Window width: {} px, height: {} px", size.width(), size.height());

        GLFW.glfwMakeContextCurrent(this.window.id);
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(this.window.id);
        GLFW.glfwSetInputMode(this.window.id, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

        CursorPos pos = window.getCursorPosition();

        logger.info("Starting cursor: {}, {}", pos.left, pos.top);

        GL.createCapabilities();

        Map<String, Model> models = new HashMap<>();

        models.put("cube", Cuboid.getModel());
        models.put("pyramid", Pyramid.getModel());

        renderer = new Renderer(models);

    }

    public GameLoop loop() {
        return new GameLoop(this.window, this.width, this.height, this.renderer);
    }

    public void close() {

        this.renderer.close();
        this.window.close();

        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null);
    }

}
