package org.kbs;

import java.util.HashMap;
import java.util.Map;
import org.tensorflow.DataType;
import org.tensorflow.Graph;
import org.tensorflow.Shape;

public class Ops {
  public static Ops create(Graph g) {
    return new Ops(g);
  }

  public Placeholder placeholder(DataType dtype, Shape shape) {
    return Placeholder.create(graph, makeUnique("Placeholder"), dtype, shape);
  }

  public Variable variable(Object init) {
    return Variable.create(graph, makeUnique("Variable"), init);
  }

  public Constant constant(Object value) {
    return Constant.create(graph, makeUnique("Constant"), value);
  }

  public MathOps math() {
    return math;
  }

  public NnOps nn() {
    return nn;
  }

  private Ops(Graph g) {
    graph = g;
  }

  private String makeUnique(String s) {
    int count;
    String result;
    if (ids.containsKey(s)) {
      count = ids.get(s);
      result = String.format("%s_%d", s, count++);
    } else {
      count = 1;
      result = s;
    }
    ids.put(s, count);
    return result;
  }

  private final Graph graph;
  private final Map<String, Integer> ids = new HashMap<String, Integer>();
  private final MathOps math = new MathOps();
  private final NnOps nn = new NnOps();

  public final class MathOps {
    private MathOps() {}

    public Add add(Input a, Input b) {
      return Add.create(graph, makeUnique("Add"), a, b);
    }

    public MatMul matMul(Input a, Input b) {
      return MatMul.create(graph, makeUnique("MatMul"), a, b);
    }

    public Mean mean(Input input, Input indices) {
      return Mean.create(graph, makeUnique("Mean"), input, indices);
    }
  }

  public final class NnOps {
    private NnOps() {}

    public SoftmaxCrossEntropyWithLogits softmaxCrossEntropyWithLogits(
        Input features, Input labels) {
      return SoftmaxCrossEntropyWithLogits.create(
          graph, makeUnique("SoftmaxCrossEntropyWithLogits"), features, labels);
    }
  }
}
