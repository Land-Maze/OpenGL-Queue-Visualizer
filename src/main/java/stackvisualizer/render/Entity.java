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
     * @param name The name of the entity.
     * @param mesh The mesh representing the entity's geometry.
     * @param material The material defining the entity's appearance.
     * @param modelMatrix The model matrix defining the entity's transformation in the world.
     */
    public Entity(String name, Mesh mesh, Material material, ModelMatrix modelMatrix) {
        this.name = name;
        this.mesh = mesh;
        this.material = material;
        this.modelMatrix = modelMatrix;
    }

    public void render(Matrix4f view, Matrix4f projection, Vector3f viewPos, Vector3f lightPos) {
        material.bind(modelMatrix.asMatrix(), view, projection, viewPos, lightPos);
        mesh.render();
        material.unbind();
    }

    public void cleanup() {
        mesh.cleanup();
    }

    // public void reset() {
    //     position.set(0, 0, 0);
    //     rotation.set(0, 0, 0);
    //     scale.set(1, 1, 1);
    //     color.set(1, 1, 1, 1);
    // }
    // public void translate(Vector3f translation) {
    //     position.add(translation);
    // }
    // public void rotate(Vector3f rotation) {
    //     this.rotation.add(rotation);
    // }
    // public void scale(Vector3f scaling) {
    //     scale.mul(scaling);
    // }
    // public void setPosition(Vector3f position) {
    //     this.position.set(position);
    // }
    // public void setRotation(Vector3f rotation) {
    //     this.rotation.set(rotation);
    // }
    // public void setScale(Vector3f scale) {
    //     this.scale.set(scale);
    // }
    // public void setColor(Vector4f color) {
    //     this.color.set(color);
    // }
    // public Vector3f getPosition() {
    //     return position;
    // }
    // public Vector3f getRotation() {
    //     return rotation;
    // }
    // public Vector3f getScale() {
    //     return scale;
    // }
    // public Vector4f getColor() {
    //     return color;
    // }

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
