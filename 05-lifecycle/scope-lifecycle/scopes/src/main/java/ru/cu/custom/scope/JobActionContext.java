package ru.cu.custom.scope;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JobActionContext {
    private final Map<String, Object> params;

    public JobActionContext() {
         params = new HashMap<>();
    }

    public JobActionContext(Map<String, Object> params) {
        this.params = params;
    }

    public void putParam(String name, Object param) {
        params.put(name, param);
    }

    public Map<String, Object> getParams() {
        return Collections.unmodifiableMap(params);
    }
}
