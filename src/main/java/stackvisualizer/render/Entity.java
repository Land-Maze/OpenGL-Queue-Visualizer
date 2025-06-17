package stackvisualizer.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Entity {
  private final String name;
  private final Mesh mesh;
  private final Material material;
  private final ModelMatrix modelMatrix;

  /**
   * Creates an Entity with a name, mesh, material, and model matrix.
   * 
   * @param name        The name of the entity.
   * @param mesh        The mesh representing the entity's geometry.
   * @param material    The material defining the entity's appearance.
   * @param modelMatrix The model matrix defining the entity's transformation in
   *                    the world.
   */
  public Entity(String name, Mesh mesh, Material material, ModelMatrix modelMatrix) {
    this.name = name;
    this.mesh = mesh;
    this.material = material;
    this.modelMatrix = modelMatrix;
  }

  /**
   * Renders the entity using the provided view and projection matrices, view
   * position, and light position.
   *
   * @param view       The view matrix for the camera.
   * @param projection The projection matrix for the camera.
   * @param viewPos    The position of the camera in the world.
   * @param lightPos   The position of the light source in the world.
   */
  public void render(Matrix4f view, Matrix4f projection, Vector3f viewPos, Vector3f lightPos) {
    material.bind(modelMatrix.getMatrix(), view, projection, viewPos, lightPos);
    mesh.render();
    material.unbind();
  }

  public void cleanup() {
    mesh.cleanup();
    material.reset();
    material.getShader().cleanup();
  }

  @Override
  public String toString() {
    return "Entity{" +
        "name='" + name + '\'' +
        ", mesh=" + mesh +
        ", material=" + material +
        ", modelMatrix=" + modelMatrix +
        '}';
  }
}
