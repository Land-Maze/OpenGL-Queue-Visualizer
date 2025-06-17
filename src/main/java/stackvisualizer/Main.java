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

        /***********************************************************
         * ============================================================
         * Initialization of the application
         * ============================================================
         ***********************************************************/
        Window window = new Window(1280, 720, "Stack Visualizer", true);
        window.init();

        Camera camera = new Camera();

        TextRenderer textRenderer = new TextRenderer("fonts/RobotoMono-VariableFont_wght.ttf", 24f);

        long lastTime = System.nanoTime();
        int frames = 0;
        float fps = 0;
        float accumulatedTime = 0;

        /***********************************************************
         * Shared resources
         ***********************************************************/
        Shader lightShader;
        try {
            lightShader = new Shader("shaders/light.vert", "shaders/light.frag");
        } catch (IOException e) {
            System.err.println("Error loading light shader: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        System.out.println("Light shader loaded successfully.");

        Mesh cubeMesh = new Mesh(Cube.getVertices());

        Material cubeMaterial = new Material(lightShader, new Vector4f(0.5f, 0.5f, 0.5f, 1.0f));

        System.out.println("Cube mesh and material created successfully.");

        /***********************************************************
         * Creating entities
         ***********************************************************/
        Entity cube1 = new Entity("Cube 1", cubeMesh, cubeMaterial, new ModelMatrix()
                .translate(new Vector3f(-2.0f, 1.0f, 0.0f))
                .scale(new Vector3f(1.0f, 1.0f, 1.0f)));
        Entity cube2 = new Entity("Cube 2", cubeMesh, cubeMaterial, new ModelMatrix()
                .translate(new Vector3f(0.0f, 0.0f, 0.0f))
                .scale(new Vector3f(1.0f, 1.0f, 1.0f)));
        Entity cube3 = new Entity("Cube 3", cubeMesh, cubeMaterial, new ModelMatrix()
                .translate(new Vector3f(2.0f, -1.0f, 0.0f))
                .scale(new Vector3f(1.0f, 1.0f, 1.0f)));

        Renderer renderer = new Renderer();
        renderer.addObject(cube1);
        renderer.addObject(cube2);
        renderer.addObject(cube3);

        System.out.println("Entities created successfully.");

        /***********************************************************
         * ============================================================
         * Main loop
         * ============================================================
         ***********************************************************/

        while (!window.shouldClose()) {

            /***********************************************************
             * Calculating FPS
             ***********************************************************/
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

            /***********************************************************
             * Handling window events and input
             ***********************************************************/
            if (window.isResized()) {
                glViewport(0, 0, window.getWidth(), window.getHeight());
                window.setResized(false);
            }

            if (window.consumeReset()) {
                camera.reset();
            }

            // Rotating with mouse
            float dx = window.consumeDeltaX();
            float dy = window.consumeDeltaY();
            camera.rotate(dx * 0.3f, -dy * 0.3f);

            // Panning with mouse
            float px = window.consumePanX();
            float py = window.consumePanY();
            camera.pan(px * 0.005f, py * 0.005f);
            // Zooming with mouse wheel
            float dz = window.consumeDeltaZ();
            camera.zoom(dz * 0.1f);

            /***********************************************************
             * Rendering the scene
             ***********************************************************/

            Matrix4f view = camera.getViewMatrix();
            Matrix4f projection = camera.getProjectionMatrix(45.0f, window.getAspectRatio(), 0.1f, 100.0f);

            Vector3f viewPos = camera.getPosition();

            renderer.renderAll(view, projection, viewPos, (float) glfwGetTime());

            /***********************************************************
             * Rendering text overlay
             ***********************************************************/
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
            renderer.getObjects().forEach(Entity::cleanup);
            textRenderer.cleanup();
            System.out.println("Application closed successfully.");
        }
    }
}