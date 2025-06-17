package stackvisualizer.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;

import org.lwjgl.BufferUtils;



public class ResourceLoader {

  /**
   * Loads a resource as an InputStream from the classpath.
   *
   * @param resourceName the name of the resource to load.
   * @return an InputStream for the resource.
   * @throws RuntimeException if the resource is not found.
   */
  public static InputStream loadResourceAsStream(String resourceName) {
    InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
    if (stream == null) {
      throw new RuntimeException("Resource not found: " + resourceName);
    }
    return stream;
  }

  /**
   * Loads a resource as a URL from the classpath.
   *
   * @param resourceName the name of the resource to load.
   * @return a URL pointing to the resource.
   * @throws RuntimeException if the resource is not found.
   */
  public static URL loadResourceAsURL(String resourceName) {
    URL url = ResourceLoader.class.getClassLoader().getResource(resourceName);
    if (url == null) {
      throw new RuntimeException("Resource not found: " + resourceName);
    }
    return url;
  }

  /**
   * Reads a resource from the classpath into a ByteBuffer.
   *
   * @param resource the name of the resource to read.
   * @param bufferSize the initial size of the ByteBuffer.
   * @return a ByteBuffer containing the resource data.
   * @throws IOException if an I/O error occurs while reading the resource.
   */
  public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
      ByteBuffer buffer;

      try (var source = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource)) {
          if (source == null)
              throw new IOException("Resource not found: " + resource);

          try (var rbc = Channels.newChannel(source)) {
              buffer = BufferUtils.createByteBuffer(bufferSize);

              while (true) {
                  int bytes = rbc.read(buffer);
                  if (bytes == -1)
                      break;
                  if (buffer.remaining() == 0)
                      buffer = resizeBuffer(buffer, buffer.capacity() * 2);
              }
          }
      }

      buffer.flip();
      return buffer;
  }

  /**
   * Resizes a ByteBuffer to a new capacity.
   *
   * @param buffer the ByteBuffer to resize.
   * @param newCapacity the new capacity for the ByteBuffer.
   * @return a new ByteBuffer with the specified capacity containing the data from the original buffer.
   */
  public static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }
}
