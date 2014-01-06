import java.util.*;
class Test5 {
  public static void main(String[] args) {
    int upper = 1;
    int lower = 2;
    int dir = 0;
    dir |= upper;
    dir = 1 | 2 | 4 | 8 | 16 | 32 | 64 | 128;
    System.out.println("d=" + dir);
    int result = dir & 64;
    System.out.println("r=" + result);
    if ((int)(dir & 64) > 0) {
      System.out.println("flag on");
    }
  }
}
