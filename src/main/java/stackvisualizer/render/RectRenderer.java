package stackvisualizer.render;

import java.io.IOException;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import stackvisualizer.Meshes.Quad;

public class RectRenderer {
    private int vaoId, vboId;
    private Shader shader;

    @SuppressWarnings("CallToPrintStackTrace")
    public void init() {
        float[] vertices = Quad.getVertices();

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 2, GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        try {
          shader = new Shader("shaders/ui.vert", "shaders/ui.frag");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load shaders for RectRenderer", e);
        }
    }

    public void drawRectangle(float x, float y, float width, float height, Vector4f color, int screenWidth, int screenHeight) {
        shader.bind();

        Matrix4f ortho = new Matrix4f().ortho(0, screenWidth, screenHeight, 0, -1, 1);
        Matrix4f model = new Matrix4f().translate(x, y, 0).scale(width, height, 1);

        shader.setUniform("projection", ortho);
        shader.setUniform("model", model);
        shader.setUniform("color", color);

        glBindVertexArray(vaoId);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        glBindVertexArray(0);

        shader.unbind();
    }

    public void cleanup() {
        shader.cleanup();
        glDeleteBuffers(vboId);
        glDeleteVertexArrays(vaoId);
    }
}
