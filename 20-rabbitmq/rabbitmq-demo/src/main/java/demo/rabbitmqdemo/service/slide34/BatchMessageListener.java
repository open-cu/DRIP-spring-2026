package demo.rabbitmqdemo.service.slide34;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import demo.rabbitmqdemo.core.batch.QueueItemReader;
import demo.rabbitmqdemo.core.model.Order;
import java.util.*;
import java.util.concurrent.*;
import demo.rabbitmqdemo.core.model.Order;
import org.springframework.context.annotation.Profile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Profile("slide34")
@ConditionalOnProperty(name = "demo.slide34.mode", havingValue = "batch-job")
@Service
public class BatchMessageListener {

    private final JobLauncher jobLauncher;
    private final Job job;
    private final QueueItemReader<Order> itemReader;
    private final Queue<Order> buffer = new ConcurrentLinkedQueue<>();
    private final int batchSize;

    public BatchMessageListener(JobLauncher jobLauncher,
                            Job job,
                            QueueItemReader<Order> itemReader,
                            @Value("${demo.slide34.batchJob.batchSize:100}") int batchSize) {
        this.jobLauncher = jobLauncher;
        this.job = job;
        this.itemReader = itemReader;
        this.batchSize = batchSize;
    }

    @RabbitListener(queues = "batch.queue")
    public void handleOrder(Order order) {
        buffer.add(order);
    }

    @Scheduled(fixedRateString = "${demo.slide34.batchJob.rateMs:5000}") // каждые N секунд
    public void processBatch() throws Exception {
        if (buffer.isEmpty() || buffer.size() < batchSize) {
            return;
        }

        List<Order> batch = new ArrayList<>();
        Order order;
        while ((order = buffer.poll()) != null && batch.size() < batchSize) {
            batch.add(order);
        }

        if (!batch.isEmpty()) {
            itemReader.setQueue(new ArrayDeque<>(batch));
            JobParameters params = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(job, params);
        }
    }
}
