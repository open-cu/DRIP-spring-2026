package demo.rabbitmqdemo.core.batch;

import java.util.Queue;

import org.springframework.batch.item.ItemReader;

public class QueueItemReader<T> implements ItemReader<T> {
    private Queue<T> queue;

    public void setQueue(Queue<T> queue) {
        this.queue = queue;
    }

    @Override
    public T read() {
        if (queue == null) {
            return null;
        }
        return queue.poll();
    }
}

