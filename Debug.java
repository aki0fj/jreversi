public class Debug {
  public static boolean state = false;
  static {
    if (System.getProperty("debug","off").equals("on")) {state = true;}
  }

  public static void println(String msg) {
    if (state) {System.out.println(msg);}
  }
}
