package demo.rabbitmqdemo.service.blocks.routingbasics;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class RoutingTopologyView {

    private static final String DIRECT = "direct";
    private static final String TOPIC = "topic";
    private static final String FANOUT = "fanout";
    private static final String HEADERS = "headers";

    private final Map<String, String> exchanges;
    private final Map<String, List<String>> queuesByType;

    public RoutingTopologyView(
            DirectExchange directExchange,
            TopicExchange topicExchange,
            FanoutExchange fanoutExchange,
            HeadersExchange headersExchange,
            @Qualifier("orderQueue") Queue orderQueue,
            @Qualifier("billingQueue") Queue billingQueue,
            @Qualifier("notificationsQueue") Queue notificationsQueue,
            @Qualifier("notificationMoscowQueue") Queue notificationMoscowQueue,
            @Qualifier("notificationPscovQueue") Queue notificationPscovQueue,
            @Qualifier("notificationTverQueue") Queue notificationTverQueue,
            @Qualifier("notificationElistaQueue") Queue notificationElistaQueue,
            @Qualifier("highPriorityQueue") Queue highPriorityQueue,
            @Qualifier("lowPriorityQueue") Queue lowPriorityQueue
    ) {
        Map<String, String> exchangeMap = new LinkedHashMap<>();
        exchangeMap.put(DIRECT, directExchange.getName());
        exchangeMap.put(TOPIC, topicExchange.getName());
        exchangeMap.put(FANOUT, fanoutExchange.getName());
        exchangeMap.put(HEADERS, headersExchange.getName());
        this.exchanges = Map.copyOf(exchangeMap);

        Map<String, List<String>> queuesMap = new LinkedHashMap<>();
        queuesMap.put(DIRECT, List.of(orderQueue.getName(), billingQueue.getName()));
        queuesMap.put(TOPIC, List.of(orderQueue.getName(), notificationsQueue.getName()));
        queuesMap.put(FANOUT, List.of(
                notificationMoscowQueue.getName(),
                notificationPscovQueue.getName(),
                notificationTverQueue.getName(),
                notificationElistaQueue.getName()
        ));
        queuesMap.put(HEADERS, List.of(highPriorityQueue.getName(), lowPriorityQueue.getName()));
        this.queuesByType = Map.copyOf(queuesMap);
    }

    public String directExchange() {
        return exchanges.get(DIRECT);
    }

    public String topicExchange() {
        return exchanges.get(TOPIC);
    }

    public String fanoutExchange() {
        return exchanges.get(FANOUT);
    }

    public String headersExchange() {
        return exchanges.get(HEADERS);
    }

    public List<String> directQueues() {
        return queuesByType.getOrDefault(DIRECT, List.of());
    }

    public List<String> topicQueues() {
        return queuesByType.getOrDefault(TOPIC, List.of());
    }

    public List<String> fanoutQueues() {
        return queuesByType.getOrDefault(FANOUT, List.of());
    }

    public List<String> headersQueues() {
        return queuesByType.getOrDefault(HEADERS, List.of());
    }
}
