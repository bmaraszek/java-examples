package leetcode.examples;

public class ArrayDemo {

    public static void main(String[] args) {
        int[][] matrix = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
        };

        System.out.println("matrix.length: " + matrix.length);
        System.out.println("matrix[0].length: " + matrix[0].length);

        for(int y = 0; y < matrix.length; y++)
            for(int x = 0; x < matrix[0].length; x++)
                System.out.print(matrix[y][x] + ", ");
    }
}
