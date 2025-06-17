package stackvisualizer.render;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;

public class Shader {
    private final int programId;

    public Shader(String vertexPath, String fragmentPath) throws IOException {
        String vertexCode = new String(Files.readAllBytes(Paths.get(vertexPath)));
        String fragmentCode = new String(Files.readAllBytes(Paths.get(fragmentPath)));

        int vertexId = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexId, vertexCode);
        glCompileShader(vertexId);
        checkCompileErrors(vertexId, "VERTEX");

        int fragmentId = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentId, fragmentCode);
        glCompileShader(fragmentId);
        checkCompileErrors(fragmentId, "FRAGMENT");

        programId = glCreateProgram();
        glAttachShader(programId, vertexId);
        glAttachShader(programId, fragmentId);
        glLinkProgram(programId);
        checkCompileErrors(programId, "PROGRAM");

        glDeleteShader(vertexId);
        glDeleteShader(fragmentId);
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void setUniform(String name, Matrix4f matrix) {
        int location = glGetUniformLocation(programId, name);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        matrix.get(buffer);
        glUniformMatrix4fv(location, false, buffer);
    }

    public void setUniform(String name, Vector4f vec) {
        int location = glGetUniformLocation(programId, name);
        glUniform4f(location, vec.x, vec.y, vec.z, vec.w);
    }

    private void checkCompileErrors(int id, String type) {
        int status;
        if (type.equals("PROGRAM")) {
            status = glGetProgrami(id, GL_LINK_STATUS);
            if (status == GL_FALSE) {
                String log = glGetProgramInfoLog(id);
                System.err.println("Shader Program linking error: " + log);
            }
        } else {
            status = glGetShaderi(id, GL_COMPILE_STATUS);
            if (status == GL_FALSE) {
                String log = glGetShaderInfoLog(id);
                System.err.println("Shader compilation error in " + type + ": " + log);
            }
        }
    }

    public void cleanup() {
        glDeleteProgram(programId);
    }
}