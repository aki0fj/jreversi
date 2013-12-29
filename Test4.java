import java.util.*;
class Test4 {
  public static void main(String[] args) {
    for (int y=1; y < 9; y++) {
      for (int x=1; x < 9; x++) {
        Disc dc = new Disc(x,y,0);
        System.out.println(dc.to_s() + dc.x + "," + dc.y + "=" + dc.getPos());
      }
    }
  }
}
