package ru.cu.base.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cu.base.dto.SelfInfoPresenterDto;
import ru.cu.base.service.SelfInfoPresenter;

@RestController
public class SelfInfoPresenterController {
    
    private final SelfInfoPresenter singletonScopeBean;
    private final SelfInfoPresenter prototypeScopeBean1;
    private final SelfInfoPresenter prototypeScopeBean2;
    private final SelfInfoPresenter proxiedPrototypeScopeBean;
    private final SelfInfoPresenter sessionScopeBean;
    private final SelfInfoPresenter requestScopeBean;

    public SelfInfoPresenterController(@Qualifier("singletonScopeBean") SelfInfoPresenter singletonScopeBean,
                                       @Qualifier("prototypeScopeBean") SelfInfoPresenter prototypeScopeBean1,
                                       @Qualifier("prototypeScopeBean") SelfInfoPresenter prototypeScopeBean2,
                                       @Qualifier("proxiedPrototypeScopeBean") SelfInfoPresenter proxiedPrototypeScopeBean,
                                       @Qualifier("sessionScopeBean") SelfInfoPresenter sessionScopeBean,
                                       @Qualifier("requestScopeBean") SelfInfoPresenter requestScopeBean) {
        this.singletonScopeBean = singletonScopeBean;
        this.prototypeScopeBean1 = prototypeScopeBean1;
        this.prototypeScopeBean2 = prototypeScopeBean2;
        this.proxiedPrototypeScopeBean = proxiedPrototypeScopeBean;
        this.sessionScopeBean = sessionScopeBean;
        this.requestScopeBean = requestScopeBean;
    }

    @GetMapping("/api/self-info")
    public SelfInfoPresenterDto getSelfInfo() {
        return new SelfInfoPresenterDto(singletonScopeBean.getSelfInfo(),
                prototypeScopeBean1.getSelfInfo(),
                prototypeScopeBean2.getSelfInfo(),
                proxiedPrototypeScopeBean.getSelfInfo(),
                sessionScopeBean.getSelfInfo(),
                requestScopeBean.getSelfInfo()
                );
    }
}
