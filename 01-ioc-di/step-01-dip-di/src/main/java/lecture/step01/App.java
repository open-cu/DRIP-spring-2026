package lecture.step01;


import lecture.step01.controller.CategoryController;
import lecture.step01.repo.CategoryRepository;
import lecture.step01.repo.DbCategoryRepository;
import lecture.step01.service.validator.CategoryValidator;
import lecture.step01.service.CategoryService;
import lecture.step01.service.validator.LenientCategoryValidator;
import lecture.step01.service.validator.StrictCategoryValidator;
import lecture.step01.service.io.ConsoleIoService;
import lecture.step01.storage.DataSource;
import lecture.step01.storage.InMemoryStorage;
import lecture.step01.storage.JdbcStorage;
import lecture.step01.storage.Storage;

public class App {
    public static void main(String[] args) {
        String profile = System.getProperty("profile", "demo"); // demo | prod

        Storage storage = profile.equalsIgnoreCase("prod")
                ? new JdbcStorage(new DataSource())
                : new InMemoryStorage();

        CategoryRepository repo = new DbCategoryRepository(storage);

        CategoryValidator validator = profile.equalsIgnoreCase("prod")
                ? new StrictCategoryValidator()
                : new LenientCategoryValidator();

        CategoryService service = new CategoryService(repo, validator);
        ConsoleIoService ioService = new ConsoleIoService();

        CategoryController controller = new CategoryController(service, ioService);

        controller.run();
    }
}
