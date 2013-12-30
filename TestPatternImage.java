import java.util.*;
class TestPatternImage {
  public static void main(String[] args) {
    Disc disc;
    Console cs = new Console();
    Board bd = new Board();
    cs.viewBoard(bd); //view initial board
    printPattern(bd);
    disc = new Disc(8,8,Board.BLACK);
    bd.putPos(disc.getPos(), disc.color);
    cs.viewBoard(bd); //view after put
    printPattern(bd);
    bd.flipPos(disc.getPos());
    cs.viewBoard(bd); //view after flip
    printPattern(bd);
    bd.flipPos(disc.getPos());
    cs.viewBoard(bd); //view after flip
    printPattern(bd);
  }

  static void printPattern(Board board) {
    String row;
    for (int i=0; Pattern.list[i][0] >= 0; i++) {
      int idx = board.getPattern(i);
      if (idx != 0) {
        row = "";
        for (int j = 0; Pattern.list[i][j] >= 0; j++) {
          int num = idx % 3;
          idx /= 3;
          row += "" + num;
        }
        System.out.println("ptn=" + i + ":" + row);
      }
    }
  }
}
