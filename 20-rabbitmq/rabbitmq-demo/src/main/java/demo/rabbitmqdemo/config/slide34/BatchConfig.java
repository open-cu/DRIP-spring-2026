package demo.rabbitmqdemo.config.slide34;

import demo.rabbitmqdemo.core.batch.QueueItemReader;
import demo.rabbitmqdemo.core.model.Order;
import demo.rabbitmqdemo.core.model.ProcessedOrder;
import demo.rabbitmqdemo.core.repository.OrderRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.PlatformTransactionManager;

@Profile("slide34")
@Configuration
@EnableBatchProcessing
@ConditionalOnProperty(name = "demo.slide34.mode", havingValue = "batch-job")
public class BatchConfig {

    @Bean
    public Job batchProcessingJob(JobRepository jobRepository,
                                Step batchStep) {
        return new JobBuilder("batchProcessingJob", jobRepository)
                .start(batchStep)
                .build();
    }

    @Bean
    public Step batchStep(JobRepository jobRepository,
                        PlatformTransactionManager transactionManager,
                        ItemReader<Order> itemReader,
                        ItemProcessor<Order, ProcessedOrder> processor,
                        ItemWriter<ProcessedOrder> writer) {
        return new StepBuilder("batchStep", jobRepository)
                .<Order, ProcessedOrder>chunk(100, transactionManager)
                .reader(itemReader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public QueueItemReader<Order> orderItemReader() {
        return new QueueItemReader<>();
    }

    @Bean
    public ItemProcessor<Order, ProcessedOrder> orderProcessor() {
        return order -> {
            // бизнес-логика обработки
            return new ProcessedOrder(order.getId(), "PROCESSED");
        };
    }

    @Bean
    public ItemWriter<ProcessedOrder> orderWriter(OrderRepository repository) {
        return repository::saveAll;
    }
}
