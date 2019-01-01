package engine.modelling;

import engine.renderer.Buffer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

public class Model {

    // TODO: We may want to load the model directly to the GPU hardware
    // rather than store in intermediate arrays in memory.
    private final float[] vertices;
    private final float[] normals;
    private final int[] indices;
    private final float[] mapping;

    public Model(float[] vertices, float[] normals, int[] indices, float[] mapping) {
        this.vertices = vertices;
        this.normals = normals;
        this.indices = indices;
        this.mapping = mapping;
    }

    public float[] getVertices() {
        return this.vertices;
    }

    public float[] getNormals() {
        return this.normals;
    }

    public int[] getIndices() {
        return this.indices;
    }

    public float[] getMapping() {
        return this.mapping;
    }

    public FloatBuffer toVertexBuffer() {
        return Buffer.floatBuffer(getVertices());
    }

    public FloatBuffer toNormalBuffer() {
        return Buffer.floatBuffer(getNormals());
    }

    public IntBuffer toIndexBuffer() {
        return Buffer.intBuffer(getIndices());
    }

    public FloatBuffer toMappingBuffer() {
        return Buffer.floatBuffer(getMapping());
    }

}
