package demo.kafkademo.testsupport;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.DockerClientFactory;

public class RequiresDockerCondition implements ExecutionCondition {
    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        try {
            DockerClientFactory.instance().client();
            return ConditionEvaluationResult.enabled("Docker is available");
        } catch (Throwable ex) {
            return ConditionEvaluationResult.disabled("Docker is not available: " + ex.getMessage());
        }
    }
}
