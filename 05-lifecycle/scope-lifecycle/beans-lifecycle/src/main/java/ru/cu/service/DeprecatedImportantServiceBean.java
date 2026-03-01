package ru.cu.service;

import org.springframework.stereotype.Component;

@Component
public class DeprecatedImportantServiceBean implements ImportantService {

    @Override
    public void doWork() {
        System.out.println("Делаю дело устаревшим способом!");
    }
}
