public class Pattern {

  //positions in board
  static final int A1 = 10;
  static final int B1 = 11;
  static final int C1 = 12;
  static final int D1 = 13;
  static final int E1 = 14;
  static final int F1 = 15;
  static final int G1 = 16;
  static final int H1 = 17;

  static final int A2 = 19;
  static final int B2 = 20;
  static final int C2 = 21;
  static final int D2 = 22;
  static final int E2 = 23;
  static final int F2 = 24;
  static final int G2 = 25;
  static final int H2 = 26;

  static final int A3 = 28;
  static final int B3 = 29;
  static final int C3 = 30;
  static final int D3 = 31;
  static final int E3 = 32;
  static final int F3 = 33;
  static final int G3 = 34;
  static final int H3 = 35;

  static final int A4 = 37;
  static final int B4 = 38;
  static final int C4 = 39;
  static final int D4 = 40;
  static final int E4 = 41;
  static final int F4 = 42;
  static final int G4 = 43;
  static final int H4 = 44;

  static final int A5 = 46;
  static final int B5 = 47;
  static final int C5 = 48;
  static final int D5 = 49;
  static final int E5 = 50;
  static final int F5 = 51;
  static final int G5 = 52;
  static final int H5 = 53;

  static final int A6 = 55;
  static final int B6 = 56;
  static final int C6 = 57;
  static final int D6 = 58;
  static final int E6 = 59;
  static final int F6 = 60;
  static final int G6 = 61;
  static final int H6 = 62;

  static final int A7 = 64;
  static final int B7 = 65;
  static final int C7 = 66;
  static final int D7 = 67;
  static final int E7 = 68;
  static final int F7 = 69;
  static final int G7 = 70;
  static final int H7 = 71;

  static final int A8 = 73;
  static final int B8 = 74;
  static final int C8 = 75;
  static final int D8 = 76;
  static final int E8 = 77;
  static final int F8 = 78;
  static final int G8 = 79;
  static final int H8 = 80;

  public static final int LINE4 = 0;
  public static final int LINE3 = 1;
  public static final int LINE2 = 2;
  public static final int DIAG8 = 3;
  public static final int DIAG7 = 4;
  public static final int DIAG6 = 5;
  public static final int DIAG5 = 6;
  public static final int DIAG4 = 7;
  public static final int EDGE8 = 8;
  public static final int CORNER8 = 9;
  public static final int PARITY = 10;
  public static final int LAST = 11;

  public static final int ID_LINE4_1 = 0;
  public static final int ID_LINE4_2 = 1;
  public static final int ID_LINE4_3 = 2;
  public static final int ID_LINE4_4 = 3;
  public static final int ID_LINE3_1 = 4;
  public static final int ID_LINE3_2 = 5;
  public static final int ID_LINE3_3 = 6;
  public static final int ID_LINE3_4 = 7;
  public static final int ID_LINE2_1 = 8;
  public static final int ID_LINE2_2 = 9;
  public static final int ID_LINE2_3 = 10;
  public static final int ID_LINE2_4 = 11;
  public static final int ID_DIAG8_1 = 12;
  public static final int ID_DIAG8_2 = 13;
  public static final int ID_DIAG7_1 = 14;
  public static final int ID_DIAG7_2 = 15;
  public static final int ID_DIAG7_3 = 16;
  public static final int ID_DIAG7_4 = 17;
  public static final int ID_DIAG6_1 = 18;
  public static final int ID_DIAG6_2 = 19;
  public static final int ID_DIAG6_3 = 20;
  public static final int ID_DIAG6_4 = 21;
  public static final int ID_DIAG5_1 = 22;
  public static final int ID_DIAG5_2 = 23;
  public static final int ID_DIAG5_3 = 24;
  public static final int ID_DIAG5_4 = 25;
  public static final int ID_DIAG4_1 = 26;
  public static final int ID_DIAG4_2 = 27;
  public static final int ID_DIAG4_3 = 28;
  public static final int ID_DIAG4_4 = 29;
  public static final int ID_EDGE8_1 = 30;
  public static final int ID_EDGE8_2 = 31;
  public static final int ID_EDGE8_3 = 32;
  public static final int ID_EDGE8_4 = 33;
  public static final int ID_EDGE8_5 = 34;
  public static final int ID_EDGE8_6 = 35;
  public static final int ID_EDGE8_7 = 36;
  public static final int ID_EDGE8_8 = 37;
  public static final int ID_CORNER8_1 = 38;
  public static final int ID_CORNER8_2 = 39;
  public static final int ID_CORNER8_3 = 40;
  public static final int ID_CORNER8_4 = 41;
  public static final int ID_LAST = 42;

  //block pattern in board for evalate
  public static final int list[][] = {
    { A4, B4, C4, D4, E4, F4, G4, H4, -1 }, //No.0
    { A5, B5, C5, D5, E5, F5, G5, H5, -1 }, //No.1
    { D1, D2, D3, D4, D5, D6, D7, D8, -1 }, //No.2
    { E1, E2, E3, E4, E5, E6, E7, E8, -1 }, //No.3
    { A3, B3, C3, D3, E3, F3, G3, H3, -1 }, //No.4
    { A6, B6, C6, D6, E6, F6, G6, H6, -1 }, //No.5
    { C1, C2, C3, C4, C5, C6, C7, C8, -1 }, //No.6
    { F1, F2, F3, F4, F5, F6, F7, F8, -1 }, //No.7
    { A2, B2, C2, D2, E2, F2, G2, H2, -1 }, //No.8
    { A7, B7, C7, D7, E7, F7, G7, H7, -1 }, //No.9
    { B1, B2, B3, B4, B5, B6, B7, B8, -1 }, //No.10
    { G1, G2, G3, G4, G5, G6, G7, G8, -1 }, //No.11
    { A1, B2, C3, D4, E5, F6, G7, H8, -1 }, //No.12
    { A8, B7, C6, D5, E4, F3, G2, H1, -1 }, //No.13
    { A2, B3, C4, D5, E6, F7, G8, -1 }, //No.14
    { B1, C2, D3, E4, F5, G6, H7, -1 }, //No.15
    { A7, B6, C5, D4, E3, F2, G1, -1 }, //No.16
    { B8, C7, D6, E5, F4, G3, H2, -1 }, //No.17
    { A3, B4, C5, D6, E7, F8, -1 }, //No.18
    { C1, D2, E3, F4, G5, H6, -1 }, //No.19
    { A6, B5, C4, D3, E2, F1, -1 }, //No.20
    { C8, D7, E6, F5, G4, H3, -1 }, //No.21
    { A4, B5, C6, D7, E8, -1 }, //No.22
    { D1, E2, F3, G4, H5, -1 }, //No.23
    { A5, B4, C3, D2, E1, -1 }, //No.24
    { D8, E7, F6, G5, H4, -1 }, //No.25
    { A5, B6, C7, D8, -1 }, //No.26
    { E1, F2, G3, H4, -1 }, //No.27
    { A4, B3, C2, D1, -1 }, //No.28
    { E8, F7, G6, H5, -1 }, //No.29
    { B2, G1, F1, E1, D1, C1, B1, A1, -1 }, //No.30
    { G2, B1, C1, D1, E1, F1, G1, H1, -1 }, //No.31
    { B7, G8, F8, E8, D8, C8, B8, A8, -1 }, //No.32
    { G7, B8, C8, D8, E8, F8, G8, H8, -1 }, //No.33
    { B2, A7, A6, A5, A4, A3, A2, A1, -1 }, //No.34
    { B7, A2, A3, A4, A5, A6, A7, A8, -1 }, //No.35
    { G2, H7, H6, H5, H4, H3, H2, H1, -1 }, //No.36
    { G7, H2, H3, H4, H5, H6, H7, H8, -1 }, //No.37
    { B3, A3, C2, B2, A2, C1, B1, A1, -1 }, //No.38
    { G3, H3, F2, G2, H2, F1, G1, H1, -1 }, //No.39
    { B6, A6, C7, B7, A7, C8, B8, A8, -1 }, //No.40
    { G6, H6, F7, G7, H7, F8, G8, H8, -1 }, //No.41
    { -1 }  //No.42
  };

  public static final int[] allPos = {
    A1, A8, H8, H1,
    D3, D6, E3, E6, C4, C5, F4, F5,
    C3, C6, F3, F6,
    D2, D7, E2, E7, B4, B5, G4, G5,
    C2, C7, F2, F7, B3, B6, G3, G6,
    D1, D8, E1, E8, A4, A5, H4, H5,
    C1, C8, F1, F8, A3, A6, H3, H6,
    B2, B7, G2, G7,
    B1, B8, G1, G8, A2, A7, H2, H7,
    D4, D5, E4, E5
  };
}
