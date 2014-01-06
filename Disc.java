public class Disc extends Point {
  public int color, flipFlg;
  
  public Disc(int x, int y, int color) {
    super(x, y);
    this.color = color;
    this.flipFlg = 0;
  }

  public Disc(int x, int y, int color, int flipFlg) {
    super(x, y);
    this.color = color;
    this.flipFlg = flipFlg;
  }

}
