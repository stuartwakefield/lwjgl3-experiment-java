package engine.modelling;

import engine.geometry.Vec3;

import java.util.Arrays;

public class Cuboid {

    public static Model getModel() {
        return new Model(getVertices(), getNormals(), getIndices(), getMappings());
    }

    public static float[] getVertices() {

        return new float[] {

                // Face one
                -1, 1, -1,
                1, 1, -1,
                1, -1, -1,
                -1, -1, -1,

                // Face two
                1, 1, 1,
                -1, 1, 1,
                -1, -1, 1,
                1, -1, 1,

                // Face three
                -1, 1, 1,
                1, 1, 1,
                1, 1, -1,
                -1, 1, -1,

                // Face four
                1, -1, -1,
                -1, -1, -1,
                -1, -1, 1,
                1, -1, 1,

                // Face five
                -1, 1, 1,
                -1, 1, -1,
                -1, -1, -1,
                -1, -1, 1,

                // Face six
                1, 1, -1,
                1, 1, 1,
                1, -1, 1,
                1, -1, -1

        };

    }

    public static int[] getIndices() {

        return new int[] {

                // Face one
                3, 0, 1,
                1, 2, 3,

                // Face two
                7, 4, 5,
                5, 6, 7,

                // Face three
                11, 8, 9,
                9, 10, 11,

                // Face four
                13, 12, 15,
                15, 14, 13,

                // Face five
                19, 16, 17,
                17, 18, 19,

                // Face six
                23, 20, 21,
                21, 22, 23

        };

    }

    public static float[] getMappings() {

        return new float[] {
                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f,
                0.0f, 0.0f,

                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f,
                0.0f, 0.0f,

                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f,
                0.0f, 0.0f,

                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f,
                0.0f, 0.0f,

                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f,
                0.0f, 0.0f,

                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f,
                0.0f, 0.0f
        };

    }

    public static float[] getNormals() {
        return getNormals(getIndices(), getVertices());
    }

    public static float[] getNormals(int[] indices, float[] vertices) {

        float[] normals = new float[vertices.length];

        for (int i = 0; i < normals.length; ++i) {
            normals[i] = 0;
        }

        for (int i = 0; i < indices.length; i += 3) {

            int i1 = indices[i];
            int i2 = indices[i + 1];
            int i3 = indices[i + 2];

            Vec3 v1 = new Vec3(vertices[i1 * 3], vertices[i1 * 3 + 1], vertices[i1 * 3 + 2]);
            Vec3 v2 = new Vec3(vertices[i2 * 3], vertices[i2 * 3 + 1], vertices[i2 * 3 + 2]);
            Vec3 v3 = new Vec3(vertices[i3 * 3], vertices[i3 * 3 + 1], vertices[i3 * 3 + 2]);
            Vec3 a = v2.subtract(v1);
            Vec3 b = v3.subtract(v1);
            Vec3 c = a.cross(b).normalize();

            normals[i1 * 3] += c.x();
            normals[i1 * 3 + 1] += c.y();
            normals[i1 * 3 + 2] += c.z();

            normals[i2 * 3] += c.x();
            normals[i2 * 3 + 1] += c.y();
            normals[i2 * 3 + 2] += c.z();

            normals[i3 * 3] += c.x();
            normals[i3 * 3 + 1] += c.y();
            normals[i3 * 3 + 2] += c.z();

        }

        for (int i = 0; i < normals.length; i += 3) {
            Vec3 normalized = new Vec3(normals[i], normals[i + 1], normals[i + 2]).normalize();

            normals[i] = normalized.x();
            normals[i + 1] = normalized.y();
            normals[i + 2] = normalized.z();
        }

        return normals;

    }

}
