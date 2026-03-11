package lecture.step01.service.io;

import java.util.Scanner;

public class ConsoleIoService implements IoService {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void println(String text) { System.out.println(text); }

    @Override
    public void print(String text) { System.out.print(text); }

    @Override
    public String readLine() { return scanner.nextLine(); }

    @Override
    public void close() { scanner.close(); }
}

