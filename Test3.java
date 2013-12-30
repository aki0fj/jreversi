import java.util.*;
class Test3 {
  public static void main(String[] args) {
    Board bd = new Board();
    Console cs = new Console();
    cs.viewBoard(bd);
//    for (int j=0; j < Board.BOARD_SIZE; j++) {
//      for (int i=0; i < Board.BOARD_SIZE; i++) {
//        Disc disc = new Disc(i+1, j+1, Board.BLACK);
//        System.out.println("" + disc.x + "," + disc.y + "=" + bd.getCountFlip(disc));
//      }
//    }
  }
}
