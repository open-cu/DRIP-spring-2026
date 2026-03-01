package ru.cu.base.service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SelfInfoPresenterImpl implements SelfInfoPresenter {
    private int executionsCount;

    public String getSelfInfo() {
        return "hashCode: %s, executionsCount: %d".formatted(Integer.toHexString(hashCode()), ++executionsCount);
    }
}
