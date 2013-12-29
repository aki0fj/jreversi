import java.util.*;
class TestPatternImage {
  public static void main(String[] args) {
    Board bd = new Board();
    String row;
    for (int i=0; Pattern.list[i][0] >= 0; i++) {
      int idx = bd.getPattern(i);
      row = "";
      for (int j = 0; Pattern.list[i][j] >= 0; j++) {
        int num = idx % 3;
        idx /= 3;
        row += "" + num;
      }
      System.out.println(row);
    }
  }
}
