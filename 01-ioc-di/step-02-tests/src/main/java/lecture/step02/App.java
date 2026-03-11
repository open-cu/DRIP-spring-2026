package lecture.step02;


import lecture.step02.controller.CategoryController;
import lecture.step02.repo.CategoryRepository;
import lecture.step02.repo.DbCategoryRepository;
import lecture.step02.service.CategoryService;
import lecture.step02.service.io.ConsoleIoService;
import lecture.step02.service.io.IoService;
import lecture.step02.service.validator.CategoryValidator;
import lecture.step02.service.validator.CommonCategoryValidator;
import lecture.step02.service.validator.LenientCategoryValidator;
import lecture.step02.service.validator.StrictCategoryValidator;
import lecture.step02.storage.DataSource;
import lecture.step02.storage.InMemoryStorage;
import lecture.step02.storage.JdbcStorage;
import lecture.step02.storage.Storage;

public class App {
    public static void main(String[] args) {
        String profile = System.getProperty("profile", "demo"); // demo | prod
        String validator = System.getProperty("validator", "strict"); // strict | lenient | common

        Storage storage = profile.equalsIgnoreCase("prod")
                ? new JdbcStorage(new DataSource())
                : new InMemoryStorage();

        CategoryRepository repo = new DbCategoryRepository(storage);

        CategoryValidator categoryValidator = switch (validator) {
            case "strict" -> new StrictCategoryValidator();
            case "lenient" -> new LenientCategoryValidator();
            case "common" -> new CommonCategoryValidator();
            default -> throw new IllegalArgumentException(
                    "Unknown validator: " + validator
            );
        };

        CategoryService service = new CategoryService(repo, categoryValidator);
        IoService ioService = new ConsoleIoService();

        CategoryController controller = new CategoryController(service, ioService);

        controller.run();
    }
}
