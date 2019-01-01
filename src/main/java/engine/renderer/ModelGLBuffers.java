package engine.renderer;

public class ModelGLBuffers {

    private final GLBuffer vertexBuffer;
    private final GLBuffer normalBuffer;
    private final GLBuffer textureMappingBuffer;
    private final GLElementBuffer elementBuffer;
    private final Integer elementCount;

    public ModelGLBuffers(GLBuffer vertexBuffer, GLBuffer normalBuffer, GLBuffer textureMappingBuffer, GLElementBuffer elementBuffer, Integer elementCount) {
        this.vertexBuffer = vertexBuffer;
        this.normalBuffer = normalBuffer;
        this.textureMappingBuffer = textureMappingBuffer;
        this.elementBuffer = elementBuffer;
        this.elementCount = elementCount;
    }

    public void draw(GLAttribute vertexAttr, GLAttribute normalAttr, GLAttribute textureAttr) {
        this.vertexBuffer.setFloatPointer(vertexAttr, 3);
        this.normalBuffer.setFloatPointer(normalAttr, 3);
        this.textureMappingBuffer.setFloatPointer(textureAttr, 2);
        this.elementBuffer.draw(this.elementCount);
    }

    public void destroy() {
        this.vertexBuffer.destroy();
    }

}
