import java.util.*;

class Node implements Comparable<Node> {
    public int row, col;
    public int gCost, hCost;
    public Node parent;

    public Node(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getFCost() {
        return gCost + hCost;
    }

    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.getFCost(), other.getFCost());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Node) {
            Node other = (Node) obj;
            return this.row == other.row && this.col == other.col;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}

public class AStar {
    private static final int[][] DIRECTIONS = {
        {0, 1}, {1, 0}, {0, -1}, {-1, 0}, // Right, Down, Left, Up
        {1, 1}, {-1, -1}, {1, -1}, {-1, 1} // Diagonals
    };

    public List<Node> findPath(int[][] grid, Node start, Node goal) {
        PriorityQueue<Node> openSet = new PriorityQueue<>();
        Set<Node> closedSet = new HashSet<>();
        start.gCost = 0;
        start.hCost = calculateHeuristic(start, goal);
        openSet.add(start);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.equals(goal)) {
                return reconstructPath(current);
            }

            closedSet.add(current);

            for (int[] direction : DIRECTIONS) {
                int newRow = current.row + direction[0];
                int newCol = current.col + direction[1];

                if (isInBounds(grid, newRow, newCol) && grid[newRow][newCol] == 0) {
                    Node neighbor = new Node(newRow, newCol);
                    if (closedSet.contains(neighbor)) continue;

                    int tentativeGCost = current.gCost + 1;

                    if (!openSet.contains(neighbor) || tentativeGCost < neighbor.gCost) {
                        neighbor.gCost = tentativeGCost;
                        neighbor.hCost = calculateHeuristic(neighbor, goal);
                        neighbor.parent = current;

                        if (!openSet.contains(neighbor)) {
                            openSet.add(neighbor);
                        }
                    }
                }
            }
        }
        return new ArrayList<>(); // Return empty path if goal not reachable
    }

    private boolean isInBounds(int[][] grid, int row, int col) {
        return row >= 0 && col >= 0 && row < grid.length && col < grid[0].length;
    }

    private int calculateHeuristic(Node a, Node b) {
        return Math.abs(a.row + a.col - b.row - b.col); // Manhattan distance heuristic
    }

    private List<Node> reconstructPath(Node node) {
        List<Node> path = new ArrayList<>();
        while (node != null) {
            path.add(node);
            node = node.parent;
        }
        Collections.reverse(path);
        return path;
    }

    public static void main(String[] args) {
        int[][] grid = {
            {0, 0, 0, 0, 1},
            {0, 1, 1, 0, 0},
            {0, 0, 0, 0, 0},
            {1, 1, 0, 1, 0},
            {0, 0, 0, 0, 0}
        };

        AStar aStar = new AStar();
        Node start = new Node(0, 0);
        Node goal = new Node(4, 4);

        List<Node> path = aStar.findPath(grid, start, goal);

        if (!path.isEmpty()) {
            System.out.println("Path found:");
            for (Node node : path) {
                System.out.println("(" + node.row + ", " + node.col + ")");
            }
        } else {
            System.out.println("No path found.");
        }
    }
}
