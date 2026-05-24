package demo.kafkademo.blocks.kafkabatch;

import static org.assertj.core.api.Assertions.assertThat;

import demo.kafkademo.core.model.BatchOrderEvent;
import demo.kafkademo.service.BatchUiStateService;
import org.junit.jupiter.api.Test;

class BatchUiStateServiceTest {
    @Test
    void countIsUpdatedWhenEventMovesToDlt() {
        BatchUiStateService stateService = new BatchUiStateService();
        BatchOrderEvent event = stateService.createFailingEvent("ошибка");

        stateService.markFailed(event, "одно событие ломает пачку");
        stateService.markDlt(event);

        BatchUiStateService.PageData data = stateService.pageData();
        assertThat(data.published()).isEqualTo(1);
        assertThat(data.failed()).isEqualTo(1);
        assertThat(data.sentToDlt()).isEqualTo(1);
        assertThat(data.orders()).singleElement()
                .extracting(BatchUiStateService.BatchOrderView::status)
                .isEqualTo("DLT");
    }

    @Test
    void blankDescriptionUsesBatchFallback() {
        BatchUiStateService stateService = new BatchUiStateService();

        BatchOrderEvent event = stateService.createOkEvent(" ");

        assertThat(event.description()).startsWith("batch заказ");
    }
}
