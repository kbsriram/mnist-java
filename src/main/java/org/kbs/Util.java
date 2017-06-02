package org.kbs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

final class Util {

  static void download(URL url, File path) throws IOException {
    System.out.println("Downloading " + url + " to " + path + "...");
    try (FileOutputStream out = new FileOutputStream(path);
        InputStream in = url.openStream(); ) {
      byte[] buf = new byte[8192];
      int nread;
      while ((nread = in.read(buf)) > 0) {
        out.write(buf, 0, nread);
      }
    }
  }
}
