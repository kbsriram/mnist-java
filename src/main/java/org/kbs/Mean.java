package org.kbs;

import org.tensorflow.Graph;
import org.tensorflow.Operation;
import org.tensorflow.Output;

public class Mean implements Input {
  public static Mean create(Graph g, String name, Input input, Input indices) {
    return new Mean(
        g.opBuilder("Mean", name).addInput(input.asOutput()).addInput(indices.asOutput()).build());
  }

  @Override
  public Output asOutput() {
    return op.output(0);
  }

  private Mean(Operation op) {
    this.op = op;
  }

  private final Operation op;
}
