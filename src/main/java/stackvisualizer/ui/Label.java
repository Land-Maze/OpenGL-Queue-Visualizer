package stackvisualizer.ui;

import org.joml.Vector4f;

public class Label extends UIElement {
    private String text;

    public Label(String text, float x, float y, float width, float height) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.textRenderer = null;
        this.rectRenderer = null;
    }

    @Override
    public void render() {
        rectRenderer.drawRectangle(x, y, width, height, new Vector4f(0.1f, 0.1f, 0.1f, 1f), screenWidth, screenHeight);
        textRenderer.renderText(text, x + 5, y + 5);
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }

    @Override
    public void update() { }
  
}
