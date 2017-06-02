package org.kbs;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.zip.GZIPInputStream;

final class Mnist {

  static Mnist loadTrain(File root, int skip, int num) throws IOException {
    return loadDataset(root, "train", skip, num);
  }

  static Mnist loadDataset(File root, String base, int skip, int num) throws IOException {

    // First, images.
    File src = maybeLoad(root, base + "-images-idx3-ubyte.gz");
    // System.out.println("Reading " + src);
    float[][] images;
    try (DataInputStream in =
        new DataInputStream(
            new GZIPInputStream(new BufferedInputStream(new FileInputStream(src))))) {
      if (in.readInt() != 0x803) {
        throw new IOException("bad magic");
      }
      int count = in.readInt();
      int rows = in.readInt();
      int cols = in.readInt();
      int size = rows * cols;
      images = new float[num][];
      // System.out.println(
      // String.format("%d-%d/%d %dx%d images...", skip, skip + num, count, rows, cols));
      in.skip(size * skip);

      for (int i = 0; i < num; i++) {
        images[i] = new float[size];
        for (int j = 0; j < size; j++) {
          images[i][j] = (float) (in.readUnsignedByte());
        }
      }
    }
    // Next, labels
    src = maybeLoad(root, base + "-labels-idx1-ubyte.gz");
    // System.out.println("Reading " + src);
    float[][] labels;
    try (DataInputStream in =
        new DataInputStream(
            new GZIPInputStream(new BufferedInputStream(new FileInputStream(src))))) {
      if (in.readInt() != 0x801) {
        throw new IOException("bad magic");
      }
      int count = in.readInt();
      labels = new float[num][];
      in.skip(skip);
      // System.out.println(String.format("%d-%d/%d labels", skip, skip + num, count));
      for (int i = 0; i < num; i++) {
        labels[i] = new float[10]; // one-shot encoding.
        labels[i][in.readUnsignedByte()] = 1f;
      }
    }

    return new Mnist(images, labels);
  }

  float[][] images() {
    // modifiable, but ok for PoC
    return images;
  }

  float[][] labels() {
    // modifiable but ok for PoC
    return labels;
  }

  private Mnist(float[][] images, float[][] labels) {
    this.images = images;
    this.labels = labels;
  }

  private static File maybeLoad(File root, String file) throws IOException {
    File path = new File(root, file);
    if (path.canRead()) {
      return path;
    }
    try {
      root.mkdirs();
      Util.download(new URL(BASE_URL + file), path);
      return path;
    } catch (IOException any) {
      path.delete();
      throw any;
    }
  }

  private final float[][] images;
  private final float[][] labels;

  private static final String BASE_URL = "http://yann.lecun.com/exdb/mnist/";
}
