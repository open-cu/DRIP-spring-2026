package demo.kafkademo.blocks.kafkadist;

import static org.assertj.core.api.Assertions.assertThat;

import demo.kafkademo.core.model.DistributionEvent;
import demo.kafkademo.service.DistributionUiStateService;
import org.junit.jupiter.api.Test;

class DistributionUiStateServiceTest {
    @Test
    void blankKeyAndPayloadGetStudentFriendlyFallbacks() {
        DistributionUiStateService stateService = new DistributionUiStateService();

        DistributionEvent event = stateService.createEvent(" ", "");

        assertThat(event.key()).isEqualTo("order-42");
        assertThat(event.payload()).isEqualTo("событие заказа");
    }

    @Test
    void receivedEventKeepsPartitionAndOffsetForUi() {
        DistributionUiStateService stateService = new DistributionUiStateService();
        DistributionEvent event = stateService.createEvent("order-1", "paid");

        stateService.markReceived(event, 2, 17, "consumer-thread");

        assertThat(stateService.pageData().events())
                .singleElement()
                .satisfies(view -> {
                    assertThat(view.partition()).isEqualTo(2);
                    assertThat(view.offset()).isEqualTo(17);
                    assertThat(view.consumerName()).isEqualTo("consumer-thread");
                });
    }
}
