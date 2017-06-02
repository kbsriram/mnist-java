package org.kbs;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.tensorflow.DataType;
import org.tensorflow.Graph;
import org.tensorflow.Operation;
import org.tensorflow.Session;
import org.tensorflow.Shape;
import org.tensorflow.Tensor;

public class Main {
  public static void main(String args[]) throws IOException {
    try (Graph g = new Graph();
        Session sess = new Session(g); ) {

      Ops ops = Ops.create(g);

      // Create the model
      Input x = ops.placeholder(DataType.FLOAT, Shape.make(-1, 784));
      Input weight = ops.variable(zeros(784, 10));
      Input b = ops.variable(new float[10]);
      Input y = ops.math().add(ops.math().matMul(x, weight), b);

      // Loss
      Input y_ = ops.placeholder(DataType.FLOAT, Shape.make(-1, 10));
      Input cross_entropy =
          ops.math().mean(ops.nn().softmaxCrossEntropyWithLogits(y, y_).loss(), ops.constant(0));

      //  and optimizer
      // I can't train yet :-(

      Session.Runner r = sess.runner();
      for (Operation op : Variable.inits()) {
        r.addTarget(op);
      }
      r.run();

      // Just find the cross_entropy on the untrained model for now.
      for (int i = 0; i < 100; i++) {
        Mnist mnist = Mnist.loadTrain(new File("mnist_data"), i * 100, 100);
        try (Tensor xs = Tensor.create(mnist.images());
            Tensor ys_ = Tensor.create(mnist.labels()); ) {
          dump(
              sess.runner()
                  .feed(x.asOutput(), xs)
                  .feed(y_.asOutput(), ys_)
                  .fetch(cross_entropy.asOutput())
                  .run());
        }
      }
    }
  }

  private static void dump(List<Tensor> tensors) {
    for (Tensor tensor : tensors) {
      System.out.println(tensor.floatValue());
    }
  }

  private static final float[][] zeros(int dim1, int dim2) {
    float[][] result = new float[dim1][];
    for (int i = 0; i < dim1; i++) {
      result[i] = new float[dim2];
    }
    return result;
  }
}
