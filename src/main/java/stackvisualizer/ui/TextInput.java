package stackvisualizer.ui;

import org.joml.Vector4f;

public class TextInput extends UIElement {
    private StringBuilder text = new StringBuilder();
    private boolean active = false;

    public TextInput(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.textRenderer = null;
        this.rectRenderer = null;
    }

    public void type(char c) {
        if (active) text.append(c);
    }

    public void click(float px, float py) {
        active = contains(px, py);
    }

    @Override
    public void render() {
        rectRenderer.drawRectangle(x, y, width, height, new Vector4f(0.1f, 0.1f, 0.1f, 1f), screenWidth, screenHeight);
        textRenderer.renderText(text.toString(), x + 5, y + 5);
    }

    @Override
    public void update() { }
}
