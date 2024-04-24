package y_Bit;

public class Main {

    public static void main(String[] args) {
//        exampleToStrings(3);

//        System.out.println(toString((byte) -1));
//        System.out.println(toString(-1));

//        System.out.println();
//        System.out.println(Integer.MIN_VALUE);
//        shiftOperations(-1, 4);

//        System.out.println();
//        System.out.println();
//        otherOperations(6, 187420);
        replaceAt(0x12345678, 0, 3);
        System.out.println(getAt(0x12345678, 3));
        System.out.println(getAt(0x12345678, 2));
    }

    private static int getAt(int n, int position) {
        return ((n >> position) & 1);
    }

    private static void exampleToStrings(int i) {
        System.out.printf("%08X\n", i);

        System.out.println(String.format("%08X", i).replaceAll("(..)", ":$1").substring(1));

        System.out.println(String.format("%32s", Integer.toBinaryString(i)).replaceAll(" ", "0").replaceAll("(\\d{8})", "$1 "));
    }


    public static int replaceAt(int data, int replaceable, int position) {
        System.out.println(toString(data) + "data");
        int mask = (~(1 << position));

        System.out.println(toString(1 << position));
        System.out.println(toString(mask));

        data = (data & mask);
        System.out.println(toString(data) + "data");
        data |= (replaceable << position);
        System.out.println(toString(replaceable << position));
        System.out.println(toString(data) + "data");
        return data;
    }


    public static String toString(Number n) {
        if (n instanceof Integer) {
            return String.format("%32s", Integer.toBinaryString((int) n)).replaceAll(" ", "0").replaceAll("(\\d{8})", "$1  ").replaceAll("(\\d{4})", "$1 ");
        }
        if (n instanceof Byte) {
            return String.format("%8s", Integer.toBinaryString(Byte.toUnsignedInt((byte) n))).replaceAll(" ", "0").replaceAll("(\\d{4})", "$1 ");
        }
        throw new IllegalArgumentException("UNKNOWN NUMBER");
    }


    public static void shiftOperations(int n1, int n2) {
        System.out.println(toString(n1));


        System.out.println("n1>>4");
        System.out.println(toString(n1 >> n2));

        System.out.println("n1>>>4");
        System.out.println(toString(n1 >>> n2));

        System.out.println("n1<<4");
        System.out.println(toString(n1 << n2));
    }


    public static void otherOperations(int n1, int n2) {
        System.out.println(toString(n1));
        System.out.println(toString(n2));

        System.out.println("or: n1|n2");
        System.out.println(toString(n1 | n2));

        System.out.println("xor: n1^n2");
        System.out.println(toString(n1 ^ n2));

        System.out.println("and: n1&n2");
        System.out.println(toString(n1 & n2));

        System.out.println("not: ~n1");
        System.out.println(toString(~n1));

        System.out.println("not: ~n2");
        System.out.println(toString(~n2));
    }
}