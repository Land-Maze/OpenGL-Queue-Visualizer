package stackvisualizer.render;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.GL_ALPHA;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTruetype;

import static stackvisualizer.util.ResourceLoader.ioResourceToByteBuffer;

public class TextRenderer {

  private static final int BITMAP_W = 512;
  private static final int BITMAP_H = 512;

  private final int fontTextureID;
  private final STBTTBakedChar.Buffer charData;

  public TextRenderer(String ttfResourcePath, float fontHeight) {
    ByteBuffer ttfBuffer = null;
    try {
      ttfBuffer = ioResourceToByteBuffer(ttfResourcePath, 160 * 1024);
    } catch (IOException e) {
      throw new RuntimeException("Failed to load font file", e);
    }

    charData = STBTTBakedChar.malloc(96);

    ByteBuffer bitmap = BufferUtils.createByteBuffer(BITMAP_W * BITMAP_H);

    int result = STBTruetype.stbtt_BakeFontBitmap(ttfBuffer, fontHeight, bitmap, BITMAP_W, BITMAP_H, 32, charData);
    if (result <= 0) {
      throw new RuntimeException("Failed to bake font bitmap");
    }

    fontTextureID = glGenTextures();
    glBindTexture(GL_TEXTURE_2D, fontTextureID);
    glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
    glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, BITMAP_W, BITMAP_H, 0, GL_ALPHA, GL_UNSIGNED_BYTE, bitmap);

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
  }

  /**
   * Renders a text string at specified screen coordinates.
   * Uses orthographic projection, expects OpenGL context with no active shader.
   *
   * @param text string to render
   * @param x    screen x position (pixels)
   * @param y    screen y position (pixels, from top)
   */
  public void renderText(String text, float x, float y) {
    glEnable(GL_TEXTURE_2D);
    glBindTexture(GL_TEXTURE_2D, fontTextureID);
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    glPushMatrix();
    glTranslatef(x, y, 0);

    STBTTAlignedQuad quad = STBTTAlignedQuad.malloc();

    FloatBuffer xBuf = BufferUtils.createFloatBuffer(1).put(0, 0f);
    FloatBuffer yBuf = BufferUtils.createFloatBuffer(1).put(0, 0f);

    glBegin(GL_QUADS);
    for (int i = 0; i < text.length(); i++) {
      char c = text.charAt(i);
      if (c < 32 || c >= 128)
        continue; // ignore non-printable

      STBTruetype.stbtt_GetBakedQuad(charData, BITMAP_W, BITMAP_H, c - 32, xBuf, yBuf, quad, true);

      glTexCoord2f(quad.s0(), quad.t0());
      glVertex2f(quad.x0(), quad.y0());

      glTexCoord2f(quad.s1(), quad.t0());
      glVertex2f(quad.x1(), quad.y0());

      glTexCoord2f(quad.s1(), quad.t1());
      glVertex2f(quad.x1(), quad.y1());

      glTexCoord2f(quad.s0(), quad.t1());
      glVertex2f(quad.x0(), quad.y1());
    }
    glEnd();

    glPopMatrix();

    glDisable(GL_BLEND);
    glDisable(GL_TEXTURE_2D);

    quad.free();
  }

  public float getTextWidth(String text) {
    float width = 0;
    FloatBuffer xBuf = BufferUtils.createFloatBuffer(1).put(0, 0f);
    FloatBuffer yBuf = BufferUtils.createFloatBuffer(1).put(0, 0f);

    for (int i = 0; i < text.length(); i++) {
      char c = text.charAt(i);
      if (c < 32 || c >= 128)
        continue; // ignore non-printable

      STBTTAlignedQuad quad = STBTTAlignedQuad.malloc();
      STBTruetype.stbtt_GetBakedQuad(charData, BITMAP_W, BITMAP_H, c - 32, xBuf, yBuf, quad, true);
      width += quad.x1() - quad.x0();
      quad.free();
    }
    return width;
  }

  public float getTextHeight() {
    return charData.get(0).x1() - charData.get(0).x0();
  }

  public void cleanup() {
    glDeleteTextures(fontTextureID);
  }
}
