package ru.cu.base.dto;

public record SelfInfoPresenterDto(String singletonScopeBeanHash,
                                   String prototypeScopeBean1Hash,
                                   String prototypeScopeBean2Hash,
                                   String proxiedPrototypeScopeBeanHash,
                                   String sessionScopeBeanHash,
                                   String requestScopeBeanHash
) {
}
