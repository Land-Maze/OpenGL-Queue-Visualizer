package stackvisualizer;

import java.io.IOException;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glViewport;

import stackvisualizer.core.Cube;
import stackvisualizer.render.Camera;
import stackvisualizer.render.Entity;
import stackvisualizer.render.Material;
import stackvisualizer.render.Mesh;
import stackvisualizer.render.ModelMatrix;
import stackvisualizer.render.Renderer;
import stackvisualizer.render.Shader;
import stackvisualizer.render.TextRenderer;
import stackvisualizer.util.Window;

public class Main {
    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) {

        System.out.println("Stack Visualizer");

        Window window = new Window(1280, 720, "Stack Visualizer", true);
        window.init();

        Mesh cubeMesh = new Mesh(Cube.getVertices());
        Material cubeMaterial;
        try {
            cubeMaterial = new Material(new Shader("shaders/cube.vert", "shaders/cube.frag"), new Vector4f(0.5f, 0.5f, 0.5f, 1.0f));
        } catch (IOException e) {
            System.err.println("Error loading cube shader: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        ModelMatrix cube1ModelMatrix = new ModelMatrix();
        cube1ModelMatrix.setPosition(0, 0, 0);
        Entity cube1 = new Entity("Cube1", cubeMesh, cubeMaterial, cube1ModelMatrix);

        ModelMatrix cube2ModelMatrix = new ModelMatrix();
        cube2ModelMatrix.setPosition(1.5f, 0, 0);
        Entity cube2 = new Entity("Cube2", cubeMesh, cubeMaterial, cube2ModelMatrix);

        ModelMatrix cube3ModelMatrix = new ModelMatrix();
        cube3ModelMatrix.setPosition(-1.5f, 0, 0);
        cube3ModelMatrix.setRotation(0, 45, 0);
        Material cube3Material;
        
        try {
            cube3Material = new Material(new Shader("shaders/cube.vert", "shaders/cube.frag"), new Vector4f(0.0f, 0.0f, 1f, 0.4f));
        } catch (IOException e) {
            System.err.println("Error loading cube shader: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        Entity cube3 = new Entity("Cube3", cubeMesh, cube3Material, cube3ModelMatrix);

        Renderer renderer = new Renderer();
        renderer.addObject(cube1);
        renderer.addObject(cube2);
        renderer.addObject(cube3);
        
        Camera camera = new Camera();
        Shader shader;

        try {
            shader = new Shader("shaders/cube.vert", "shaders/cube.frag");
        } catch (IOException e) {
            System.err.println("Error loading shader: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        TextRenderer textRenderer = new TextRenderer("fonts/RobotoMono-VariableFont_wght.ttf", 24f);

        long lastTime = System.nanoTime();
        int frames = 0;
        float fps = 0;
        float accumulatedTime = 0;
        
        // Main loop
        while (!window.shouldClose()) {
            long now = System.nanoTime();
            float delta = (now - lastTime) / 1_000_000_000.0f;
            lastTime = now;
            
            accumulatedTime += delta;
            frames++;
            if (accumulatedTime >= 0.1f) { // 1/10 of a second
                fps = frames / accumulatedTime;
                frames = 0;
                accumulatedTime = 0;
            }

            window.clear();

            if (window.isResized()) {
                glViewport(0, 0, window.getWidth(), window.getHeight());
                window.setResized(false);
            }

            if (window.consumeReset()) {
                camera.reset();
            }

            float dx = window.consumeDeltaX();
            float dy = window.consumeDeltaY();
            camera.rotate(dx * 0.3f, -dy * 0.3f);
            float dz = window.consumeDeltaZ();
            camera.zoom(dz * 0.1f);

            float px = window.consumePanX();
            float py = window.consumePanY();
            camera.pan(px * 0.005f, py * 0.005f);

            Matrix4f view = camera.getViewMatrix();
            Matrix4f projection = camera.getProjectionMatrix(45.0f, window.getAspectRatio(), 0.1f, 100.0f);

            Vector3f viewPos = camera.getPosition();

            renderer.renderAll(view, projection, viewPos, (float) glfwGetTime());

            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            glOrtho(0, window.getWidth(), window.getHeight(), 0, -1, 1);
            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();

            textRenderer.renderText(String.format("FPS: %.2f", fps), 10, 30);

            window.update();
        }

        // Cleanup
        {
            window.destroy();
            renderer.cleanup();
            shader.cleanup();
            textRenderer.cleanup();
            System.out.println("Application closed successfully.");
        }
    }
}