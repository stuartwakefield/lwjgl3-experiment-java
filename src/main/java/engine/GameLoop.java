package engine;

import engine.input.CursorPos;
import engine.instrumentation.FrameTimeSampler;
import engine.instrumentation.Sampler;
import engine.renderer.FrameSize;
import engine.renderer.Renderer;
import engine.renderer.Window;
import entities.Entity;
import entities.Scene;
import entities.World;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameLoop {

    private final Renderer renderer;
    private Window window;
    private int width;
    private int height;
    private static Logger logger = LoggerFactory.getLogger(GameLoop.class);

    public GameLoop(Window window, int width, int height, Renderer renderer) {
        this.window = window;
        this.width = width;
        this.height = height;
        this.renderer = renderer;
    }

    public void start() {

        FrameSize size = this.window.getFramebufferSize();

        logger.info("Framebuffer size: width: {} px, height: {} px", size.width(), size.height());

        Sampler cursorSampler = new Sampler(0.01, () -> {
            CursorPos pos = window.getCursorPosition();
            logger.info("Cursor: {}, {}", pos.left, pos.top);
        });

        FrameTimeSampler frameTimeSampler = new FrameTimeSampler(1, event -> {
            logger.info("Total frames: {}", event.totalFrameCount);
            logger.info("Frame rate: {} ms ({} FPS)", event.frameTime * 1000, event.frameRate);
        });

        renderer.init();

        World world = new World();

        frameTimeSampler.start();
//        cursorSampler.start();

        while (this.window.shouldClose()) {

            frameTimeSampler.next();
//            cursorSampler.next();

            Scene scene = world.update(window);
            renderer.render(scene, window);

            window.swapBuffers();

            GLFW.glfwPollEvents();

        }

    }

}
