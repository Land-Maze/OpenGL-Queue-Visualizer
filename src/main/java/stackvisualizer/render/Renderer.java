package stackvisualizer.render;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.joml.Vector3f;
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

import stackvisualizer.core.Cube;


public class Renderer {
    private int vaoId, vboId;

    public void init() {
      float[] vertices = Cube.getVertices();

      vaoId = glGenVertexArrays();
      glBindVertexArray(vaoId);

      vboId = glGenBuffers();
      glBindBuffer(GL_ARRAY_BUFFER, vboId);
      glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

      // position attribute
      glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
      glEnableVertexAttribArray(0);

      // normal attribute
      glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
      glEnableVertexAttribArray(1);

      glBindBuffer(GL_ARRAY_BUFFER, 0);
      glBindVertexArray(0);
    }

    public void render(Shader shader, Matrix4f model, Matrix4f view, Matrix4f projection, Vector4f color, Vector3f lightPos, Vector3f viewPos) {
      shader.bind();

      shader.setUniform("model", model);
      shader.setUniform("view", view);
      shader.setUniform("projection", projection);
      shader.setUniform("color", color);
      shader.setUniform("lightPos", lightPos);
      shader.setUniform("viewPos", viewPos);

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