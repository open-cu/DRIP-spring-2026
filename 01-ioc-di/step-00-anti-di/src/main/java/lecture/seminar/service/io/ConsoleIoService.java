package lecture.seminar.service.io;

import java.util.Scanner;

public class ConsoleIoService implements AutoCloseable {
    private final Scanner scanner = new Scanner(System.in);

    public void println(String text) {
        System.out.println(text);
    }

    public void print(String text) {
        System.out.print(text);
    }

    public String readLine() {
        return scanner.nextLine();
    }

    public void close() {
        scanner.close();
    }
}
