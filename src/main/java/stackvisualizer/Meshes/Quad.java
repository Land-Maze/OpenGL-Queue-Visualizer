package stackvisualizer.Meshes;

public class Quad {
    public static float[] getVertices() {
        return new float[]{
            // posX, posY, u, v
            0f, 0f, 0f, 0f,
            1f, 0f, 1f, 0f,
            1f, 1f, 1f, 1f,
            0f, 0f, 0f, 0f,
            1f, 1f, 1f, 1f,
            0f, 1f, 0f, 1f
        };
    }
}
