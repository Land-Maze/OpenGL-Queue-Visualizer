package stackvisualizer.ui;

import stackvisualizer.render.RectRenderer;
import stackvisualizer.render.TextRenderer;

public abstract class UIElement {
    protected float x, y, width, height;
    protected boolean active = false;
    protected TextRenderer textRenderer;
    protected RectRenderer rectRenderer;
    protected int screenHeight, screenWidth;
    public abstract void render();
    public abstract void update();
    public boolean contains(float px, float py) {
        return px >= x && px <= x + width && py >= y && py <= y + height;
    }
    public boolean isActive() {
        return active;
    }

    public void setText(String text) {}
}

