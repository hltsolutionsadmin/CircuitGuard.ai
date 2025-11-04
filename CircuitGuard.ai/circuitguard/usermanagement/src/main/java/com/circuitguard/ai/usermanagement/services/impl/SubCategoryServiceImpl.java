package com.circuitguard.ai.usermanagement.services.impl;

import com.circuitguard.ai.usermanagement.dto.SubCategoryDTO;
import com.circuitguard.ai.usermanagement.model.CategoryModel;
import com.circuitguard.ai.usermanagement.model.SubCategoryModel;
import com.circuitguard.ai.usermanagement.populator.SubCategoryPopulator;
import com.circuitguard.ai.usermanagement.repository.CategoryRepository;
import com.circuitguard.ai.usermanagement.repository.SubCategoryRepository;
import com.circuitguard.ai.usermanagement.services.SubCategoryService;
import com.circuitguard.auth.exception.handling.ErrorCode;
import com.circuitguard.auth.exception.handling.HltCustomerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SubCategoryServiceImpl implements SubCategoryService {

    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryPopulator subCategoryPopulator;

    @Override
    public SubCategoryDTO createSubCategory(SubCategoryDTO dto) {
        CategoryModel category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new HltCustomerException(ErrorCode.CATEGORY_NOT_FOUND));

        if (subCategoryRepository.existsByCategoryIdAndNameIgnoreCase(dto.getCategoryId(), dto.getName())) {
            throw new HltCustomerException(ErrorCode.DUPLICATE_SUBCATEGORY);
        }

        SubCategoryModel model = new SubCategoryModel();
        subCategoryPopulator.populate(dto, model, category);
        subCategoryRepository.save(model);

        SubCategoryDTO result = new SubCategoryDTO();
        subCategoryPopulator.populate(model, result);
        return result;
    }

    @Override
    public SubCategoryDTO updateSubCategory(Long id, SubCategoryDTO dto) {
        SubCategoryModel model = subCategoryRepository.findById(id)
                .orElseThrow(() -> new HltCustomerException(ErrorCode.SUBCATEGORY_NOT_FOUND));

        CategoryModel category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new HltCustomerException(ErrorCode.CATEGORY_NOT_FOUND));

        subCategoryPopulator.populate(dto, model, category);
        subCategoryRepository.save(model);

        SubCategoryDTO result = new SubCategoryDTO();
        subCategoryPopulator.populate(model, result);
        return result;
    }

    @Override
    public SubCategoryDTO getSubCategory(Long id) {
        SubCategoryModel model = subCategoryRepository.findById(id)
                .orElseThrow(() -> new HltCustomerException(ErrorCode.SUBCATEGORY_NOT_FOUND));

        SubCategoryDTO dto = new SubCategoryDTO();
        subCategoryPopulator.populate(model, dto);
        return dto;
    }

    @Override
    public Page<SubCategoryDTO> getAllSubCategories(Long categoryId, Pageable pageable) {
        return subCategoryRepository.findByCategoryId(categoryId, pageable)
                .map(model -> {
                    SubCategoryDTO dto = new SubCategoryDTO();
                    subCategoryPopulator.populate(model, dto);
                    return dto;
                });
    }

    @Override
    public void deleteSubCategory(Long id) {
        SubCategoryModel model = subCategoryRepository.findById(id)
                .orElseThrow(() -> new HltCustomerException(ErrorCode.SUBCATEGORY_NOT_FOUND));
        subCategoryRepository.delete(model);
    }
}
