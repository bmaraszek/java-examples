package leetcode.examples;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * In Java, a stack/list's head is always index 0
 * So when we join the strings, we either need to use push() pop() and iterate backwards
 * or use LinkedList addLast() removeLast()
 */
public class ShortenUnixPath {
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println(shortenPath("/foo/bar/baz"));
    }

    public static String shortenPath(String path) {
        boolean startsWithSlash = path.startsWith("/");
        String[] dirs = path.split("/");
        List<String> tokens = Arrays.stream(dirs)
                .filter(x -> isImportantToken(x))
                .collect(Collectors.toList());

        LinkedList<String> stack = new LinkedList<>();
        if(startsWithSlash) stack.addLast("");
        for(String token : tokens) {
            if(token.equals("..")) {
                if(stack.size() == 0 || stack.peek().equals("..")) {
                    //stack.push(token);
                    stack.addLast(token);
                }
                else if(!stack.peek().equals("")) {
                    //stack.pop();
                    stack.removeLast();
                }
            } else {
                //stack.push(token);
                stack.addLast(token);
            }
        }
        if(stack.size() == 1 || stack.getLast().equals("")) return "/";
        return String.join("/", stack);
    }

    public static boolean isImportantToken(String str) {
        return !str.equals("") && !str.equals(".");
    }
}
