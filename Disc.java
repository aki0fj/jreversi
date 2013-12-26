public class Disc extends Point {
  public static final int EMPTY = 0;
  public static final int BLACK = 1;
  public static final int WHITE = -1;
  public static final int WALL = 2;

  public int x, y, color;
  
  public Disc(int x, int y, int color) {
    super(x, y);
    this.color = color;
  }
}