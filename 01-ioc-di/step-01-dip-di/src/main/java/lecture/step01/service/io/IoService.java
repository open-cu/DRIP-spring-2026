package lecture.step01.service.io;

public interface IoService extends AutoCloseable {
    void println(String text);
    void print(String text);
    String readLine();

    @Override
    void close();
}