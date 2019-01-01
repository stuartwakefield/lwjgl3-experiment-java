package engine.renderer;

import engine.geometry.Mat4;
import entities.Entity;
import engine.modelling.Model;
import entities.Pos;
import entities.Scene;
import org.lwjgl.opengl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static engine.geometry.Mat4.*;

public class Renderer {

    private static final float[] QUAD_VERTICES = new float[]{
            -1, 1,
            1, 1,
            1, -1,
            -1, -1
    };
    private static final int[] QUAD_INDICES = new int[]{
            1, 0, 3,
            3, 2, 1
    };

    private static Logger logger = LoggerFactory.getLogger(Renderer.class);
    private final Map<String, Model> models;
    private Map<String, ModelGLBuffers> modelBuffers = new HashMap<>();

    private ShaderProgram program;
    private Shader fragShader;
    private Shader vertexShader;

    private int vao;

    private GLAttribute vertexAttr;
    private GLAttribute normalAttr;
    private GLAttribute textureAttr;
    private GLUniform viewUniform;
    private int modelUniform;
    private int eyeUniform;
    private LightUniforms lightUniforms;
    private GLUniform samplerUniform;
    private TextureBuffer textureBuffer;
    private GLAttribute bloomHighlightVertexPosition;
    private ShaderProgram bloomHighlightProgram;
    private GLUniform bloomHighlightSamplerUniform;
    private ShaderProgram bloomMixProgram;
    private GLUniform bloomMixSceneSamplerUniform;
    private GLUniform bloomMixHighlightSamplerUniform;
    private Shader bloomVertexShader;
    private Shader bloomHighlightFragmentShader;
    private Shader bloomMixFragmentShader;
    private GLAttribute bloomMixVertexPosition;
    private Shader bloomHBlurFragmentShader;
    private ShaderProgram bloomHBlurProgram;
    private GLAttribute bloomHBlurVertexPosition;
    private GLUniform bloomHBlurSamplerUniform;
    private GLUniform bloomHBlurResolutionUniform;
    private Shader bloomVBlurFragmentShader;
    private ShaderProgram bloomVBlurProgram;
    private GLAttribute bloomVBlurVertexPosition;
    private GLUniform bloomVBlurSamplerUniform;
    private GLUniform bloomVBlurResolutionUniform;
    private int sceneFrameBuffer;
    private int sceneTextureBuffer;
    private GLBuffer quadVertexBuffer;
    private GLElementBuffer quadElementBuffer;
    private int sceneRenderBuffer;
    private TextureBuffer sceneTexture;
    private int bloomHighlightFrameBuffer;
    private int bloomHighlightTextureBuffer;
    private TextureBuffer bloomHighlightTexture;
    private int bloomHBlurFramebuffer;
    private int bloomHBlurTextureBuffer;
    private TextureBuffer bloomHBlurTexture;
    private int bloomVBlurFramebuffer;
    private int bloomVBlurTextureBuffer;
    private TextureBuffer bloomVBlurTexture;
    private int bloomHBlur2Framebuffer;
    private int bloomHBlur2TextureBuffer;
    private TextureBuffer bloomHBlur2Texture;
    private int bloomVBlur2Framebuffer;
    private int bloomVBlur2TextureBuffer;
    private TextureBuffer bloomVBlur2Texture;
    private int sceneMultisampleFrameBuffer;
    private int sceneMultisampleColorRenderBuffer;
    private int sceneMultisampleDepthRenderBuffer;

    public Renderer(Map<String, Model> models) {
        this.models = models;
    }

    public void init() {

        Shader vertexShader = Shader.loadFromResourceFile("shaders/basic_vs.glsl", ShaderType.VERTEX);
        vertexShader.compile();

        Shader fragShader = Shader.loadFromResourceFile("shaders/basic_fs.glsl", ShaderType.FRAGMENT);
        fragShader.compile();

        ShaderProgram program = new ShaderProgram();
        program.attach(vertexShader);
        program.attach(fragShader);
        program.link();

        this.vertexShader = vertexShader;
        this.fragShader = fragShader;
        this.program = program;

        this.vertexAttr = new GLAttribute(program.getAttributeId("vertex"));
        this.normalAttr = new GLAttribute(program.getAttributeId("normal"));
        this.textureAttr = new GLAttribute(program.getAttributeId("textureCoord"));

        this.viewUniform = new GLUniform(program.getUniformId("view"));
        this.modelUniform = program.getUniformId("model");

        this.eyeUniform = program.getUniformId("eye");

        this.samplerUniform = new GLUniform(program.getUniformId("sampler"));

        this.lightUniforms = new LightUniforms(
                program.getUniformId("light.direction"),
                program.getUniformId("light.color"),
                program.getUniformId("light.ambience"),
                program.getUniformId("light.diffusion"),
                new LightSpecularityUniforms(
                        program.getUniformId("light.specularity"),
                        program.getUniformId("light.power")
                )
        );

        Shader bloomVertexShader = Shader.loadFromResourceFile("shaders/bloom_vs.glsl", ShaderType.VERTEX);
        bloomVertexShader.compile();

        Shader bloomHighlightFragmentShader = Shader.loadFromResourceFile("shaders/bloom_highlight_fs.glsl", ShaderType.FRAGMENT);
        bloomHighlightFragmentShader.compile();

        Shader bloomMixFragmentShader = Shader.loadFromResourceFile("shaders/bloom_mix_fs.glsl", ShaderType.FRAGMENT);
        bloomMixFragmentShader.compile();

        Shader bloomHBlurFragmentShader = Shader.loadFromResourceFile("shaders/bloom_hblur_fs.glsl", ShaderType.FRAGMENT);
        bloomHBlurFragmentShader.compile();

        Shader bloomVBlurFragmentShader = Shader.loadFromResourceFile("shaders/bloom_vblur_fs.glsl", ShaderType.FRAGMENT);
        bloomVBlurFragmentShader.compile();

        ShaderProgram bloomHighlightProgram = new ShaderProgram();
        bloomHighlightProgram.attach(bloomVertexShader);
        bloomHighlightProgram.attach(bloomHighlightFragmentShader);
        bloomHighlightProgram.link();

        this.bloomVertexShader = bloomVertexShader;

        this.bloomHighlightFragmentShader = bloomHighlightFragmentShader;
        this.bloomHighlightProgram = bloomHighlightProgram;
        this.bloomHighlightVertexPosition = new GLAttribute(bloomHighlightProgram.getAttributeId("position"));
        this.bloomHighlightSamplerUniform = new GLUniform(bloomHighlightProgram.getUniformId("scene"));

        ShaderProgram bloomMixProgram = new ShaderProgram();
        bloomMixProgram.attach(bloomVertexShader);
        bloomMixProgram.attach(bloomMixFragmentShader);
        bloomMixProgram.link();

        this.bloomMixFragmentShader = bloomMixFragmentShader;
        this.bloomMixProgram = bloomMixProgram;
        this.bloomMixVertexPosition = new GLAttribute(bloomHighlightProgram.getAttributeId("position"));
        this.bloomMixSceneSamplerUniform = new GLUniform(bloomMixProgram.getUniformId("scene"));
        this.bloomMixHighlightSamplerUniform = new GLUniform(bloomMixProgram.getUniformId("highlight"));

        ShaderProgram bloomHBlurProgram = new ShaderProgram();
        bloomHBlurProgram.attach(bloomVertexShader);
        bloomHBlurProgram.attach(bloomHBlurFragmentShader);
        bloomHBlurProgram.link();

        this.bloomHBlurFragmentShader = bloomHBlurFragmentShader;
        this.bloomHBlurProgram = bloomHBlurProgram;
        this.bloomHBlurVertexPosition = new GLAttribute(bloomHBlurProgram.getAttributeId("position"));
        this.bloomHBlurSamplerUniform = new GLUniform(bloomHBlurProgram.getUniformId("source"));
        this.bloomHBlurResolutionUniform = new GLUniform(bloomHBlurProgram.getUniformId("resolution"));

        ShaderProgram bloomVBlurProgram = new ShaderProgram();
        bloomVBlurProgram.attach(bloomVertexShader);
        bloomVBlurProgram.attach(bloomVBlurFragmentShader);
        bloomVBlurProgram.link();

        this.bloomVBlurFragmentShader = bloomVBlurFragmentShader;
        this.bloomVBlurProgram = bloomVBlurProgram;
        this.bloomVBlurVertexPosition = new GLAttribute(bloomVBlurProgram.getAttributeId("position"));
        this.bloomVBlurSamplerUniform = new GLUniform(bloomVBlurProgram.getUniformId("source"));
        this.bloomVBlurResolutionUniform = new GLUniform(bloomVBlurProgram.getUniformId("resolution"));

        logger.info("Loading image");

        this.textureBuffer = new Texture("textures/main.jpg").load();

        // Set up VAO - one per program?
        int vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        this.vao = vao;

        GLBuffer quadVertexBuffer = GLBuffer.create();
        GLElementBuffer quadElementBuffer = GLElementBuffer.create();

        quadVertexBuffer.setStaticDrawData(Buffer.floatBuffer(QUAD_VERTICES));
        quadElementBuffer.setStaticDrawData(Buffer.intBuffer(QUAD_INDICES));

        this.quadVertexBuffer = quadVertexBuffer;
        this.quadElementBuffer = quadElementBuffer;

        // Set up frame buffer for the scene post processing
        this.sceneMultisampleFrameBuffer = GL30.glGenFramebuffers();
        this.sceneMultisampleColorRenderBuffer = GL30.glGenRenderbuffers();
        this.sceneMultisampleDepthRenderBuffer = GL30.glGenRenderbuffers();

        this.sceneFrameBuffer = GL30.glGenFramebuffers();
        this.sceneTextureBuffer = GL11.glGenTextures();
        this.sceneRenderBuffer = GL30.glGenRenderbuffers();
        this.sceneTexture = new TextureBuffer(sceneTextureBuffer);

        this.bloomHighlightFrameBuffer = GL30.glGenFramebuffers();
        this.bloomHighlightTextureBuffer = GL11.glGenTextures();
        this.bloomHighlightTexture = new TextureBuffer(bloomHighlightTextureBuffer);

        this.bloomHBlurFramebuffer = GL30.glGenFramebuffers();
        this.bloomHBlurTextureBuffer = GL11.glGenTextures();
        this.bloomHBlurTexture = new TextureBuffer(bloomHBlurTextureBuffer);

        this.bloomVBlurFramebuffer = GL30.glGenFramebuffers();
        this.bloomVBlurTextureBuffer = GL11.glGenTextures();
        this.bloomVBlurTexture = new TextureBuffer(bloomVBlurTextureBuffer);

        this.bloomHBlur2Framebuffer = GL30.glGenFramebuffers();
        this.bloomHBlur2TextureBuffer = GL11.glGenTextures();
        this.bloomHBlur2Texture = new TextureBuffer(bloomHBlur2TextureBuffer);

        this.bloomVBlur2Framebuffer = GL30.glGenFramebuffers();
        this.bloomVBlur2TextureBuffer = GL11.glGenTextures();
        this.bloomVBlur2Texture = new TextureBuffer(bloomVBlur2TextureBuffer);


    }

    public void prepareViewProjection(Camera camera, int width, int height) {

        // Program plus camera, program.bindCamera(Camera camera)
        this.viewUniform.setFloatMatrix4(camera.getViewProjection(width, height));
        GL20.glUniform3fv(this.eyeUniform, camera.getEye().toBuffer());

    }

    public void prepareLight(Light light) {

        // Program plus light, program.bindLight(Light light);
        lightUniforms.write(light);
    }

    public void prepareEntity(Entity entity) {

        String modelId = entity.getModel();

        // Prepare the model if it hasn't already been prepped
        if (!modelBuffers.containsKey(modelId))
            this.prepareModel(modelId);

        // Set up model transformation matrix
        Pos pos = entity.getPosition();
        Mat4 model = translate(pos.toVec3());
        GL20.glUniformMatrix4fv(this.modelUniform, false, model.toBuffer());

    }

    public void prepareModel(String modelId) {

        if (!models.containsKey(modelId))
            throw new IllegalStateException("Model: " + modelId + " does not exist!");

        Model model = models.get(modelId);

        GLBuffer vbo = GLBuffer.create();
        GLBuffer nbo = GLBuffer.create();
        GLBuffer mbo = GLBuffer.create();
        GLElementBuffer ebo = GLElementBuffer.create();

        vbo.setStaticDrawData(model.toVertexBuffer());
        nbo.setStaticDrawData(model.toNormalBuffer());
        mbo.setStaticDrawData(model.toMappingBuffer());
        ebo.setStaticDrawData(model.toIndexBuffer());

        // Model buffers
        ModelGLBuffers buffers = new ModelGLBuffers(vbo, nbo, mbo, ebo, model.getIndices().length);
        modelBuffers.put(modelId, buffers);

    }

    public void renderEntity(Entity entity) {

        String modelId = entity.getModel();

        if (!modelBuffers.containsKey(modelId))
            throw new IllegalStateException("Model: " + modelId + " does not exist!");

        this.renderModel(modelId);
    }

    public void renderModel(String modelId) {

        this.vertexAttr.enable();
        this.normalAttr.enable();
        this.textureAttr.enable();

        this.textureBuffer.bind(this.samplerUniform);

        ModelGLBuffers buffers = modelBuffers.get(modelId);

        buffers.draw(this.vertexAttr, this.normalAttr, this.textureAttr);

        this.textureAttr.disable();
        this.normalAttr.disable();
        this.vertexAttr.disable();

    }

    public void close() {

        GL20.glUseProgram(0);

        modelBuffers.values().forEach(ModelGLBuffers::destroy);

        GL30.glDeleteVertexArrays(this.vao);
        this.vao = -1;

        this.program.close();
        this.vertexShader.close();
        this.fragShader.close();

        this.program = null;
        this.vertexShader = null;
        this.fragShader = null;

    }


    public void render(Scene scene, Window window) {

        FrameSize windowSize = window.getFramebufferSize();

        int w = windowSize.width();
        int h = windowSize.height();

        // Scene framebuffer

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, sceneMultisampleFrameBuffer);
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, sceneMultisampleColorRenderBuffer);
        GL30.glRenderbufferStorageMultisample(GL30.GL_RENDERBUFFER, 4, GL11.GL_RGBA8, w, h);
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL30.GL_RENDERBUFFER, sceneMultisampleColorRenderBuffer);
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, sceneMultisampleDepthRenderBuffer);
        GL30.glRenderbufferStorageMultisample(GL30.GL_RENDERBUFFER, 4, GL14.GL_DEPTH_COMPONENT24, w, h);
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, sceneMultisampleDepthRenderBuffer);

        if (GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) != GL30.GL_FRAMEBUFFER_COMPLETE)
            throw new IllegalStateException("Something went wrong with framebuffer");

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, sceneFrameBuffer);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, sceneTextureBuffer);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, w, h, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, 0);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        // Add options to all textures to disable wrapping
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, sceneTextureBuffer, 0);
        GL20.glDrawBuffers(GL30.GL_COLOR_ATTACHMENT0);

        // Preserves depth testing, without this we will not have depth testing
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, sceneRenderBuffer);
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, w, h);
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, sceneRenderBuffer);

        if (GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) != GL30.GL_FRAMEBUFFER_COMPLETE)
            throw new IllegalStateException("Something went wrong with framebuffer");

        // Do multi-pass bloom

        // Bloom framebuffers

        // Do a two-stage bloom 8 -> 64
        int bloomScale = 16;
        int bloomScale2 = 8;

        // Highlight framebuffer

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, bloomHighlightFrameBuffer);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, bloomHighlightTextureBuffer);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, w / bloomScale, h / bloomScale, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, 0);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, bloomHighlightTextureBuffer, 0);
        GL20.glDrawBuffers(GL30.GL_COLOR_ATTACHMENT0);

        if (GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) != GL30.GL_FRAMEBUFFER_COMPLETE)
            throw new IllegalStateException("Something went wrong with framebuffer");

        // Horizontal blur framebuffer

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, bloomHBlurFramebuffer);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, bloomHBlurTextureBuffer);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, w / bloomScale, h / bloomScale, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, 0);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, bloomHBlurTextureBuffer, 0);
        GL20.glDrawBuffers(GL30.GL_COLOR_ATTACHMENT0);

        if (GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) != GL30.GL_FRAMEBUFFER_COMPLETE)
            throw new IllegalStateException("Something went wrong with framebuffer");

        // Vertical blur framebuffer

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, bloomVBlurFramebuffer);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, bloomVBlurTextureBuffer);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, w / bloomScale, h / bloomScale, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, 0);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, bloomVBlurTextureBuffer, 0);
        GL20.glDrawBuffers(GL30.GL_COLOR_ATTACHMENT0);

        if (GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) != GL30.GL_FRAMEBUFFER_COMPLETE)
            throw new IllegalStateException("Something went wrong with framebuffer");

        // Bloom framebuffers 2

        // Horizontal blur framebuffer 2

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, bloomHBlur2Framebuffer);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, bloomHBlur2TextureBuffer);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, w / bloomScale2, h / bloomScale2, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, 0);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, bloomHBlur2TextureBuffer, 0);
        GL20.glDrawBuffers(GL30.GL_COLOR_ATTACHMENT0);

        if (GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) != GL30.GL_FRAMEBUFFER_COMPLETE)
            throw new IllegalStateException("Something went wrong with framebuffer");

        // Vertical blur framebuffer 2

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, bloomVBlur2Framebuffer);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, bloomVBlur2TextureBuffer);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, w / bloomScale2, h / bloomScale2, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, 0);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, bloomVBlur2TextureBuffer, 0);
        GL20.glDrawBuffers(GL30.GL_COLOR_ATTACHMENT0);

        if (GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) != GL30.GL_FRAMEBUFFER_COMPLETE)
            throw new IllegalStateException("Something went wrong with framebuffer");

        // Render scene to FBO
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, sceneMultisampleFrameBuffer);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glClearColor(0.1f, 0.6f, 0.8f, 0);
        GL11.glClearDepth(1);
        GL11.glViewport(0, 0, w, h);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        this.program.use();

        Camera camera = getCameraFromScene(scene);
        Light light = getLightFromScene(scene);

        prepareViewProjection(camera, w, h);
        prepareLight(light);

        for (Entity entity : scene.getEntities()) {
            prepareEntity(entity);
            renderEntity(entity);
        }

        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, sceneMultisampleFrameBuffer);
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, sceneFrameBuffer);
        GL30.glBlitFramebuffer(0, 0, w, h, 0, 0, w, h, GL11.GL_COLOR_BUFFER_BIT, GL11.GL_LINEAR);

        // Render quad with bloom highlighting

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, bloomHighlightFrameBuffer);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glClearColor(0, 0, 0, 0);
        GL11.glClearDepth(1);
        GL11.glViewport(0, 0, w / bloomScale, h / bloomScale);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        this.bloomHighlightProgram.use();

        this.bloomHighlightVertexPosition.enable();

        sceneTexture.bind(this.bloomHighlightSamplerUniform);

        quadVertexBuffer.setFloatPointer(bloomHighlightVertexPosition, 2);
        quadElementBuffer.draw(QUAD_INDICES.length);

        this.bloomHighlightVertexPosition.disable();

        // Render quad with horizontal blur

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, bloomHBlurFramebuffer);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glClearColor(0, 0, 0, 0);
        GL11.glClearDepth(1);
        GL11.glViewport(0, 0, w / bloomScale, h / bloomScale);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        this.bloomHBlurProgram.use();

        this.bloomHBlurVertexPosition.enable();

        bloomHighlightTexture.bind(this.bloomHBlurSamplerUniform);

        bloomHBlurResolutionUniform.setFloat2(bloomScale / (float) w, bloomScale / (float) h);

        quadVertexBuffer.setFloatPointer(bloomHBlurVertexPosition, 2);
        quadElementBuffer.draw(QUAD_INDICES.length);

        this.bloomHBlurVertexPosition.disable();

        // Render quad with vertical blur

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, bloomVBlurFramebuffer);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glClearColor(0, 0, 0, 0);
        GL11.glClearDepth(1);
        GL11.glViewport(0, 0, w / bloomScale, h / bloomScale);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        this.bloomVBlurProgram.use();

        this.bloomVBlurVertexPosition.enable();

        bloomHBlurTexture.bind(this.bloomVBlurSamplerUniform);

        bloomVBlurResolutionUniform.setFloat2(bloomScale / (float) w, bloomScale / (float) h);

        quadVertexBuffer.setFloatPointer(bloomVBlurVertexPosition, 2);
        quadElementBuffer.draw(QUAD_INDICES.length);

        this.bloomVBlurVertexPosition.disable();

        // Render quad with horizontal blur Pt.II

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, bloomHBlur2Framebuffer);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glClearColor(0, 0, 0, 0);
        GL11.glClearDepth(1);
        GL11.glViewport(0, 0, w / bloomScale2, h / bloomScale2);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        this.bloomHBlurProgram.use();

        this.bloomHBlurVertexPosition.enable();

        bloomVBlurTexture.bind(this.bloomHBlurSamplerUniform);

        bloomHBlurResolutionUniform.setFloat2(bloomScale2 / (float) w, bloomScale2 / (float) h);

        quadVertexBuffer.setFloatPointer(bloomHBlurVertexPosition, 2);
        quadElementBuffer.draw(QUAD_INDICES.length);

        this.bloomHBlurVertexPosition.disable();

        // Render quad with vertical blur Pt.II

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, bloomVBlur2Framebuffer);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glClearColor(0, 0, 0, 0);
        GL11.glClearDepth(1);
        GL11.glViewport(0, 0, w / bloomScale2, h / bloomScale2);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        this.bloomVBlurProgram.use();

        this.bloomVBlurVertexPosition.enable();

        bloomHBlur2Texture.bind(this.bloomVBlurSamplerUniform);

        bloomVBlurResolutionUniform.setFloat2(bloomScale2 / (float) w, bloomScale2 / (float) h);

        quadVertexBuffer.setFloatPointer(bloomVBlurVertexPosition, 2);
        quadElementBuffer.draw(QUAD_INDICES.length);

        this.bloomVBlurVertexPosition.disable();

        // Mix to screen framebuffer

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glClearColor(0, 0, 0, 0);
        GL11.glClearDepth(1);
        GL11.glViewport(0, 0, w, h);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        this.bloomMixProgram.use();

        this.bloomMixVertexPosition.enable();

        sceneTexture.bind(this.bloomMixSceneSamplerUniform);
        bloomVBlur2Texture.bind(this.bloomMixHighlightSamplerUniform, 1);

        quadVertexBuffer.setFloatPointer(bloomMixVertexPosition, 2);
        quadElementBuffer.draw(QUAD_INDICES.length);

        this.bloomMixVertexPosition.disable();

    }

    private Camera getCameraFromScene(Scene scene) {

        entities.Camera c = scene.getCamera();

        return new Camera(
                c.getEye().toVec3(),
                c.getCenter().toVec3(),
                c.getUp().toVec3()
        );

    }

    private Light getLightFromScene(Scene scene) {

        entities.Light l = scene.getLight();
        entities.LightSpecularity ls = scene.getLight().getSpecularity();

        return new Light(
                l.getDirection(),
                l.getColor().toVec3(),
                l.getAmbience(),
                l.getDiffusion(),
                new LightSpecularity(
                        ls.getIntensity(),
                        ls.getPower()
                )
        );

    }

}
