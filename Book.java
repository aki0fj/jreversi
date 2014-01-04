import java.io.*;
public class Book {
  public boolean testBook(String fileName) {
    int tournament, black, white;
    byte score, theoricScore;
    byte[] readHead = new byte[16];
    byte[] readBuff = new byte[68];
    try {
      DataInputStream dis = new DataInputStream(new FileInputStream(fileName));
      if (dis.read(readHead) <= 0) return false;
      int num = 0;
      while (dis.read(readBuff) > 0) {
        int idx = 0;
        String str = "";
        tournament = (int)readBuff[idx] & 0x000000ff; idx++;
        tournament |= ((int)readBuff[idx] << 8) & 0x0000ff00; idx++;
        str += "tn=" + tournament;
        black = (int)readBuff[idx] & 0x000000ff; idx++;
        black |= ((int)readBuff[idx] << 8) & 0x0000ff00; idx++;
        str += ",b=" + black;
        white = (int)readBuff[idx] & 0x000000ff; idx++;
        white |= ((int)readBuff[idx] << 8) & 0x0000ff00; idx++;
        str += ",w=" + white;
        score = readBuff[idx]; idx++;
        str += ",s=" + score;
        theoricScore = readBuff[idx]; idx++;
        str += ",ts=" + theoricScore + ",";

        num++;
        System.out.println("no." + num + ":" + str);
        Board board = new Board();
        Console console = new Console();
        while (board.getTurn() <= Board.MAX_TURNS - Ai.WINLOSE_DEPTH) {
          while (board.getMovablePos().isEmpty()) {
            board.pass();
            System.out.println("pass t=" + board.getTurn());
          }
          int x = ((int)readBuff[idx] / 10);
          int y = ((int)readBuff[idx] % 10);
          Disc disc = new Disc(x, y, board.getCurrentColor());
          board.put(disc.getPos(), disc.color);
          idx++;
        }
        console.viewBoard(board);
        Ai ai = new Ai();
        str = "";
        while (!board.isGameOver()) {
          while (board.getMovablePos().isEmpty()) {
            board.pass();
            System.out.println("pass t=" + board.getTurn());
          }
          Move move = ai.getMove(board);
          if (move == null) {
            System.out.println("cannot getMove t=" + board.getTurn() + ",bp=" + readBuff[idx]);
            System.exit(-1);
          }
          byte pos = (byte)(move.x * 10 + move.y);
          if (pos != readBuff[idx]) {
            System.out.println("pos diff t=" + board.getTurn() + ".p=" + pos + ",bp=" + readBuff[idx]);
            System.exit(-1);
          }
          Disc disc = new Disc(move.x, move.y, board.getCurrentColor());
          board.put(disc.getPos(), disc.color);
          idx++;
          str += "t=" + board.getTurn() + ",p=" + pos + ",e=" + move.eval;
        }
        System.out.println("no." + num + ":" + str);
      }
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
    }
    return true;
  }
  public boolean displayBook(String fileName) {
    int tournament, black, white;
    byte score, theoricScore;
    byte[] readHead = new byte[16];
    byte[] readBuff = new byte[68];
    try {
      DataInputStream dis = new DataInputStream(new FileInputStream(fileName));
      if (dis.read(readHead) <= 0) return false;
      int num = 0;
      while (dis.read(readBuff) > 0) {
        int idx = 0;
        String str = "";
        tournament = (int)readBuff[idx] & 0x000000ff; idx++;
        tournament |= ((int)readBuff[idx] << 8) & 0x0000ff00; idx++;
        str += "tn=" + tournament;
        black = (int)readBuff[idx] & 0x000000ff; idx++;
        black |= ((int)readBuff[idx] << 8) & 0x0000ff00; idx++;
        str += ",b=" + black;
        white = (int)readBuff[idx] & 0x000000ff; idx++;
        white |= ((int)readBuff[idx] << 8) & 0x0000ff00; idx++;
        str += ",w=" + white;
        score = readBuff[idx]; idx++;
        str += ",s=" + score;
        theoricScore = readBuff[idx]; idx++;
        str += ",ts=" + theoricScore + ",";
        for (; idx < 68; idx++) {
          byte[] asc = "a".getBytes("US-ASCII");
          asc[0] += (readBuff[idx] / 10) - 1;
          str += new String(asc, "US-ASCII");
          str += (readBuff[idx] % 10);
        }
        num++;
        System.out.println("no." + num + ":" + str);
      }
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
    }
    return true;
  }
}
