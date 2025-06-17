package stackvisualizer.util;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_MIDDLE;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.system.MemoryUtil.NULL;
public class Window {

    private static final float MOUSE_SENSITIVITY = 2f;

    private long windowHandle;

    private int width;
    private int height;
    private String title;

    private boolean resized;
    private boolean vSync;

    private double prevX, prevY;
    private float deltaX = 0, deltaY = 0, deltaZ = 0;
    private float panX = 0, panY = 0;

    private boolean rightMouseDown = false;
    private boolean middleMouseDown = false;
    private boolean resetPressed = false;

    /**
     * Constructs a new Window instance with specified dimensions, title, and VSync setting.
     *
     * @param width  the width of the window in pixels
     * @param height the height of the window in pixels
     * @param title  the title of the window
     * @param vSync  vertical synchronization (VSync)
     */
    public Window(int width, int height, String title, boolean vSync) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.vSync = vSync;
        this.resized = false;
    }

    /**
     * Initializes the GLFW window and OpenGL context.
     * <p>
     * Sets up callbacks for resizing and key input, centers the window on the primary monitor,
     * and makes the OpenGL context current.
     *
     * @throws IllegalStateException if GLFW cannot be initialized or window creation fails
     */
    public void init() {
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowHandle == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwSetFramebufferSizeCallback(windowHandle, (window, w, h) -> {
            this.width = w;
            this.height = h;
            this.resized = true;
        });

        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true);
            }
        });

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(
            windowHandle,
            (vidmode.width() - width) / 2,
            (vidmode.height() - height) / 2
        );

        glfwMakeContextCurrent(windowHandle);

        glfwSwapInterval(vSync ? 1 : 0);

        glfwShowWindow(windowHandle);

        GL.createCapabilities();

        glClearColor(0f, 0f, 0f, 1f);  // black background
        glEnable(GL_DEPTH_TEST);

        setupCallbacks();
    }

    public void update() {
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(windowHandle);
    }

    public void destroy() {
        glfwDestroyWindow(windowHandle);
        glfwTerminate();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isResized() {
        return resized;
    }

    public void setResized(boolean resized) {
        this.resized = resized;
    }

    public long getWindowHandle() {
        return windowHandle;
    }

    public float getAspectRatio() {
        return (float) width / (float) height;
    }

    private void setupCallbacks() {
    glfwSetCursorPosCallback(windowHandle, (win, xpos, ypos) -> {
        if (rightMouseDown) {
            deltaX = (float)(xpos - prevX) * MOUSE_SENSITIVITY;
            deltaY = (float)(ypos - prevY) * MOUSE_SENSITIVITY;
        }
        else if (middleMouseDown) {
            panX = (float)(xpos - prevX);
            panY = (float)(ypos - prevY);
        }
        prevX = xpos;
        prevY = ypos;
    });

    glfwSetMouseButtonCallback(windowHandle, (win, button, action, mods) -> {
        if (button == GLFW_MOUSE_BUTTON_RIGHT) {
            rightMouseDown = (action == GLFW_PRESS);
        }
        else if (button == GLFW_MOUSE_BUTTON_MIDDLE) {
            middleMouseDown = (action == GLFW_PRESS);
        }
    });

    glfwSetScrollCallback(windowHandle, (win, xoffset, yoffset) -> {
        deltaZ += (float)yoffset * MOUSE_SENSITIVITY;
    });

    glfwSetKeyCallback(windowHandle, (win, key, scancode, action, mods) -> {
        if (key == GLFW_KEY_SPACE && action == GLFW_PRESS) {
            resetPressed = true;
        }
    });
}

    public float consumeDeltaX() {
        float dx = -deltaX;
        deltaX = 0;
        return dx;
    }

    public float consumeDeltaY() {
        float dy = -deltaY;
        deltaY = 0;
        return dy;
    }

    public float consumeDeltaZ() {
        float dz = -deltaZ;
        deltaZ = 0;
        return dz;
    }

    public float consumePanX() {
        float x = -panX;
        panX = 0;
        return x;
    }

    public float consumePanY() {
        float y = -panY;
        panY = 0;
        return y;
    }

    public boolean consumeReset() {
        boolean wasPressed = resetPressed;
        resetPressed = false;
        return wasPressed;
    }
}
