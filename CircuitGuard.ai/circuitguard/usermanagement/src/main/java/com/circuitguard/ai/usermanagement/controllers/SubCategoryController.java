package com.circuitguard.ai.usermanagement.controllers;

import com.circuitguard.ai.usermanagement.dto.SubCategoryDTO;
import com.circuitguard.ai.usermanagement.services.SubCategoryService;
import com.circuitguard.commonservice.dto.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subcategories")
@RequiredArgsConstructor
public class SubCategoryController {

    private final SubCategoryService subCategoryService;

    @PostMapping
    public StandardResponse<SubCategoryDTO> create(@RequestBody SubCategoryDTO dto) {
        SubCategoryDTO created = subCategoryService.createSubCategory(dto);
        return StandardResponse.single("Subcategory created successfully", created);
    }

    @PutMapping("/{id}")
    public StandardResponse<SubCategoryDTO> update(@PathVariable Long id, @RequestBody SubCategoryDTO dto) {
        SubCategoryDTO updated = subCategoryService.updateSubCategory(id, dto);
        return StandardResponse.single("Subcategory updated successfully", updated);
    }

    @GetMapping("/{id}")
    public StandardResponse<SubCategoryDTO> get(@PathVariable Long id) {
        SubCategoryDTO subCategory = subCategoryService.getSubCategory(id);
        return StandardResponse.single("Subcategory fetched successfully", subCategory);
    }

    @GetMapping("/category/{categoryId}")
    public StandardResponse<Page<SubCategoryDTO>> list(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<SubCategoryDTO> subCategories = subCategoryService.getAllSubCategories(categoryId, pageable);
        return StandardResponse.page("Subcategories fetched successfully", subCategories);
    }

    @DeleteMapping("/{id}")
    public StandardResponse<String> delete(@PathVariable Long id) {
        subCategoryService.deleteSubCategory(id);
        return StandardResponse.message("Subcategory deleted successfully");
    }
}
