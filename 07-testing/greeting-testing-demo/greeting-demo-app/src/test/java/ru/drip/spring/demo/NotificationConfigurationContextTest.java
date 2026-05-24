package ru.drip.spring.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = NotificationConfiguration.class)
@TestPropertySource(properties = "feature.notify=false")
@ActiveProfiles("dev")
class NotificationConfigurationContextTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void shouldBuildOnlyMinimalSpringContextForDevProfile() {
        assertThat(notificationService).isNotNull();
        assertThat(applicationContext.getBeanNamesForType(NotificationService.class))
                .containsExactly("devNoopNotificationService");
        assertThat(applicationContext.containsBean("realNotificationService")).isFalse();
    }
}
