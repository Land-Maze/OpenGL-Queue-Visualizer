package stackvisualizer.Meshes;

public class Cube {
    private static final float[] VERTICES = {
    // positions (3 f)    // normals (3 f)
    -0.5f, -0.5f, -0.5f,  0f,  0f, -1f,
     0.5f, -0.5f, -0.5f,  0f,  0f, -1f,
     0.5f,  0.5f, -0.5f,  0f,  0f, -1f,
     0.5f,  0.5f, -0.5f,  0f,  0f, -1f,
    -0.5f,  0.5f, -0.5f,  0f,  0f, -1f,
    -0.5f, -0.5f, -0.5f,  0f,  0f, -1f,

    -0.5f, -0.5f,  0.5f,  0f,  0f,  1f,
     0.5f, -0.5f,  0.5f,  0f,  0f,  1f,
     0.5f,  0.5f,  0.5f,  0f,  0f,  1f,
     0.5f,  0.5f,  0.5f,  0f,  0f,  1f,
    -0.5f,  0.5f,  0.5f,  0f,  0f,  1f,
    -0.5f, -0.5f,  0.5f,  0f,  0f,  1f,

    -0.5f,  0.5f,  0.5f, -1f,  0f,  0f,
    -0.5f,  0.5f, -0.5f, -1f,  0f,  0f,
    -0.5f, -0.5f, -0.5f, -1f,  0f,  0f,
    -0.5f, -0.5f, -0.5f, -1f,  0f,  0f,
    -0.5f, -0.5f,  0.5f, -1f,  0f,  0f,
    -0.5f,  0.5f,  0.5f, -1f,  0f,  0f,

     0.5f,  0.5f,  0.5f,  1f,  0f,  0f,
     0.5f,  0.5f, -0.5f,  1f,  0f,  0f,
     0.5f, -0.5f, -0.5f,  1f,  0f,  0f,
     0.5f, -0.5f, -0.5f,  1f,  0f,  0f,
     0.5f, -0.5f,  0.5f,  1f,  0f,  0f,
     0.5f,  0.5f,  0.5f,  1f,  0f,  0f,

    -0.5f, -0.5f, -0.5f,  0f, -1f,  0f,
     0.5f, -0.5f, -0.5f,  0f, -1f,  0f,
     0.5f, -0.5f,  0.5f,  0f, -1f,  0f,
     0.5f, -0.5f,  0.5f,  0f, -1f,  0f,
    -0.5f, -0.5f,  0.5f,  0f, -1f,  0f,
    -0.5f, -0.5f, -0.5f,  0f, -1f,  0f,

    -0.5f,  0.5f, -0.5f,  0f,  1f,  0f,
     0.5f,  0.5f, -0.5f,  0f,  1f,  0f,
     0.5f,  0.5f,  0.5f,  0f,  1f,  0f,
     0.5f,  0.5f,  0.5f,  0f,  1f,  0f,
    -0.5f,  0.5f,  0.5f,  0f,  1f,  0f,
    -0.5f,  0.5f, -0.5f,  0f,  1f,  0f
};

    public static float[] getVertices() {
        return VERTICES;
    }
}
