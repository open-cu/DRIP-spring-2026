package demo.rabbitmqdemo.service.blocks.routingbasics;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Service;

@Service
public class ScenarioService {
    private final MessagingService messagingService;
    private final RoutingTopologyView topology;
    private final RabbitAdmin rabbitAdmin;

    public ScenarioService(MessagingService messagingService, RoutingTopologyView topology, RabbitAdmin rabbitAdmin) {
        this.messagingService = messagingService;
        this.topology = topology;
        this.rabbitAdmin = rabbitAdmin;
    }

    public void sendDirect(String routingKey, String description) {
        messagingService.sendDirect(routingKey, description);
    }

    public void sendTopic(String routingKey, String description) {
        messagingService.sendTopic(routingKey, description);
    }

    public void sendFanout(String description) {
        messagingService.sendFanout(description);
    }

    public void sendHeaders(String priority, String description) {
        messagingService.sendHeaders(priority, description);
    }

    public void reset() {
        topology.directQueues().forEach(this::purge);
        topology.topicQueues().forEach(this::purge);
        topology.fanoutQueues().forEach(this::purge);
        topology.headersQueues().forEach(this::purge);
    }

    public Map<String, Integer> queueStats() {
        Map<String, Integer> stats = new LinkedHashMap<>();
        putQueues(stats, topology.directQueues(), "direct");
        putQueues(stats, topology.topicQueues(), "topic");
        putQueues(stats, topology.fanoutQueues(), "fanout");
        putQueues(stats, topology.headersQueues(), "headers");
        return stats;
    }

    private void putQueues(Map<String, Integer> stats, java.util.List<String> queues, String scope) {
        for (String queue : queues) {
            if (stats.containsKey(queue)) {
                stats.put(queue + " (" + scope + ")", count(queue));
                continue;
            }
            stats.put(queue, count(queue));
        }
    }

    private void purge(String queue) {
        rabbitAdmin.purgeQueue(queue, false);
    }

    private int count(String queue) {
        var info = rabbitAdmin.getQueueInfo(queue);
        return info == null ? 0 : info.getMessageCount();
    }
}
