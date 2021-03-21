import java.util.Stack;

public class stringUnpacker {

    public static void main(String[] args) {

        String input = "q2[a3[d2[er0[qwerty]]x]s]"; // -> qadererxdererxdererxsadererxdererxdererxs

        if (isValid(input)) {
            String output = unpackString(input);
            System.out.println(output);
        }

    }


    public static String unpackString(String input) {
        StringBuilder result = new StringBuilder();
        StringBuilder number = new StringBuilder();
        StringBuilder letters = new StringBuilder();
        Stack<Integer> numberStack = new Stack<>();
        Stack<String> lettersStack = new Stack<>();

        char[] charSequence = input.toCharArray();
        for (char symbol : charSequence) {

            if (symbol >= '0' && symbol <= '9') {
                number.append(symbol);
            } else if (symbol == '[') {
                numberStack.push(Integer.parseInt(number.toString()));
                number.setLength(0);

                lettersStack.push(letters.toString());
                letters.setLength(0);

            } else if ((symbol >= 'A' && symbol <= 'Z') || (symbol >= 'a' && symbol <= 'z')) {
                letters.append(symbol);
            } else if (symbol == ']') {
                int multiplier = numberStack.pop();
                if (multiplier == 0) {
                    letters.setLength(0);
                } else {
                    letters.append(String.valueOf(letters).repeat(multiplier - 1));
                }
                if (!lettersStack.empty()) {
                    letters.insert(0, lettersStack.pop());
                }

                if (numberStack.empty()) {
                    result.append(letters);
                    letters.setLength(0);
                }
            }
        }
        if (!letters.isEmpty()) {
            result.append(letters);
        }
        return result.toString();
    }

    public static boolean isValid(String input) {
        int bracketsCounter = 0;

        if (input.isEmpty()) {
            System.out.println("Error: Input string is empty.");
            return false;
        }
        char[] charSequence = input.toCharArray();
        if (charSequence[0] == '[' || charSequence[0] == ']') {
            showErrorInInputString(input, 0);
            System.out.println("Error: Input string should not start with '[' or ']'.");
            return false;
        }
        if (charSequence[charSequence.length - 1] >= '0' && charSequence[charSequence.length - 1] <= '9') {
            showErrorInInputString(input, charSequence.length - 1);
            System.out.println("Error: The string cannot end with a number.");
            return false;
        }

        for (int i = 0; i < charSequence.length; i++) {
            if (charSequence[i] == '[' && !(charSequence[i - 1] >= '0' && charSequence[i - 1] <= '9')) {
                showErrorInInputString(input, i);
                System.out.println("Error: Need to provide the number of repetitions before the brackets.");
                return false;
            } else if (charSequence[i] == '[') {
                bracketsCounter++;
            } else if (charSequence[i] == ']') {
                if (bracketsCounter > 0) {
                    bracketsCounter--;
                } else {
                    showErrorInInputString(input, i);
                    System.out.println("Error: There are more closing brackets than opening.");
                    return false;
                }
                if (charSequence[i - 1] == '[') {
                    showErrorInInputString(input, i - 1);
                    System.out.println("Error: There must be letters between the brackets.");
                    return false;
                }
            } else if (!((charSequence[i] >= 'a' && charSequence[i] <= 'z')
                    || (charSequence[i] >= 'A' && charSequence[i] <= 'Z')
                    || (charSequence[i] >= '0' && charSequence[i] <= '9')
                    || (charSequence[i] == '[')
                    || (charSequence[i] == ']'))) {
                showErrorInInputString(input, i);
                System.out.println("Error: Invalid character in string.");
                return false;
            } else if ((charSequence[i] >= '0' && charSequence[i] <= '9') &&
                    !((charSequence[i + 1] >= '0' && charSequence[i + 1] <= '9') || (charSequence[i + 1] == '['))) {
                showErrorInInputString(input, i);
                System.out.println("Error: There must be brackets after the numbers.");
                return false;
            }
        }
        if (bracketsCounter > 0) {
            showErrorInInputString(input, -1);
            System.out.println("Error: There are more opening brackets than closing.");
            return false;
        }
        return true;
    }

    public static void showErrorInInputString(String inputString, int index) {
        System.out.println(inputString);
        char[] charSequence = inputString.toCharArray();
        for (int i = 0; i < charSequence.length; i++) {
            if (i == index) {
                System.out.print('^');
            } else {
                System.out.print(' ');
            }
        }
        System.out.print('\n');
    }
}
