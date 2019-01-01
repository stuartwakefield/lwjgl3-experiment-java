package engine.modelling;

public class Pyramid {

    public static float[] getVertices() {

        return new float[] {

                // Face one
                0, 1, 0,
                -1, -1, -1,
                1, -1, -1,

                // Face two
                0, 1, 0,
                1, -1, 1,
                -1, -1, 1,

                // Face three
                0, 1, 0,
                -1, -1, 1,
                -1, -1, -1,

                // Face four
                0, 1, 0,
                1, -1, -1,
                1, -1, 1,

                // Face five
                1, -1, -1,
                -1, -1, -1,
                -1, -1, 1,
                1, -1, 1

        };

    }

    public static int[] getIndices() {

        return new int[] {

                // Face one
                2, 1, 0,

                // Face two
                5, 4, 3,

                // Face three
                8, 7, 6,

                // Face four
                11, 10, 9,

                // Face five
                13, 12, 15,
                15, 14, 13

        };

    }

    public static float[] getMappings() {

        return new float[] {
                0.5f, 1.0f,
                0.0f, 0.0f,
                1.0f, 0.0f,

                0.5f, 1.0f,
                0.0f, 0.0f,
                1.0f, 0.0f,

                0.5f, 1.0f,
                0.0f, 0.0f,
                1.0f, 0.0f,

                0.5f, 1.0f,
                0.0f, 0.0f,
                1.0f, 0.0f,

                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f,
                0.0f, 0.0f
        };

    }

    public static Model getModel() {

        int[] indices = getIndices();
        float[] vertices = getVertices();

        return new Model(vertices, Cuboid.getNormals(indices, vertices), indices, getMappings());
    }
}
