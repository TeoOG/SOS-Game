import java.util.Random;
import java.util.Scanner;

import static java.lang.System.exit;

    // -----------------------------------------------------------------------------------------------------------------

// This class represents a 3x3 grid.
// nextMoves: all the possible moves
// next: the move that Com follows
// empty_cells: number of empty cells in grid.
// table: current grid state

class min_max {
    
    private min_max[] nextMoves ;
    private int next;
    private int empty_cells;
    private char[][] table = new char[3][3];

    // MiniMax Initialization: 8 free cells , 2 Options per cell ("S" or "O")

    public min_max(){
        nextMoves = new min_max[16];
    }

    // Operator & Mutator for Attributes ---------------------------------------------------------------

    public min_max[] getPointers() {
        return nextMoves;
    }

    public void setPointers(min_max minmax, int position ) {
        this.nextMoves[position] = minmax;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public int getFree_boxes() {
        return empty_cells;
    }

    public void setFree_boxes(int empty_cells) {
        this.empty_cells = empty_cells;
    }

    public char[][] getTable() {
        return table;
    }
}

    // -----------------------------------------------------------------------------------------------------------------

public class Assignment2 {

    private static Scanner scanner = new Scanner(System.in);

    private static final int MAX = 1;
    private static final int MIN = -1;
    private static final int TIE = 0;


    // -----------------------------------------------------------------------------------------------------------------
    // Checking for ending states in the grid (horizontal, vertical, diagonal)
    // No Need to check the Second Raw
	// Returns true if there is a "SOS",otherwise false
    private static boolean check_SOS(char[][] table) {
        if (table[0][0] == 'S' && table[0][1] == 'O' && table[0][2] == 'S') {
            return true;
        } else if (table[2][0] == 'S' && table[2][1] == 'O' && table[2][2] == 'S') {
            return true;
        } else if (table[0][0] == 'S' && table[1][0] == 'O' && table[2][0] == 'S') {
            return true;
        } else if (table[0][1] == 'S' && table[1][1] == 'O' && table[2][1] == 'S') {
            return true;
        } else if (table[0][2] == 'S' && table[1][2] == 'O' && table[2][2] == 'S') {
            return true;
        } else if (table[0][0] == 'S' && table[1][1] == 'O' && table[2][2] == 'S') {
            return true;
        } else if (table[2][0] == 'S' && table[1][1] == 'O' && table[0][2] == 'S') {
            return true;
        } else {
            return false;
        }

    }

    // -----------------------------------------------------------------------------------------------------------------
    // Copies table A to table B
    public static void copy_table(char[][] A, char[][] B) {
        int r = 0 ;
        int c ;

        for (char[] i: A) {
            c=0;
            for(char j: i){
                B[r][c] = j;
                c++;
            }
            r++;
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Create children for all possible next States
    public static int next_states(min_max tree,  int next_player, int[] childrenValues) {
        int position = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tree.getTable()[i][j] == '♥') {

                    // Check children for "S"
                    tree.setPointers(new min_max(), position);
                    tree.getPointers()[position].setFree_boxes(tree.getFree_boxes()-1) ;
                    copy_table(tree.getTable(), tree.getPointers()[position].getTable());
                    tree.getPointers()[position].getTable()[i][j] = 'S';

                    childrenValues[position] = minimaxTree(tree.getPointers()[position], next_player);
                    position++;

                    // Check children for "O"
                    tree.setPointers(new min_max(), position);
                    tree.getPointers()[position].setFree_boxes(tree.getFree_boxes()-1) ;
                    copy_table(tree.getTable(), tree.getPointers()[position].getTable());
                    tree.getPointers()[position].getTable()[i][j] = 'O';
                    childrenValues[position] = minimaxTree(tree.getPointers()[position], next_player);
                    position++;
                }
            }
        }

        return position;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Allocate turns && Selects best move for Computer
    // Simulates the game flow and returns the most valuable move

    public static int minimaxTree(min_max tree, int turn) {
        int next_player;
        int position;
        int[] childrenValues = new int[16];
        int best_option;
        int best;

        if (turn == MAX && check_SOS(tree.getTable())) {
            return (MIN);
        } else if (turn == MIN && check_SOS(tree.getTable())) {
            return (MAX);
        } else if (tree.getFree_boxes() == 0) {
            return (TIE);
        }

        if (turn == MAX) {
            next_player = MIN;
        } else {
            next_player = MAX;
        }

        // Creates children because it ain't a leaf
        position = next_states(tree, next_player, childrenValues);

        // After next_states, Recursion Closes
        // Carries best value to Children
        // MAX's Turn
        if (turn == MAX) {

            best_option = childrenValues[0];
            best = 0;

            for (int i = 0; i < position; i++) {
                if (childrenValues[i] > best_option) {
                    best_option = childrenValues[i];
                    best = i;
                }
            }

        // Carries best value to Children
        // MAX's Turn
        }else{
            best_option = childrenValues[0];
            best = 0;
            for (int i = 1; i < position; i++) {
                if (childrenValues[i] < best_option)
                {
                    best_option = childrenValues[i];
                    best = i;
                }
            }
        }
        tree.setNext(best) ;
        return best_option;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Prints Grid
    public static void print_table(char[][] A) {

        for (char[] ci: A) {
            for (char c: ci) {
                System.out.print(c + " ");
            }
            System.out.print("\n");
        }
        System.out.print("-------------------------------------------\n\n");
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Controls the Game Flow
    // state: Stores the Grid State while Game is ON
    public static void main(String[] args) {

        min_max state = new min_max();
        int turn = MAX;

        // Grid Initialization. Fill Grid with "♥" as "Empty Cell"------------------------------------------------------
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                state.getTable()[i][j] = '♥';
            }
        }
        state.setFree_boxes(8);
        // -------------------------------------------------------------------------------------------------------------

        //  Place Randomly an "O" in second Raw: first or third Column
        Random random = new Random();
        int starting_pos = random.nextInt(2);
        switch (starting_pos){
            case 0:
                state.getTable()[1][0] = 'O';
                break;
            case 1:
                state.getTable()[1][2] = 'O';
                break;
        }
        // -------------------------------------------------------------------------------------------------------------

        // Game Starts Here
        System.out.print("Game Starts \n");
        print_table(state.getTable());

        while (state.getFree_boxes()!=-1) {

            // Check if Game is Over
            if(state.getFree_boxes()<=6) {
                checkForWinner(turn, state);
            }

            if (turn == MAX) {

                System.out.print("Com's Turn  \n\n\n");

                minimaxTree(state, MAX);
                state = state.getPointers()[state.getNext()];
                print_table(state.getTable());

                turn = MIN;

            } else {

                System.out.print("Player's  \n\n\n");

                char next_character = giveCharacter();

                int[] position = givePosition(state);
                int raw = position[0];
                int column = position[1];

                System.out.print("\n");

                state.getTable()[raw - 1][column - 1] = next_character;
                state.setFree_boxes(state.getFree_boxes() - 1) ;

                print_table(state.getTable());
                turn = MAX;
            }
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Validity Check: Character && Position that Player enters
    private static char giveCharacter(){
        char next_character;

        while (true){
            System.out.print("Choose Character:  (S or s / O or o) \n");
            next_character = scanner.next().charAt(0);
            next_character = Character.toUpperCase(next_character);
            if(next_character == 'O' || next_character == 'S')
                return next_character;
        }
    }

    private static int[] givePosition(min_max state){
        char[] position = new char[2];


        System.out.print("Choose Raw: (1 or 2 or 3)  \n");
        position[0] = scanner.next().charAt(0);

        System.out.print("Choose Column: (1 or 2 or 3)  \n");
        position[1] = scanner.next().charAt(0);

        return checkPosition(position,state);
    }

    private static int[] checkPosition(char[] positionChars,min_max state){

        int[] position = new int[2];

        try{
            position[0] = Character.getNumericValue(positionChars[0]);
            position[1] = Character.getNumericValue(positionChars[1]);
        } catch (NumberFormatException e) {
            return givePosition(state);
        }

        if(position[0]>=1 && position[0]<=3 && position[1]>=1 && position[1]<=3 && state.getTable()[position[0]-1][position[1]-1]=='♥')
            return position;

        return givePosition(state);

    }

    // -----------------------------------------------------------------------------------------------------------------
    // Check for Winner && Game Over
    private static void checkForWinner(int turn, min_max state){
        if (turn == MAX && check_SOS(state.getTable())) {
            printEndingCase("player");
        } else if (turn == MIN && check_SOS(state.getTable())) {
            printEndingCase("com");
        } else if (state.getFree_boxes() == 0) {
            printEndingCase("tie");
        }
    }

    private static void printEndingCase(String winner_tag){
        switch (winner_tag){
            case ("com"):
                System.out.print("Com Wins  \n\n\n");
                break;
            case("player"):
                System.out.print("Player Wins \n\n\n");
                break;
            default:
                System.out.print("Tie  \n\n\n");
        }
        System.out.println("Press Any Key To Exit");
        scanner.nextLine();
        exit(1);

    }

    // -----------------------------------------------------------------------------------------------------------------
}
