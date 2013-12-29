public class Point{
  public int x, y;

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getPos() {
    return (Board.BOARD_SIZE + 1) * this.y + this.x;
  }

  public String to_s() {
    String str = "";
    try {
      byte[] asc = "a".getBytes("US-ASCII");
      asc[0] += this.x - 1;
      str += new String(asc, "US-ASCII");
    } catch (Exception e) {
      e.printStackTrace();
    }
    str += this.y;
    return str;
  }
}
