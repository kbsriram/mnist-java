package org.kbs;

import org.tensorflow.Graph;
import org.tensorflow.Operation;
import org.tensorflow.Output;

public class MatMul implements Input {
  public static MatMul create(Graph g, String name, Input a, Input b) {
    return new MatMul(
        g.opBuilder("MatMul", name).addInput(a.asOutput()).addInput(b.asOutput()).build());
  }

  @Override
  public Output asOutput() {
    return op.output(0);
  }

  private MatMul(Operation op) {
    this.op = op;
  }

  private final Operation op;
}
