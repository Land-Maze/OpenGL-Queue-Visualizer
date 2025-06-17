package stackvisualizer.render;

import stackvisualizer.core.Cube;

import static org.lwjgl.opengl.GL30.*;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class Renderer {
    private int vaoId, vboId;

    public void init() {
        float[] vertices = Cube.getVertices();

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void render(Shader shader, Matrix4f model, Matrix4f view, Matrix4f projection, Vector4f color) {
        shader.bind();
        shader.setUniform("model", model);
        shader.setUniform("view", view);
        shader.setUniform("projection", projection);
        shader.setUniform("color", color);

        glBindVertexArray(vaoId);
        glDrawArrays(GL_TRIANGLES, 0, 36);
        glBindVertexArray(0);

        shader.unbind();
    }

    public void cleanup() {
        glDeleteBuffers(vboId);
        glDeleteVertexArrays(vaoId);
    }
}