package com.example.sudoku;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.event.ActionEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.util.*;


public class BoardController {
    public static final int DIM_NUM = 9;
    public static final int CELL_NUM = 81;
    private static int filledCellNum = 0;
    @FXML
    private GridPane board;
    @FXML
    private Button easyGameButton;
    @FXML
    private Button hardGameButton;
    @FXML
    private Button mediumGameButton;
    @FXML
    public Text nameText;
    @FXML
    void onEasyGameButtonClick(ActionEvent event) {
        double probability = 0.3;
        filledCellNum = 0;
        buildBoard(createBoard(), board, probability);
    }
    @FXML
    void onMediumGameButtonClick(ActionEvent event) {
        double probability = 0.5;
        filledCellNum = 0;
        buildBoard(createBoard(), board, probability);
    }
    @FXML
    void onHardGameButtonClick(ActionEvent event) {
        double probability = 0.8;
        filledCellNum = 0;
        buildBoard(createBoard(), board, probability);
    }
    public void initialize() {
        Font nameFont = Font.loadFont(getClass().getResourceAsStream("/AAsianHiro-vmv3L.ttf"), 55);
        if (nameFont != null) {
            nameText.setFont(nameFont);
        }
        Font buttonFont = Font.loadFont(getClass().getResourceAsStream("/AAsianHiro-vmv3L.ttf"), 14);
        easyGameButton.setFont(buttonFont);
        mediumGameButton.setFont(buttonFont);
        hardGameButton.setFont(buttonFont);
    }

    private static int[][] createBoard () {
        int[][] boardNum = new int[DIM_NUM][DIM_NUM];
        List<Integer> basicRow = new ArrayList<>();

        for(int i = 1; i <= 9; i++) {
            basicRow.add(i);
        }

        Collections.shuffle(basicRow);

        for (int row = 0; row < DIM_NUM; row++) {
            int shift = (row % 3) * 3 + (row / 3);
            for (int col = 0; col < DIM_NUM; col++) {
                int shiftedIndex = (col + shift) % DIM_NUM;
                boardNum[row][col] = basicRow.get(shiftedIndex);
            }
        }

        shuffleRowsAndColumns(boardNum);

        while(!solveSudoku(boardNum)) {
            createBoard();
        }
        return boardNum;
    }

    private static void buildBoard(int[][] boardNum, GridPane board, double probability){

        Random random = new Random();
        Font cellFont = Font.loadFont(BoardController.class.getResourceAsStream("/AAsianHiro-vmv3L.ttf"), 14);

        Stack<TextField> textFields = new Stack<>();

        for (int row = 0; row < boardNum.length; row++) {
            for (int col = 0; col < boardNum[row].length; col++) {
                TextField cell = new TextField();
                cell.setFont(cellFont);
                cell.setStyle("-fx-font-family: '" + cellFont.getFamily() + "'; -fx-font-size: 14px;");
                if (random.nextDouble() > probability){
                    cell.setText(Integer.toString(boardNum[row][col]));
                    cell.setEditable(false);
                    board.add(cell, col, row);
                    filledCellNum++;
                } else {
                    board.add(cell, col, row);
                    int emptyCellRow = row;
                    int emptyCellCol = col;

                    cell.textProperty().addListener((a, boardNumValue, enteredValue) -> {
                        if (enteredValue.isEmpty()) {
                            cell.setStyle("-fx-background-color: #ecf2dc;");
                        } else {
                            try {
                                if(enteredValue.matches("[1-9]?")) {
                                    int convertedEnteredValue = Integer.parseInt(enteredValue);
                                    if (boardNum[emptyCellRow][emptyCellCol] == convertedEnteredValue) {
                                        cell.setStyle("-fx-background-color: #cff2e0; -fx-font-family: '" + cellFont.getFamily() + "'; -fx-font-size: 14px;");
                                        textFields.push(cell);
                                        cell.setEditable(false);
                                        if (CELL_NUM - filledCellNum == textFields.size()) {
                                            ModalWindow.newWindow("WIN");
                                        }
                                    } else {
                                        cell.setStyle("-fx-background-color: #f4d7d7; -fx-font-family: '" + cellFont.getFamily() + "'; -fx-font-size: 14px;");
                                    }
                                } else {
                                    cell.setText("");
                                }
                            } catch (NumberFormatException e) {
                                cell.setStyle("-fx-background-color: #f4d7d7;");
                            }
                        }
                    });
                }
            }
        }
    }

    private static void shuffleRowsAndColumns(int[][] boardNum) {
        List<Integer> nums0_2 = new ArrayList<>();
        nums0_2.add(0);
        nums0_2.add(1);
        nums0_2.add(2);
        Collections.shuffle(nums0_2);

        List<Integer> nums3_5 = new ArrayList<>();
        nums3_5.add(3);
        nums3_5.add(4);
        nums3_5.add(5);
        Collections.shuffle(nums3_5);

        List<Integer> nums6_8 = new ArrayList<>();
        nums6_8.add(6);
        nums6_8.add(7);
        nums6_8.add(8);
        Collections.shuffle(nums6_8);

        int[][] boardNumCopy = new int[DIM_NUM][DIM_NUM];
        boardNumCopy[nums0_2.get(0)] = boardNum[nums0_2.get(0)];
        boardNum[nums0_2.get(0)] = boardNum[nums0_2.get(1)];
        boardNum[nums0_2.get(1)] = boardNumCopy[nums0_2.get(0)];

        boardNumCopy[nums3_5.get(0)] = boardNum[nums3_5.get(0)];
        boardNum[nums3_5.get(0)] = boardNum[nums3_5.get(1)];
        boardNum[nums3_5.get(1)] = boardNumCopy[nums3_5.get(0)];

        boardNumCopy[nums6_8.get(0)] = boardNum[nums6_8.get(0)];
        boardNum[nums6_8.get(0)] = boardNum[nums6_8.get(1)];
        boardNum[nums6_8.get(1)] = boardNumCopy[nums6_8.get(0)];

        Arrays.stream(boardNum).forEach(row -> {
            int temp = row[nums0_2.get(1)];
            row[nums0_2.get(1)] = row[nums0_2.get(2)];
            row[nums0_2.get(2)] = temp;
        });

        Arrays.stream(boardNum).forEach(row -> {
            int temp = row[nums3_5.get(1)];
            row[nums3_5.get(1)] = row[nums3_5.get(2)];
            row[nums3_5.get(2)] = temp;
        });

        Arrays.stream(boardNum).forEach(row -> {
            int temp = row[nums6_8.get(1)];
            row[nums6_8.get(1)] = row[nums6_8.get(2)];
            row[nums6_8.get(2)] = temp;
        });
    }

    private static boolean isNumberInRow(int[][] board, int row, int inputNum) {
        for(int i = 0; i < DIM_NUM; i++) {
            if(board[row][i] == inputNum) {
                return true;
            }
        }
        return false;
    }

    private static boolean isNumberInColumn(int[][] board, int col, int inputNum) {
        for(int i = 0; i < DIM_NUM; i++) {
            if(board[i][col] == inputNum) {
                return true;
            }
        }
        return false;
    }

    private static boolean isNumberInBox(int[][] board, int row, int col, int inputNum) {
        int boxRow = row - row % 3;
        int boxCol = col - col % 3;

        for(int i = boxRow; i< boxRow + 3; i++) {
            for(int j = boxCol; j< boxCol + 3; j++) {
                if(board[i][j] == inputNum) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isNumberValid(int[][] board, int row, int col, int inputNum) {
        return !isNumberInRow(board, row, inputNum) && !isNumberInColumn(board, col, inputNum)
                && !isNumberInBox(board, row, col, inputNum);
    }

    public static boolean solveSudoku(int[][] boardNum) {
        for (int row = 0; row < DIM_NUM; row++) {
            for (int col = 0; col < DIM_NUM; col++) {
               if(boardNum[row][col] == 0) {
                   for(int tryNum = 1; tryNum <=DIM_NUM; tryNum++) {
                       if(isNumberValid(boardNum, row, col, tryNum)) {
                           boardNum[row][col] = tryNum;

                           if(solveSudoku(boardNum)) {
                               return true;
                           } else {
                               boardNum[row][col] = 0;
                           }
                       }
                   }
                   return false;
               }
            }
        }
        return true;
    }
}

