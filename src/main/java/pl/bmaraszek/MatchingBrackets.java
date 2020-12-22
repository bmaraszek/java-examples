package pl.bmaraszek;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Stack;
import java.util.stream.Collectors;


public class MatchingBrackets {
    public static void main(String[] args) throws FileNotFoundException {
        Character c = '(';
        System.out.println(c.equals('('));
        System.out.println(balancedBrackets(")[]}"));
        String[] arr = new String[]{"1", "2", "3"};
        Arrays.stream(arr).collect(Collectors.toList());
    }

    public static boolean balancedBrackets(String str) {
        Stack<Character> stack = new Stack<>();
        for(int i = 0; i < str.length(); i++) {
            Character currentChar = str.charAt(i);
            if(isOpeningBracket(currentChar)) {
                stack.push(currentChar);
            } else if (isClosingBracket(currentChar)){
                if(stack.isEmpty()) return false;
                Character mostRecent = stack.pop();
                if(currentChar.equals(")") && !mostRecent.equals("(")) return false;
                if(currentChar.equals("}") && !mostRecent.equals("{")) return false;
                if(currentChar.equals("]") && !mostRecent.equals("[")) return false;
            }
        }
        return stack.isEmpty();
    }

    private static boolean isOpeningBracket(Character c) {
        return c.equals("(") || c.equals("{") || c.equals("[");
    }

    private static boolean isClosingBracket(Character c) {
        return c.equals(")") || c.equals("}") || c.equals("]");
    }
}
