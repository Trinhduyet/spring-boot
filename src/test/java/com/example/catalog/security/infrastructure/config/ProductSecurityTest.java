package com.example.catalog.security.infrastructure.config;

import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.catalog.product.api.controller.ProductController;
import com.example.catalog.product.application.service.ProductService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
@Import(SecurityConfig.class)
@ActiveProfiles("security-test")
@TestPropertySource(
        properties = {"security.user.name=learner", "security.user.password=test-only-password"})
class ProductSecurityTest {
    @Autowired MockMvc mvc;
    @MockitoBean ProductService service;

    @Test
    void missingCredentialsAreRejected() throws Exception {
        mvc.perform(get("/api/v1/products")).andExpect(status().isUnauthorized());
    }

    @Test
    void validBasicCredentialsCanReadProducts() throws Exception {
        when(service.list(isNull(), isNull(), org.mockito.ArgumentMatchers.any()))
                .thenReturn(Page.empty());

        mvc.perform(get("/api/v1/products").with(httpBasic("learner", "test-only-password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void invalidPasswordIsRejected() throws Exception {
        mvc.perform(get("/api/v1/products").with(httpBasic("learner", "wrong-password")))
                .andExpect(status().isUnauthorized());
    }
}
