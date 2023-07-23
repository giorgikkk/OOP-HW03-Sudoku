import java.util.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {
    // Provided grid data for main/testing
    // The instance variable strategy is up to you.

    // Provided easy 1 6 grid
    // (can paste this text into the GUI too)

    private int[][] grid;
    private int[][] resultGrid;
    private List<Spot> spots;
    private int numberOfSolutions;
    private long solvingStartTime;
    private long solvingEndTime;

    public static final int[][] easyGrid = Sudoku.stringsToGrid(
            "1 6 4 0 0 0 0 0 2",
            "2 0 0 4 0 3 9 1 0",
            "0 0 5 0 8 0 4 0 7",
            "0 9 0 0 0 6 5 0 0",
            "5 0 0 1 0 2 0 0 8",
            "0 0 8 9 0 0 0 3 0",
            "8 0 9 0 4 0 2 0 0",
            "0 7 3 5 0 9 0 0 1",
            "4 0 0 0 0 0 6 7 9");


    // Provided medium 5 3 grid
    public static final int[][] mediumGrid = Sudoku.stringsToGrid(
            "530070000",
            "600195000",
            "098000060",
            "800060003",
            "400803001",
            "700020006",
            "060000280",
            "000419005",
            "000080079");

    // Provided hard 3 7 grid
    // 1 solution this way, 6 solutions if the 7 is changed to 0
    public static final int[][] hardGrid = Sudoku.stringsToGrid(
            "3 7 0 0 0 0 0 8 0",
            "0 0 1 0 9 3 0 0 0",
            "0 4 0 7 8 0 0 0 3",
            "0 9 3 8 0 0 0 1 2",
            "0 0 0 0 4 0 0 0 0",
            "5 2 0 0 0 6 7 9 0",
            "6 0 0 0 2 1 0 4 0",
            "0 0 0 5 3 0 9 0 0",
            "0 3 0 0 0 0 0 5 1");


    public static final int SIZE = 9;  // size of the whole 9x9 puzzle
    public static final int PART = 3;  // size of each 3x3 part
    public static final int MAX_SOLUTIONS = 100;

    // Provided various static utility methods to
    // convert data formats to int[][] grid.

    /**
     * Returns a 2-d grid parsed from strings, one string per row.
     * The "..." is a Java 5 feature that essentially
     * makes "rows" a String[] array.
     * (provided utility)
     *
     * @param rows array of row strings
     * @return grid
     */
    public static int[][] stringsToGrid(String... rows) {
        int[][] result = new int[rows.length][];
        for (int row = 0; row < rows.length; row++) {
            result[row] = stringToInts(rows[row]);
        }
        return result;
    }


    /**
     * Given a single string containing 81 numbers, returns a 9x9 grid.
     * Skips all the non-numbers in the text.
     * (provided utility)
     *
     * @param text string of 81 numbers
     * @return grid
     */
    public static int[][] textToGrid(String text) {
        int[] nums = stringToInts(text);
        if (nums.length != SIZE * SIZE) {
            throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
        }

        int[][] result = new int[SIZE][SIZE];
        int count = 0;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                result[row][col] = nums[count];
                count++;
            }
        }
        return result;
    }


    /**
     * Given a string containing digits, like "1 23 4",
     * returns an int[] of those digits {1 2 3 4}.
     * (provided utility)
     *
     * @param string string containing ints
     * @return array of ints
     */
    public static int[] stringToInts(String string) {
        int[] a = new int[string.length()];
        int found = 0;
        for (int i = 0; i < string.length(); i++) {
            if (Character.isDigit(string.charAt(i))) {
                a[found] = Integer.parseInt(string.substring(i, i + 1));
                found++;
            }
        }
        int[] result = new int[found];
        System.arraycopy(a, 0, result, 0, found);
        return result;
    }


    // Provided -- the deliverable main().
    // You can edit to do easier cases, but turn in
    // solving hardGrid.
    public static void main(String[] args) {
        Sudoku sudoku;
        sudoku = new Sudoku(hardGrid);

        System.out.println(sudoku); // print the raw problem
        int count = sudoku.solve();
        System.out.println("solutions:" + count);
        System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
        System.out.println(sudoku.getSolutionText());
    }


    /**
     * Sets up based on the given ints.
     */
    public Sudoku(int[][] ints) {
        this.grid = ints;
        resultGrid = new int[SIZE][SIZE];
        spots = new ArrayList<>();
        numberOfSolutions = 0;

        initializeSpots();
    }

    private void initializeSpots() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j] == 0) {
                    Spot spot = new Spot(i, j);
                    Set<Integer> possibleAssignNumbers = new HashSet<>();
                    findPossibleAssignNumbers(spot, possibleAssignNumbers);
                    spot.setPossibleSpots(possibleAssignNumbers);
                    spots.add(spot);
                }
            }
        }

        sortSpots();

    }

    private void sortSpots() {
        spots.sort(new Comparator<Spot>() {
            @Override
            public int compare(Spot o1, Spot o2) {
                return o1.getPossibleSpots().size() - o2.getPossibleSpots().size();
            }
        });
    }

    private void findPossibleAssignNumbers(Spot spot, Set<Integer> possibleAssignNumbers) {
        int[] allPossibleAssignNumbers = new int[SIZE + 1];
        for (int i = 0; i <= SIZE; i++) {
            allPossibleAssignNumbers[i] = i;
        }

        removeSameRowNumbers(spot, allPossibleAssignNumbers);
        removeSameColumnNumbers(spot, allPossibleAssignNumbers);
        removeSameSquareNumbers(spot, allPossibleAssignNumbers);

        for (int i = 1; i <= SIZE; i++) {
            if (allPossibleAssignNumbers[i] == -1) {
                continue;
            }

            possibleAssignNumbers.add(i);
        }
    }

    private void removeSameRowNumbers(Spot spot, int[] allPossibleAssignNumbers) {
        for (int i = 0; i < SIZE; i++) {
            if (allPossibleAssignNumbers[grid[spot.getRow()][i]] != -1) {
                allPossibleAssignNumbers[grid[spot.getRow()][i]] = -1;
            }
        }
    }

    private void removeSameColumnNumbers(Spot spot, int[] allPossibleAssignNumbers) {
        for (int i = 0; i < SIZE; i++) {
            if (allPossibleAssignNumbers[grid[i][spot.getColumn()]] != -1) {
                allPossibleAssignNumbers[grid[i][spot.getColumn()]] = -1;
            }
        }
    }

    private void removeSameSquareNumbers(Spot spot, int[] allPossibleAssignNumbers) {
        int x = spot.getRow() - spot.getRow() % PART;
        int y = spot.getColumn() - spot.getColumn() % PART;

        for (int i = x; i < x + PART; i++) {
            for (int j = y; j < y + PART; j++) {
                if (allPossibleAssignNumbers[grid[i][j]] != -1) {
                    allPossibleAssignNumbers[grid[i][j]] = -1;
                }
            }
        }
    }


    public Sudoku(String text) {
        this(textToGrid(text));
    }


    /**
     * Solves the puzzle, invoking the underlying recursive search.
     */
    public int solve() {
        solvingStartTime = System.currentTimeMillis();

        Spot[] iterSpots = spots.toArray(new Spot[0]);
        int spotsSize = spots.size();
        int index = 0;
        recSolve(iterSpots, index, spotsSize);

        solvingEndTime = System.currentTimeMillis();

        return numberOfSolutions;
    }

    private void recSolve(Spot[] iterSpots, int index, int spotsSize) {
        if (numberOfSolutions == MAX_SOLUTIONS) {
            return;
        }

        if (index == spotsSize) {
            numberOfSolutions++;
            if (numberOfSolutions == 1) {
                saveFirstSolution();
            }
            return;
        }

        Spot s = iterSpots[index];
        Set<Integer> currentPossibleAssignNumbers = s.getPossibleSpots();
        for (final int n : currentPossibleAssignNumbers) {
            if (canAssign(s, n)) {
                s.set(n);
                recSolve(iterSpots, index + 1, spotsSize);
                s.set(0);
            }
        }
    }

    private boolean canAssign(Spot s, int n) {
        if (alreadyAssignInRow(s, n)) {
            return false;
        }

        if (alreadyAssignInColumn(s, n)) {
            return false;
        }

        if (alreadyAssignInSquare(s, n)) {
            return false;
        }

        return true;
    }

    private boolean alreadyAssignInRow(Spot s, int n) {
        for (int i = 0; i < SIZE; i++) {
            if (grid[s.getRow()][i] == n) {
                return true;
            }
        }

        return false;
    }

    private boolean alreadyAssignInColumn(Spot s, int n) {
        for (int i = 0; i < SIZE; i++) {
            if (grid[i][s.getColumn()] == n) {
                return true;
            }
        }

        return false;
    }

    private boolean alreadyAssignInSquare(Spot s, int n) {
        int x = s.getRow() - s.getRow() % PART;
        int y = s.getColumn() - s.getColumn() % PART;

        for (int i = x; i < x + PART; i++) {
            for (int j = y; j < y + PART; j++) {
                if (grid[i][j] == n) {
                    return true;
                }
            }
        }

        return false;
    }

    private void saveFirstSolution() {
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(grid[i], 0, resultGrid[i], 0, SIZE);
        }
    }

    public String getSolutionText() {
        StringBuilder sb = new StringBuilder();

        if (numberOfSolutions > 0) {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    sb.append(this.resultGrid[i][j]);
                    sb.append(" ");
                }

                sb.append("\n");
            }
        }

        return sb.toString();
    }

    public long getElapsed() {
        return solvingEndTime - solvingStartTime;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                sb.append(this.grid[i][j]);
                sb.append(" ");
            }

            sb.append("\n");
        }

        return sb.toString();
    }

    private class Spot {
        private final int row;
        private final int column;
        private Set<Integer> possibleSpots;

        public Spot(int row, int column) {
            this.row = row;
            this.column = column;
        }

        public int getRow() {
            return row;
        }

        public int getColumn() {
            return column;
        }

        public Set<Integer> getPossibleSpots() {
            return possibleSpots;
        }

        public void setPossibleSpots(Set<Integer> possibleSpots) {
            this.possibleSpots = possibleSpots;
        }

        public void set(int value) {
            grid[this.getRow()][this.getColumn()] = value;
        }
    }
}
