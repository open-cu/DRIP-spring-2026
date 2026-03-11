package lecture.step05.service.io;

import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
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

