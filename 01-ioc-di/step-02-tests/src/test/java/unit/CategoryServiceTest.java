package unit;

import lecture.step02.service.CategoryService;
import lecture.step02.service.validator.LenientCategoryValidator;
import org.junit.jupiter.api.Test;
import fakes.FakeCategoryRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class CategoryServiceTest {

    @Test
    void create_insertsCategory_withoutDb() {
        FakeCategoryRepository repo = new FakeCategoryRepository();

        LenientCategoryValidator validator = new LenientCategoryValidator();

        CategoryService service = new CategoryService(repo, validator);

        // when
        long id = service.create("Food");

        // then
        assertEquals(1, repo.inserted.size());
        assertEquals(1L, id);
        assertEquals(1L, repo.inserted.get(0).id());
        assertEquals("Food", repo.inserted.get(0).name());
    }

    @Test
    void create_validatesName() {
        FakeCategoryRepository repo = new FakeCategoryRepository();
        CategoryService service = new CategoryService(repo, new LenientCategoryValidator());

        assertThrows(IllegalArgumentException.class, () -> service.create(null));
        assertEquals(0, repo.inserted.size());
    }
}
