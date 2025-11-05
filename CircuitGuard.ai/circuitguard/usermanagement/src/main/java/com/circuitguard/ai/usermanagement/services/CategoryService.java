package com.circuitguard.ai.usermanagement.services;

import com.circuitguard.ai.usermanagement.dto.CategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryDTO createCategory(CategoryDTO dto);
    CategoryDTO updateCategory(Long id, CategoryDTO dto);
    CategoryDTO getCategory(Long id);
    Page<CategoryDTO> getAllCategories(Long orgId, Pageable pageable);
    Page<CategoryDTO> getAllCategoriesByProject(Long projectId, Pageable pageable);
    void deleteCategory(Long id);
}
