package io.myrecipes.front.controller;

import io.myrecipes.front.dto.PageParam;
import io.myrecipes.front.dto.Recipe;
import io.myrecipes.front.service.IndexService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;

import static org.hamcrest.Matchers.contains;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(IndexController.class)
public class IndexControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IndexService indexService;

    @Value("${app.img-path}")
    private String imgPath;

    @Value("${app.index.page-size}")
    private int pageSize;

    @Value("${app.index.sort-field}")
    private String sortField;

    @Value("${app.index.is-descending}")
    private boolean isDescending;

    @Test
    public void 메인_페이지_호출() throws Exception {
        Recipe recipe = new Recipe("test1", "test1.jpg", 30, "1", 1001);
        PageParam pageParam = new PageParam(0, this.pageSize, this.sortField, this.isDescending);
        given(this.indexService.readRecipeList(argThat(new PageParamMatcher(pageParam)))).willReturn(Collections.singletonList(recipe));

        final ResultActions actions = this.mockMvc.perform(get("/index"));

        actions.andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("popularRecipeList", "newRecipeList", "imgPath"))
                .andExpect(model().attribute("popularRecipeList", contains(recipe)))
                .andExpect(model().attribute("newRecipeList", contains(recipe)))
                .andExpect(model().attribute("imgPath", this.imgPath));
    }

    static class PageParamMatcher implements ArgumentMatcher<PageParam> {
        private PageParam left;

        PageParamMatcher(PageParam left) {
            this.left = left;
        }

        @Override
        public boolean matches(PageParam right) {
            return left.getPage() == right.getPage()
                    && left.getSize() == right.getSize()
                    && left.getSortField().equals(right.getSortField())
                    && left.isDescending() == right.isDescending();
        }
    }
}