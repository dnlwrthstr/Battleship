class ArrayOperations {
    public static int[][][] createCube() {
        // write your code here
        int[][][] a = new int[3][3][3];
        int counter = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                for (int k = 0; k < 3; k++) {
                    a[j][i][k] = counter + k;
                }
            }
            counter += 3;
        }
        return a;
    }
}

/*
class Main {
    public static void main(String[] args) {
        int[][][] a = ArrayOperations.createCube();

        for (int i = 0; i < 3; i++) {
            System.out.println();
            for (int j = 0; j < 3; j++) {
                int[] b = a[i][j];
                System.out.print("{");
                for (int k = 0; k < 3; k++) {
                    System.out.print(b[k] + ", ");
                }
                System.out.print("}");
            }
        }
    }
}
*/