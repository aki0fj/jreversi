import java.util.*;
class Test {
public static void main(String[] args) {
  Evaluator eval = new Evaluator();
  for (int i=0; i < Math.pow(3, 8); i++) {
    System.out.println(eval.MirrorLine[i]);
  }
}
}
