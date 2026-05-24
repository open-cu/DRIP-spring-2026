package demo.kafkademo.blocks.kafkadelayed;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;

import demo.kafkademo.core.model.DelayedOrderEvent;
import demo.kafkademo.service.DelayedUiStateService;
import org.junit.jupiter.api.Test;

class DelayedUiStateServiceTest {
    @Test
    void unsupportedDelayFallsBackToFiveSeconds() {
        DelayedUiStateService stateService = new DelayedUiStateService();

        DelayedOrderEvent event = stateService.registerOrder("заказ", 999);

        assertThat(event.delaySeconds()).isEqualTo(5);
        assertThat(event.processAt()).isEqualTo(event.createdAt().plusSeconds(5));
        assertThat(stateService.pageData().orders())
                .singleElement()
                .extracting(DelayedUiStateService.OrderView::delaySeconds)
                .isEqualTo(5);
    }

    @Test
    void processedEventMovesOrderToProcessedStatus() {
        DelayedUiStateService stateService = new DelayedUiStateService();
        DelayedOrderEvent event = stateService.registerOrder("заказ", 5);

        stateService.markProcessed(event);

        assertThat(stateService.pageData().orders())
                .singleElement()
                .satisfies(view -> {
                    assertThat(view.status()).isEqualTo("ОБРАБОТАН");
                    assertThat(view.processedAt()).isNotNull();
                });
    }

    @Test
    void delayedReadKeepsOrderPendingAndShowsNextAttemptTime() {
        DelayedUiStateService stateService = new DelayedUiStateService();
        DelayedOrderEvent event = stateService.registerOrder("заказ", 5);
        Instant processAt = event.processAt();

        stateService.markDelayedRead(event, processAt);

        assertThat(stateService.pageData().orders())
                .singleElement()
                .satisfies(view -> {
                    assertThat(view.status()).isEqualTo("ОЖИДАЕТ");
                    assertThat(view.note()).contains("partition на паузе до");
                    assertThat(view.dueAt()).isEqualTo(processAt);
                });
    }

    @Test
    void retryAttemptMarksOrderAsFailedWithAttemptNumber() {
        DelayedUiStateService stateService = new DelayedUiStateService();
        DelayedOrderEvent event = stateService.registerOrder("fail заказ", 5);

        stateService.markRetryAttempt(event, 2);

        assertThat(stateService.pageData().orders())
                .singleElement()
                .satisfies(view -> {
                    assertThat(view.status()).isEqualTo("ОШИБКА");
                    assertThat(view.note()).contains("retry attempt 2");
                });
    }
}
