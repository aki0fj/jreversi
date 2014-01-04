import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

class HumanPlayer extends Player {
  public void onTurn(Board board) {
    if (board.getMovablePos().isEmpty()) {
      System.out.println("pass your turn");
      board.pass();
      return;
    }

    while(true) {
      System.out.print("input point ex.\"f5\" or (u:undo/x:exit):");
      BufferedReader stdin = 
          new BufferedReader(new InputStreamReader(System.in));
      try {
        String line = stdin.readLine();
        switch (line) {
        case "u":
          System.out.println("undo processing...");
          board.undo();
          return;
        case "x":
          System.out.println("exiting...");
          System.exit(0);
          break;
        default:
          try {
            byte[] asc = line.getBytes("US-ASCII");
            int x = asc[0] - "a".getBytes("US-ASCII")[0] + 1;
            int y = Integer.parseInt(line.substring(1,2));
            int color = board.getCurrentColor();
            Disc disc = new Disc(x, y, color);
            System.out.println("" + disc.x + "," + disc.y + ":" + color);
            if (!board.put(disc.getPos(), color)) {
              System.out.println("cannot put there");
            }
            if (Debug.state) { printPattern(board); }
            return;
          } catch (Exception e) {
            e.printStackTrace();
            System.out.println("invalid input");
          }
          break;
        }
      } catch (IOException e) {
        e.printStackTrace();
        System.exit(-1);
      }
    }
  }

  void printPattern(Board board) {
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
