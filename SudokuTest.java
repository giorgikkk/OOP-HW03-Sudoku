import junit.framework.TestCase;

public class SudokuTest extends TestCase {

    private Sudoku easySudoku;
    private Sudoku mediumSudoku;
    private Sudoku hardSudoku;
    private Sudoku randomSudoku;

    private static final String easyGrid =
            "1 6 4 0 0 0 0 0 2 \n" +
            "2 0 0 4 0 3 9 1 0 \n" +
            "0 0 5 0 8 0 4 0 7 \n" +
            "0 9 0 0 0 6 5 0 0 \n" +
            "5 0 0 1 0 2 0 0 8 \n" +
            "0 0 8 9 0 0 0 3 0 \n" +
            "8 0 9 0 4 0 2 0 0 \n" +
            "0 7 3 5 0 9 0 0 1 \n" +
            "4 0 0 0 0 0 6 7 9";

    private static final int[][] RandomGrid = Sudoku.stringsToGrid(
            "3 0 0 0 0 0 0 8 0",
            "0 0 1 0 9 3 0 0 0",
            "0 4 0 7 8 0 0 0 3",
            "0 9 3 8 0 0 0 1 2",
            "0 0 0 0 4 0 0 0 0",
            "5 2 0 0 0 6 7 9 0",
            "6 0 0 0 2 1 0 4 0",
            "0 0 0 5 3 0 9 0 0",
            "0 3 0 0 0 0 0 5 1");

    protected void setUp() throws Exception {
        super.setUp();

        easySudoku = new Sudoku(easyGrid);
        mediumSudoku = new Sudoku(Sudoku.mediumGrid);
        hardSudoku = new Sudoku(Sudoku.hardGrid);
        randomSudoku = new Sudoku(RandomGrid);
    }


    public void testSolve() {
        assertEquals(1, easySudoku.solve());
        assertEquals(1, mediumSudoku.solve());
        assertEquals(1, hardSudoku.solve());
        assertEquals(6, randomSudoku.solve());
    }


    public void testGetSolutionText() {
        easySudoku.solve();
        assertEquals(easySudoku.getSolutionText(), "1 6 4 7 9 5 3 8 2 \n" +
                                                          "2 8 7 4 6 3 9 1 5 \n" +
                                                          "9 3 5 2 8 1 4 6 7 \n" +
                                                          "3 9 1 8 7 6 5 2 4 \n" +
                                                          "5 4 6 1 3 2 7 9 8 \n" +
                                                          "7 2 8 9 5 4 1 3 6 \n" +
                                                          "8 1 9 6 4 7 2 5 3 \n" +
                                                          "6 7 3 5 2 9 8 4 1 \n" +
                                                          "4 5 2 3 1 8 6 7 9 \n");

        mediumSudoku.solve();
        assertEquals(mediumSudoku.getSolutionText(), "5 3 4 6 7 8 9 1 2 \n" +
                                                            "6 7 2 1 9 5 3 4 8 \n" +
                                                            "1 9 8 3 4 2 5 6 7 \n" +
                                                            "8 5 9 7 6 1 4 2 3 \n" +
                                                            "4 2 6 8 5 3 7 9 1 \n" +
                                                            "7 1 3 9 2 4 8 5 6 \n" +
                                                            "9 6 1 5 3 7 2 8 4 \n" +
                                                            "2 8 7 4 1 9 6 3 5 \n" +
                                                            "3 4 5 2 8 6 1 7 9 \n");

        hardSudoku.solve();
        assertEquals(hardSudoku.getSolutionText(), "3 7 5 1 6 2 4 8 9 \n" +
                                                          "8 6 1 4 9 3 5 2 7 \n" +
                                                          "2 4 9 7 8 5 1 6 3 \n" +
                                                          "4 9 3 8 5 7 6 1 2 \n" +
                                                          "7 1 6 2 4 9 8 3 5 \n" +
                                                          "5 2 8 3 1 6 7 9 4 \n" +
                                                          "6 5 7 9 2 1 3 4 8 \n" +
                                                          "1 8 2 5 3 4 9 7 6 \n" +
                                                          "9 3 4 6 7 8 2 5 1 \n");

        randomSudoku.solve();
        assertEquals(randomSudoku.getSolutionText(), "3 5 7 1 6 2 4 8 9 \n" +
                                                            "8 6 1 4 9 3 2 7 5 \n" +
                                                            "2 4 9 7 8 5 1 6 3 \n" +
                                                            "4 9 3 8 5 7 6 1 2 \n" +
                                                            "1 7 6 2 4 9 5 3 8 \n" +
                                                            "5 2 8 3 1 6 7 9 4 \n" +
                                                            "6 8 5 9 2 1 3 4 7 \n" +
                                                            "7 1 4 5 3 8 9 2 6 \n" +
                                                            "9 3 2 6 7 4 8 5 1 \n");
    }


    public void testElapsedTime() {
        long easyStart = System.currentTimeMillis();
        easySudoku.solve();
        long easyEnd = System.currentTimeMillis();
        assertEquals(easyEnd - easyStart, easySudoku.getElapsed());

        long mediumStart = System.currentTimeMillis();
        mediumSudoku.solve();
        long mediumEnd = System.currentTimeMillis();
        assertEquals(mediumEnd - mediumStart, mediumSudoku.getElapsed());

        long hardStart = System.currentTimeMillis();
        hardSudoku.solve();
        long hardEnd = System.currentTimeMillis();
        assertEquals(hardEnd - hardStart, hardSudoku.getElapsed());

        long randomStart = System.currentTimeMillis();
        randomSudoku.solve();
        long randomEnd = System.currentTimeMillis();
        assertEquals(randomEnd - randomStart, randomSudoku.getElapsed());
    }


    public void testToString() {
        assertEquals(easySudoku.toString(),  "1 6 4 0 0 0 0 0 2 \n" +
                                         "2 0 0 4 0 3 9 1 0 \n" +
                                         "0 0 5 0 8 0 4 0 7 \n" +
                                         "0 9 0 0 0 6 5 0 0 \n" +
                                         "5 0 0 1 0 2 0 0 8 \n" +
                                         "0 0 8 9 0 0 0 3 0 \n" +
                                         "8 0 9 0 4 0 2 0 0 \n" +
                                         "0 7 3 5 0 9 0 0 1 \n" +
                                         "4 0 0 0 0 0 6 7 9 \n");

        assertEquals(mediumSudoku.toString(), "5 3 0 0 7 0 0 0 0 \n" +
                                                     "6 0 0 1 9 5 0 0 0 \n" +
                                                     "0 9 8 0 0 0 0 6 0 \n" +
                                                     "8 0 0 0 6 0 0 0 3 \n" +
                                                     "4 0 0 8 0 3 0 0 1 \n" +
                                                     "7 0 0 0 2 0 0 0 6 \n" +
                                                     "0 6 0 0 0 0 2 8 0 \n" +
                                                     "0 0 0 4 1 9 0 0 5 \n" +
                                                     "0 0 0 0 8 0 0 7 9 \n");

        assertEquals(hardSudoku.toString(), "3 7 0 0 0 0 0 8 0 \n" +
                                                   "0 0 1 0 9 3 0 0 0 \n" +
                                                   "0 4 0 7 8 0 0 0 3 \n" +
                                                   "0 9 3 8 0 0 0 1 2 \n" +
                                                   "0 0 0 0 4 0 0 0 0 \n" +
                                                   "5 2 0 0 0 6 7 9 0 \n" +
                                                   "6 0 0 0 2 1 0 4 0 \n" +
                                                   "0 0 0 5 3 0 9 0 0 \n" +
                                                   "0 3 0 0 0 0 0 5 1 \n");

        assertEquals(randomSudoku.toString(), "3 0 0 0 0 0 0 8 0 \n" +
                                                     "0 0 1 0 9 3 0 0 0 \n" +
                                                     "0 4 0 7 8 0 0 0 3 \n" +
                                                     "0 9 3 8 0 0 0 1 2 \n" +
                                                     "0 0 0 0 4 0 0 0 0 \n" +
                                                     "5 2 0 0 0 6 7 9 0 \n" +
                                                     "6 0 0 0 2 1 0 4 0 \n" +
                                                     "0 0 0 5 3 0 9 0 0 \n" +
                                                     "0 3 0 0 0 0 0 5 1 \n");
    }
}
