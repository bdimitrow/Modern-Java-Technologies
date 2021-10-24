import org.junit.jupiter.api.Test;

import java.lang.Object;
import java.util.LinkedList;
import java.util.List;

public class CanonicalPath {
    public static String getCanonicalPath(String path) {
        String[] components = path.split("/");
        StringBuilder sb = new StringBuilder();
        int skipDir = 0;

        for (int i = components.length - 1; i >= 0; i--) {
            String curr = components[i];
            if (curr.isBlank() || curr.equals(".")) {
                continue;
            }

            if (curr.equals("..")) {
                skipDir++;
            } else if (skipDir > 0) {
                skipDir--;
            } else {
                sb.insert(0, curr);
                sb.insert(0, "/");
            }
        }
        return sb.length() == 0 ? "/" : sb.toString();
    }

    public static void main(String[] args) {
        String test1 = getCanonicalPath("/home/");
        String test2 = getCanonicalPath("/../");
        String test3 = getCanonicalPath("/home//foo");
        String test4 = getCanonicalPath("/a/./b/../../c/");
        System.out.println(test1);
        System.out.println(test2);
        System.out.println(test3);
        System.out.println(test4);
    }


}
