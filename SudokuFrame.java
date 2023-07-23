import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;


public class SudokuFrame extends JFrame {
    private JTextArea sourceTextArea;
    private JTextArea resultTextArea;
    private JButton checkButton;
    private JCheckBox autoCheckBox;
    private Box controlsBox;
    private Sudoku sudoku;


    public SudokuFrame() {
        super("Sudoku Solver");

        setLayout(new BorderLayout(4, 4));

        initializeTextAreas();
        add(sourceTextArea, BorderLayout.WEST);
        add(resultTextArea, BorderLayout.EAST);

        initializeControlsBox();
        add(controlsBox, BorderLayout.SOUTH);

        performCheckButton();
        performSourceTextArea();

        // Could do this:
        // setLocationByPlatform(true);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void initializeTextAreas() {
        sourceTextArea = new JTextArea(15, 20);
        sourceTextArea.setBorder(new TitledBorder("Puzzle"));
        sourceTextArea.setVisible(true);

        resultTextArea = new JTextArea(15, 20);
        resultTextArea.setBorder(new TitledBorder("Solution"));
        resultTextArea.setVisible(true);
        resultTextArea.setEditable(false);
    }

    private void initializeControlsBox() {
        controlsBox = Box.createHorizontalBox();
        checkButton = new JButton("Check");
        checkButton.setVisible(true);

        autoCheckBox = new JCheckBox("Auto Check");
        autoCheckBox.setVisible(true);
        autoCheckBox.setSelected(true);

        controlsBox.add(checkButton);
        controlsBox.add(Box.createHorizontalStrut(5));
        controlsBox.add(autoCheckBox);
    }

    private void performCheckButton() {
        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solveSudokuAndDisplayResults();
            }
        });
    }

    private void performSourceTextArea() {
        sourceTextArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (autoCheckBox.isSelected()) {
                    solveSudokuAndDisplayResults();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (autoCheckBox.isSelected()) {
                    solveSudokuAndDisplayResults();
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {}
        });
    }


    private void solveSudokuAndDisplayResults() {
        try {
            String text = sourceTextArea.getText();
            int[][] grid = Sudoku.textToGrid(text);
            sudoku = new Sudoku(grid);

            int numberOfSolutions = sudoku.solve();
            String solutionText = sudoku.getSolutionText();
            long elapsedTime = sudoku.getElapsed();

            String resultText = String.format("%s\nsolutions: %d\nelapsed: %dms", solutionText, numberOfSolutions, elapsedTime);

            resultTextArea.setText(resultText);
        } catch (Exception e) {
            resultTextArea.setText("Parsing problem");
        }
    }


    public static void main(String[] args) {
        // GUI Look And Feel
        // Do this incantation at the start of main() to tell Swing
        // to use the GUI LookAndFeel of the native platform. It's ok
        // to ignore the exception.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SudokuFrame frame = new SudokuFrame();
    }
}
