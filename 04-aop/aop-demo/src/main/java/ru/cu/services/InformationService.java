package ru.cu.services;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InformationService {
    private final ActionsService actionsService;


    public void showServiceInfo() {
        System.out.printf("Хэш сервиса информации: %s, Хэш вложенного сервиса: %s%n",
                Integer.toHexString(this.hashCode()),
                Integer.toHexString(actionsService.hashCode()));
    }

}
