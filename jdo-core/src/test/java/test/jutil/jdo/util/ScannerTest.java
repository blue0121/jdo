package test.jutil.jdo.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Jin Zheng
 * @since 2022-09-14
 */
public class ScannerTest {
	public ScannerTest() {
	}

    @Test
    public void testSplit() {
        String str = "abc ; cde; fg  ;  ";
        Scanner scanner = new Scanner(str).useDelimiter(";");
        List<String> list = new ArrayList<>();
        int i = 1;
        while (scanner.hasNext()) {
            String item = scanner.next().trim();
            if (item.isEmpty()) {
                continue;
            }
            System.out.printf("%d - |%s|\n", i, item);
            i++;
            list.add(item);
        }
        Assertions.assertEquals(List.of("abc", "cde", "fg"), list);
    }

    @Test
    public void testSplit2() {
        String str = "abc";
        Scanner scanner = new Scanner(str).useDelimiter(";");
        List<String> list = new ArrayList<>();
        int i = 1;
        while (scanner.hasNext()) {
            String item = scanner.next().trim();
            if (item.isEmpty()) {
                continue;
            }
            System.out.printf("%d - |%s|\n", i, item);
            i++;
            list.add(item);
        }
        Assertions.assertEquals(List.of("abc"), list);
    }

}
