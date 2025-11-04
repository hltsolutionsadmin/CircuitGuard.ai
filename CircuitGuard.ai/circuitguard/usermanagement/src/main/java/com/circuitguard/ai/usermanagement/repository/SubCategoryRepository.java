package com.circuitguard.ai.usermanagement.repository;

import com.circuitguard.ai.usermanagement.model.SubCategoryModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCategoryRepository extends JpaRepository<SubCategoryModel, Long> {

    boolean existsByCategoryIdAndNameIgnoreCase(Long categoryId, String name);

    Page<SubCategoryModel> findByCategoryId(Long categoryId, Pageable pageable);
}
