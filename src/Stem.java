import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

public class Stem {
    private Stack<Integer> stack;

    public Stem() {
        stack = new Stack<Integer>();
    }

    boolean addDisk(int disk) {
        return stack.add(disk);
    }

    int removeDisk() {
        return stack.pop();
    }

    int getHighestDisk() {
        try {
            return stack.peek();
        } catch (EmptyStackException e) {
            return 0;
        }
    }

    List getAllDisks() {
        return Arrays.asList(stack.toArray());
    }

    public String toString() {
        return stack.toString();
    }
}
