package unit;


import lecture.step02.repo.CategoryRepository;
import lecture.step02.service.CategoryService;
import lecture.step02.service.validator.CategoryValidator;
import lecture.step02.service.validator.LenientCategoryValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CategoryServiceMockedTest {

    @Test
    void create_insertsCategory_withoutDb() {
        // given
        CategoryRepository repo = mock(CategoryRepository.class);

        when(repo.insert(anyString())).thenReturn(1L);

        CategoryValidator validator = new LenientCategoryValidator();
        CategoryService service = new CategoryService(repo, validator);

        // when
        long id = service.create("Food");

        // then
        var captor = org.mockito.ArgumentCaptor.forClass(String.class);
        verify(repo).insert(captor.capture());

        assertEquals("Food", captor.getValue());
        assertEquals(1L, id);
    }

    @Test
    void create_validatesName() {
        // given
        CategoryRepository repo = mock(CategoryRepository.class);
        CategoryService service = new CategoryService(repo, new LenientCategoryValidator());

        // when + then
        assertThrows(IllegalArgumentException.class, () -> service.create(null));
        verify(repo, never()).insert(anyString());
    }
}
