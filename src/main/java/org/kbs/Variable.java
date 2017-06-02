package org.kbs;

import java.util.ArrayList;
import java.util.List;
import org.tensorflow.Graph;
import org.tensorflow.Operation;
import org.tensorflow.Output;

public class Variable implements VariableInput {
  public static Variable create(Graph g, String name, Object init) {
    Constant c = Constant.create(g, name + "/Const", init);
    Variable var =
        new Variable(
            g.opBuilder("VariableV2", name)
                .setAttr("dtype", c.asOutput().dataType())
                .setAttr("shape", c.asOutput().shape())
                .build());
    inits.add(
        g.opBuilder("Assign", name + "/Assign")
            .addInput(var.asOutput())
            .addInput(c.asOutput())
            .build());
    return var;
  }

  @Override
  public Output asOutput() {
    return op.output(0);
  }

  public static Iterable<Operation> inits() {
    return inits;
  }

  private Variable(Operation op) {
    this.op = op;
  }

  private final Operation op;

  private static final List<Operation> inits = new ArrayList<Operation>();
}
