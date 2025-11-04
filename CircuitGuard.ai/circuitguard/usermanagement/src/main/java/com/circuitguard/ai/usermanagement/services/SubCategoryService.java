package com.circuitguard.ai.usermanagement.services;

import com.circuitguard.ai.usermanagement.dto.SubCategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SubCategoryService {
    SubCategoryDTO createSubCategory(SubCategoryDTO dto);
    SubCategoryDTO updateSubCategory(Long id, SubCategoryDTO dto);
    SubCategoryDTO getSubCategory(Long id);
    Page<SubCategoryDTO> getAllSubCategories(Long categoryId, Pageable pageable);
    void deleteSubCategory(Long id);
}
