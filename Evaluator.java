import java.util.*;
import java.io.*;

public class Evaluator implements Serializable{
  final int DISK_VALUE = 1000;
  final double UPDATE_RATIO = 0.005;
  final int MAX_PATTERN_VALUE = DISK_VALUE * 20;
  final int MIN_FREQUENCY = 10;  //Reqire appear times for update value
  final int POW_3_0 = 1;
  final int POW_3_1 = 3;
  final int POW_3_2 = 9;
  final int POW_3_3 = 27;
  final int POW_3_4 = 81;
  final int POW_3_5 = 243;
  final int POW_3_6 = 729;
  final int POW_3_7 = 2187;
  final int POW_3_8 = 6561;
  final int POW_3_9 = 19683;
  final int POW_3_10 = 59049;

  final int PatternLine4 = 0;
  final int PatternLine3 = 1;
  final int PatternLine2 = 2;
  final int PatternDiag8 = 3;
  final int PatternDiag7 = 4;
  final int PatternDiag6 = 5;
  final int PatternDiag5 = 6;
  final int PatternDiag4 = 7;
  final int PatternEdge8 = 8;
  final int PatternCornor8 = 9;
  final int PatternParity = 10;
  final int PatternLast = 11;

  final int[] PatternSize = {
    POW_3_8, POW_3_8, POW_3_8,
    POW_3_8, POW_3_7, POW_3_6, POW_3_5, POW_3_4,
    POW_3_8, POW_3_8, 2, 0
  };

  public int[][] Value;
  public int[][] PatternNum;
  public int[][] PatternSum;
  public int[] MirrorLine;
  public int[] MirrorCorner;

  public Evaluator() {
    Value = new int[PatternLast][POW_3_8];
    PatternNum = new int[PatternLast][POW_3_8];
    PatternSum = new int[PatternLast][POW_3_8];
    MirrorLine = new int[POW_3_8];
    MirrorCorner = new int[POW_3_8];

    for (int i = 0; i < POW_3_8; i++) {
      int mirror_in = i;
      int mirror_out = 0;
      int coeff = POW_3_7;
      for (int j=0; j < 8; j++) {
        mirror_out += mirror_in % 3 * coeff;
        mirror_in /= 3;
        coeff /= 3;
      }
      if (mirror_out < i) {
        MirrorLine[i] = mirror_out;
      } else {
        MirrorLine[i] = i;
      }
    }

    int mirror_corner_coeff[] = { POW_3_2, POW_3_5, POW_3_0, POW_3_3, POW_3_6, POW_3_1, POW_3_4, POW_3_7 };
    for (int i = 0; i < POW_3_8; i++) {
      int mirror_in = i;
      int mirror_out = 0;
      for (int j=0; j < 8; j++) {
        mirror_out += mirror_in % 3 * mirror_corner_coeff[j];
        mirror_in /= 3;
      }
      if (mirror_out < i) {
        MirrorCorner[i] = mirror_out;
      } else {
        MirrorCorner[i] = i;
      }
    }
  }

  public Evaluator Load(String fileName) {
    ObjectInputStream ois;
    Evaluator ev = this;
    try {
      ois = new ObjectInputStream(new FileInputStream(fileName));
      ev = (Evaluator)ois.readObject();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return ev;
  }

  public boolean Save(String fileName) {
    ObjectOutputStream oos;
    try {
      oos = new ObjectOutputStream(new FileOutputStream(fileName));
      oos.writeObject(this);
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }
}
