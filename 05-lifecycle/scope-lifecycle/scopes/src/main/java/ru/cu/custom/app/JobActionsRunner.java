package ru.cu.custom.app;

import ru.cu.custom.scope.JobActionContext;
import ru.cu.custom.scope.JobActionContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class JobActionsRunner {

    private final Map<String, JobAction> actions;

    public void runJobAction(String actionName, Map<String, Object> params) {
        var ctx = new JobActionContext(params);
        JobActionContextHolder.setJobActionContext(ctx);
        try {
            Optional.ofNullable(actions.get(actionName))
                    .orElseThrow(() -> new RuntimeException("Action not found!"))
                    .exec();
        } finally {
            JobActionContextHolder.resetJobContext();
        }
    }
}
