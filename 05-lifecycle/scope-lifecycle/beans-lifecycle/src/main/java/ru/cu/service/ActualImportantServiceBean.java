package ru.cu.service;

public class ActualImportantServiceBean implements ImportantService {

    @Override
    public void doWork() {
        System.out.println("Делаю дело современным способом!");
    }
}
