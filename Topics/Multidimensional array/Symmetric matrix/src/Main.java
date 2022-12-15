import java.util.Scanner;

class Main {

    public static void isSymmetric(int[][] array) {
        boolean isSymmetric = true;
        int n = array.length;
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                isSymmetric = array[i][j] == array[j][i] && isSymmetric;
            }
        }
        System.out.println(isSymmetric? "YES" : "NO");
    }

    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        int n= scanner.nextInt();
        int[][] array = new int[n][n];
        for(int i = 0; i < n; i++) {
            scanner.nextLine();
            for(int j = 0; j < n; j++) {
                array[i][j] = scanner.nextInt();
            }
        }
        isSymmetric(array);
    }
}