import java.util.*;

public class ColorStrage {
  private Map<Integer, Integer> map;

  public ColorStrage() {
    map = new HashMap<Integer, Integer>(4);
  }

  public int get(int color) {
    return map.get(color);
  }

  public void set(int color, int value) {
    map.put(color, value);
  }
}
