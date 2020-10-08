import java.util.*;

class node {
    int puzzle[][] = new int[3][3]; // the puzzle board
    int puzzleCopy[][] = new int[3][3]; // a copy of the puzzle board we'll need 
    int goal[][] = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}}; // the goal puzzle 
    LinkedList<node> children = new LinkedList<>(); // list carrying every possible child to the puzzle
    node parent; // the parent of the puzzle we will need in printing the paths
    int depth = 0; // the depth of the node we need in calculating the cost
    // the constructor methods takes an intial state and copy it to our puzzle and we take a copy of it
    public node(int x[][]) { 
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                puzzle[i][j] = x[i][j];
                puzzleCopy[i][j] = puzzle[i][j];
            }
        }
    }
    // just prints the puzzle
    public void printPuzzle(){ 
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
            System.out.print(puzzle[i][j]+" ");
            }
            System.out.println();
        }

    }
    //checks if goal is met 
    public boolean goalMet() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (puzzle[i][j] != goal[i][j])
                    return false;
            }
        }
        return true;
    }
//checks if two nodes have the same puzzle 
    public boolean isSamePuzzle(int x[][]) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (x[i][j] != puzzle[i][j])
                    return false;
            }
        }
        return true;
    }
    // the hueristic function we're using is calculating the manhatten distance sum between the puzzle and the goal puzzle
    public int heuristic() {
        int heuristic = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                heuristic += rightSpot(i, j, puzzle[i][j]);
                // the huerstic is the sum of all the manhattan distances of every element of the puzzle and it's location on the goal array
            }
        }
        return heuristic;
    }

    public int rightSpot(int row, int column, int num) { // gets the manhattan distance of the location of the element on the puzzle and it's location in goal
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

    public void expandNode() { // the function we need to expand the node and find out their children
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

    public void copy(int from[][], int to[][]) { // we used to copy a puzzle to the other
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                to[i][j] = from[i][j];
            }
        }
    }

    public void up(int[][] puzzle, int row, int col){ // generate a child if we moved the 0 upwards
        if (row > 0) { // the condition is to avoid getting out of bounds
            int temp = puzzle[row][col]; //swapping the 0 with the element upwards
            puzzle[row][col] = puzzle[row - 1][col];
            puzzle[row - 1][col] = temp;
            node child = new node(puzzle); // constructing the new child node
            child.depth = this.depth + 1; // incrementing the child depth
            child.parent = this; //saving that this node is the parent of the child node
            children.add(child); // adding the new child to children
            copy(puzzleCopy, puzzle); // returning the puzzle to it's regular state so we can generate the other children too
        }
    }
 // down , left , right are the same as the up method but with changing the way we move the 0 
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
    class comparator implements Comparator<node> {//the comparator we need for comparing between nodes in priorith queue
        public int compare(node o1, node o2) {
            return (o1.depth+o1.heuristic())-(o2.depth+o2.heuristic());
        }
    }
    class Main {
        public static void AStarSearch(node root) { // we set goal = false , intiallize the priority queue and the visited set , add the root them ; 
            boolean goal = false;
            PriorityQueue<node> priorityQueue = new PriorityQueue<node>(new comparator());
            HashSet<node> Visited = new HashSet<>();
            priorityQueue.add(root);
            Visited.add(root);
            if (root.goalMet()) {//if the root is the goal we just print it and break
                goal = true;
                System.out.println("Goal found");
                root.printPuzzle();
                priorityQueue.remove();
            }// while the queue isn't empty and we didnt find the goal we iterate
            while (!priorityQueue.isEmpty() && !goal) {
                node current = priorityQueue.remove(); // we get to the first element in the priority queue , add it to the visited , expand all possible movements
                Visited.add(current);
                current.expandNode();
                for (int i = 0; i < current.children.size(); i++) { // we iterate to the children of  the nodes 
                    node currentChild = current.children.get(i); // the current child we're on
                    if (currentChild.goalMet()) { //if that child puzzle = to the goal puzzle we just print the path and break 
                        goal = true;
                        System.out.println("Goal found");
                        int counter = 0;
                        ArrayList<node> parents = new ArrayList<>(); // the list of parents
                        while (currentChild.parent != null) {//getting the parents of the node
                            parents.add(currentChild);
                            counter++;
                            currentChild = currentChild.parent;
                        }
                        for (int j = parents.size()-1; j >=0 ; j--) { // notice we are printing it backwards because we started iterating from the goal state to the intial state
                            parents.get(j).printPuzzle();
                            System.out.println();
                        }
                        System.out.println("number of steps = " + counter);
                        System.out.println("number of visited nodes = " + Visited.size());
                        break;
                    }
                    node x = contains(priorityQueue, currentChild);//askes if priotity queue contains the node currentchild (if yes it will return the node else it will return null) 
                    if (x == null && !contains(Visited, currentChild)) {// if currentChild isn't in the queue nor the visited we just add iy
                        priorityQueue.add(currentChild);
                    }
                    if (x != null & !contains(Visited, currentChild)) { // if currentchild is in the queue but not visited we see if we can decrease the cost of it or not 
                        if (x.depth + x.heuristic() > currentChild.depth+currentChild.heuristic() ) { //if x cost is bigger we remove it and add the currentChild to the queue 
                            priorityQueue.remove(x);
                            priorityQueue.add(currentChild);
                        } // else we just leave it as it is
                    }
                }
            }
        }
        public static node contains(PriorityQueue<node> x , node y) {// returns if the priorty queue contains a node or not if does it returns the node else returns false
            for (node z : x) {
                if (y.isSamePuzzle(z.puzzle))
                    return z;
            }
            return null;
        }
        public static boolean contains (HashSet<node> x , node y){//the same but for the hashset we dont need the node to be returned 
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
