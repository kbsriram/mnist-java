package org.kbs;

import org.tensorflow.Graph;
import org.tensorflow.Operation;
import org.tensorflow.Output;

public class Add implements Input {
  public static Add create(Graph g, String name, Input a, Input b) {
    return new Add(g.opBuilder("Add", name).addInput(a.asOutput()).addInput(b.asOutput()).build());
  }

  @Override
  public Output asOutput() {
    return op.output(0);
  }

  private Add(Operation op) {
    this.op = op;
  }

  private final Operation op;
}
