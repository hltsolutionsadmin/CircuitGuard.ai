package com.circuitguard.ai.usermanagement.controllers;

import com.circuitguard.ai.usermanagement.dto.CategoryDTO;
import com.circuitguard.ai.usermanagement.services.CategoryService;
import com.circuitguard.commonservice.dto.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public StandardResponse<CategoryDTO> create(@RequestBody CategoryDTO dto) {
        CategoryDTO created = categoryService.createCategory(dto);
        return StandardResponse.single("Category created successfully", created);
    }

    @PutMapping("/{id}")
    public StandardResponse<CategoryDTO> update(@PathVariable Long id, @RequestBody CategoryDTO dto) {
        CategoryDTO updated = categoryService.updateCategory(id, dto);
        return StandardResponse.single("Category updated successfully", updated);
    }

    @GetMapping("/{id}")
    public StandardResponse<CategoryDTO> get(@PathVariable Long id) {
        CategoryDTO category = categoryService.getCategory(id);
        return StandardResponse.single("Category fetched successfully", category);
    }

    @GetMapping("/org/{orgId}")
    public StandardResponse<Page<CategoryDTO>> list(
            @PathVariable Long orgId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<CategoryDTO> categories = categoryService.getAllCategories(orgId, pageable);
        return StandardResponse.page("Categories fetched successfully", categories);
    }

    @DeleteMapping("/{id}")
    public StandardResponse<String> delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return StandardResponse.message("Category deleted successfully");
    }
}
