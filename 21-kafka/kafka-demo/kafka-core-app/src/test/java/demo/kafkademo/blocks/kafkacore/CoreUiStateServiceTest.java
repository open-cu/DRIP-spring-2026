package demo.kafkademo.blocks.kafkacore;

import static org.assertj.core.api.Assertions.assertThat;

import demo.kafkademo.service.CoreUiStateService;
import org.junit.jupiter.api.Test;

class CoreUiStateServiceTest {
    @Test
    void blankOrderDescriptionUsesReadableFallback() {
        CoreUiStateService stateService = new CoreUiStateService();

        stateService.registerOrder("   ");

        assertThat(stateService.pageData().orders())
                .singleElement()
                .extracting(CoreUiStateService.OrderView::description)
                .isEqualTo("заказ из core-блока");
    }

    @Test
    void streamResultUpdatesExistingRow() {
        CoreUiStateService stateService = new CoreUiStateService();
        CoreUiStateService.StreamInput input = stateService.registerStreamInput("stream заказ");

        stateService.markStreamResult(input.id() + "|STREAM_ACCEPTED|STREAM ЗАКАЗ");

        assertThat(stateService.pageData().streams())
                .singleElement()
                .satisfies(view -> {
                    assertThat(view.input()).isEqualTo("stream заказ");
                    assertThat(view.result()).contains("STREAM_ACCEPTED");
                    assertThat(view.processedAt()).isNotNull();
                });
    }
}
