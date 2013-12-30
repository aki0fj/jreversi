public class Console extends Board {
  public void viewBoard(Board board) {
    int x, y;
    String row;
    Disc disc;
    int[] rowCount = {1,2,3,4,5,6,7,8};
    int[] contents = board.getContents();
    System.out.println(" A B C D E F G H");
    for (y=1; y < BOARD_SIZE+1; y++) {
      row = "";
      for (x=0; x < BOARD_SIZE+1; x++) {
        disc = new Disc(x, y, 0);
        switch (contents[disc.getPos()]) {
        case WALL: 
          if (x == 0) {
            row += ("" + y);
          }
          break;
        case BLACK: row += "x "; break;
        case WHITE: row += "o "; break;
        case EMPTY: row += "  "; break;
        }
      }
      System.out.println(row);
    }
    row = "x: " + board.getCountDiscs(BLACK) + " ";
    row += "o: " + board.getCountDiscs(WHITE) + " ";
    row += "EMPTY: " + board.getCountDiscs(EMPTY);
    System.out.println(row);
    row = "turn=" + (board.getTurn() + 1) + " ";
    if (board.getCurrentColor() == BLACK) {
      row += "Black(x)";
    } else {
      row += "White(o)";
    }
    System.out.println(row);
  }
}
