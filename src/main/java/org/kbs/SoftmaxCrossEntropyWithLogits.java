package org.kbs;

import org.tensorflow.Graph;
import org.tensorflow.Operation;
import org.tensorflow.Output;

public class SoftmaxCrossEntropyWithLogits {
  public static SoftmaxCrossEntropyWithLogits create(
      Graph g, String name, Input features, Input labels) {
    return new SoftmaxCrossEntropyWithLogits(
        g.opBuilder("SoftmaxCrossEntropyWithLogits", name)
            .addInput(features.asOutput())
            .addInput(labels.asOutput())
            .build());
  }

  public final Input loss() {
    return new Input() {
      @Override
      public Output asOutput() {
        return op.output(0);
      }
    };
  }

  public final Input backprop() {
    return new Input() {
      @Override
      public Output asOutput() {
        return op.output(1);
      }
    };
  }

  private SoftmaxCrossEntropyWithLogits(Operation op) {
    this.op = op;
  }

  private final Operation op;
}
