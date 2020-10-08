import java.util.*;

class node {
    int puzzle[][] = new int[3][3];
    int puzzleCopy[][] = new int[3][3];
    int goal[][] = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}};
    LinkedList<node> children = new LinkedList<>();
    node parent;
    int depth = 0;
    public node(int x[][]) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                puzzle[i][j] = x[i][j];
                puzzleCopy[i][j] = puzzle[i][j];
            }
        }
    }
    public void printPuzzle(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
            System.out.print(puzzle[i][j]+" ");
            }
            System.out.println();
        }

    }
    public boolean goalMet() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (puzzle[i][j] != goal[i][j])
                    return false;
            }
        }
        return true;
    }

    public boolean isSamePuzzle(int x[][]) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (x[i][j] != puzzle[i][j])
                    return false;
            }
        }
        return true;
    }

    public int heuristic() {
        int heuristic = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                heuristic += rightSpot(i, j, puzzle[i][j]);
            }
        }
        return heuristic;
    }

    public int rightSpot(int row, int column, int num) {
        if (goal[row][column] == num)
            return 0;
        else {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (goal[i][j] == num) {
                        return Math.abs(i - row) + Math.abs(j - column);
                    }
                }
            }
            return  0 ;
        }
    }

    public void expandNode() {
        int row = 0, col = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (puzzle[i][j] == 0) {
                    row = i;
                    col = j;
                    break;
                }
            }
        }
        up(puzzle, row, col);
        down(puzzle, row, col);
        left(puzzle, row, col);
        right(puzzle, row, col);
    }

    public void copy(int from[][], int to[][]) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                to[i][j] = from[i][j];
            }
        }
    }

    public void up(int[][] puzzle, int row, int col) {
        if (row > 0) {
            int temp = puzzle[row][col];
            puzzle[row][col] = puzzle[row - 1][col];
            puzzle[row - 1][col] = temp;
            node child = new node(puzzle);
            child.depth = this.depth + 1;
            child.parent = this;
            children.add(child);
            copy(puzzleCopy, puzzle);
        }
    }

    public void down(int[][] puzzle, int row, int col) {
        if (row < 2) {
            int temp = puzzle[row][col];
            puzzle[row][col] = puzzle[row + 1][col];
            puzzle[row + 1][col] = temp;
            node child = new node(puzzle);
            child.depth = this.depth + 1;
            child.parent = this;
            children.add(child);
            copy(puzzleCopy, puzzle);
        }
    }

    public void left(int[][] puzzle, int row, int col) {
        if (col > 0) {
            int temp = puzzle[row][col];
            puzzle[row][col] = puzzle[row][col-1];
            puzzle[row][col - 1] = temp;
            node child = new node(puzzle);
            child.depth = this.depth + 1;
            child.parent = this;
            children.add(child);
            copy(puzzleCopy, puzzle);
        }
    }

    public void right(int[][] puzzle, int row, int col) {
        if (col < 2) {
            int temp = puzzle[row][col];
            puzzle[row][col] = puzzle[row][col + 1];
            puzzle[row][col + 1] = temp;
            node child = new node(puzzle);
            child.depth = this.depth + 1;
            child.parent = this;
            children.add(child);
            copy(puzzleCopy, puzzle);
        }
    }
}
    class comparator implements Comparator<node> {
        public int compare(node o1, node o2) {
            return (o1.depth+o1.heuristic())-(o2.depth+o2.heuristic());
        }
    }
    class Main {
        public static void AStarSearch(node root) {
            boolean goal = false;
            PriorityQueue<node> priorityQueue = new PriorityQueue<node>(new comparator());
            HashSet<node> Visited = new HashSet<>();
            priorityQueue.add(root);
            Visited.add(root);
            if (root.goalMet()) {
                goal = true;
                System.out.println("Goal found");
                root.printPuzzle();
                priorityQueue.remove();
            }
            while (!priorityQueue.isEmpty() && !goal) {
                node current = priorityQueue.remove();
                Visited.add(current);
                current.expandNode();
                for (int i = 0; i < current.children.size(); i++) {
                    node currentChild = current.children.get(i);
                    if (currentChild.goalMet()) {
                        goal = true;
                        System.out.println("Goal found");
                        int counter = 0;
                        ArrayList<node> parents = new ArrayList<>();
                        while (currentChild.parent != null) {
                            parents.add(currentChild);
                            counter++;
                            currentChild = currentChild.parent;
                        }
                        for (int j = parents.size()-1; j >=0 ; j--) {
                            parents.get(j).printPuzzle();
                            System.out.println();
                        }
                        System.out.println("number of steps = " + counter);
                        System.out.println("number of visited nodes = " + Visited.size());
                        break;
                    }
                    node x = contains(priorityQueue, currentChild);
                    if (x == null && !contains(Visited, currentChild)) {
                        priorityQueue.add(currentChild);
                    }
                    if (x != null & !contains(Visited, currentChild)) {
                        if (x.depth + x.heuristic() > currentChild.depth+currentChild.heuristic() ) {
                            priorityQueue.remove(x);
                            priorityQueue.add(currentChild);
                        }
                    }
                }
            }
        }
        public static node contains(PriorityQueue<node> x , node y) {
            for (node z : x) {
                if (y.isSamePuzzle(z.puzzle))
                    return z;
            }
            return null;
        }
        public static boolean contains (HashSet<node> x , node y){
            for (node z : x){
               if(y.isSamePuzzle(z.puzzle))
                   return true;
           }
           return false;
        }
        public static void main(String[] args) {
            int puzzle[][]=new int[3][3];
            Scanner s = new Scanner(System.in);
            for (int i = 0; i <3 ; i++) {
                for (int j = 0; j <3 ; j++) {
                    puzzle[i][j]=s.nextInt();
                }
            }
            node n = new node(puzzle);
            AStarSearch(n);

        }
    }