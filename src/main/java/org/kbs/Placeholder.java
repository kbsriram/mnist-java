package org.kbs;

import org.tensorflow.DataType;
import org.tensorflow.Graph;
import org.tensorflow.Operation;
import org.tensorflow.Output;
import org.tensorflow.Shape;

public class Placeholder implements Input {
  public static Placeholder create(Graph g, String name, DataType dtype, Shape shape) {
    return new Placeholder(
        g.opBuilder("Placeholder", name).setAttr("dtype", dtype).setAttr("shape", shape).build());
  }

  @Override
  public Output asOutput() {
    return op.output(0);
  }

  private Placeholder(Operation op) {
    this.op = op;
  }

  private final Operation op;
}
