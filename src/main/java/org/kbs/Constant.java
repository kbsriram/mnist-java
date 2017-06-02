package org.kbs;

import org.tensorflow.Graph;
import org.tensorflow.Operation;
import org.tensorflow.Output;
import org.tensorflow.Tensor;

public class Constant implements Input {
  public static Constant create(Graph g, String name, Object v) {
    try (Tensor t = Tensor.create(v)) {
      return new Constant(
          g.opBuilder("Const", name).setAttr("dtype", t.dataType()).setAttr("value", t).build());
    }
  }

  @Override
  public Output asOutput() {
    return op.output(0);
  }

  private Constant(Operation op) {
    this.op = op;
  }

  private final Operation op;
}
