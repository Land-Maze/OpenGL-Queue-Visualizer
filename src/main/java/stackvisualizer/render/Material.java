package stackvisualizer.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Material {
  private final Shader shader;
  private final Vector4f color;

  public Material(Shader shader, Vector4f color) {
    this.shader = shader;
    this.color = color;
  }

  public void bind(Matrix4f model, Matrix4f view, Matrix4f projection, Vector3f viewPos, Vector3f lightPos) {
    shader.bind();
    shader.setUniform("model", model);
    shader.setUniform("view", view);
    shader.setUniform("projection", projection);
    shader.setUniform("color", color);
    shader.setUniform("lightPos", lightPos);
    shader.setUniform("viewPos", viewPos);
  }

  public void cleanup() {
    shader.cleanup();
  }

  public void unbind() {
    shader.unbind();
  }

  public Shader getShader() {
    return shader;
  }

  public Vector4f getColor() {
    return color;
  }

  public void reset() {
    shader.bind();
    shader.setUniform("color", new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
    shader.unbind();
  }

  public Material copy() {
    return new Material(shader, new Vector4f(color));
  }

  public Material set(Material other) {
    this.shader.set(other.shader);
    this.color.set(other.color);
    return this;
  }

  public Material set(Shader shader, Vector4f color, Vector3f lightPos) {
    this.shader.set(shader);
    this.color.set(color);
    return this;
  }

  public Material set(Shader shader) {
    this.shader.set(shader);
    return this;
  }

  @Override
  public String toString() {
    return "Material{" +
        "shader=" + shader +
        ", color=" + color +
        '}';
  }
}
