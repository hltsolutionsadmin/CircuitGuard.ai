package com.circuitguard.ai.usermanagement.populator;

import com.circuitguard.ai.usermanagement.dto.CategoryDTO;
import com.circuitguard.ai.usermanagement.dto.SubCategoryDTO;
import com.circuitguard.ai.usermanagement.model.CategoryModel;
import com.circuitguard.ai.usermanagement.model.SubCategoryModel;
import com.circuitguard.utils.Populator;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CategoryPopulator implements Populator<CategoryModel, CategoryDTO> {

    @Override
    public void populate(CategoryModel source, CategoryDTO target) {
        if (source == null || target == null) {
            return;
        }

        target.setId(source.getId());
        target.setOrganizationId(source.getOrganizationId());
        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setActive(source.getActive());

        if (source.getSubCategories() != null && !source.getSubCategories().isEmpty()) {
            target.setSubCategories(
                    source.getSubCategories().stream()
                            .map(sub -> {
                                SubCategoryDTO dto = new SubCategoryDTO();
                                dto.setId(sub.getId());
                                dto.setName(sub.getName());
                                dto.setDescription(sub.getDescription());
                                dto.setActive(sub.getActive());
                                return dto;
                            })
                            .collect(Collectors.toList())
            );
        }

    }

    public void populate(CategoryDTO source, CategoryModel target) {
        if (source == null || target == null) {
            return;
        }

        target.setOrganizationId(source.getOrganizationId());
        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setActive(source.getActive());

        if (source.getSubCategories() != null && !source.getSubCategories().isEmpty()) {
            target.setSubCategories(
                    source.getSubCategories().stream()
                            .map(sub -> {
                                SubCategoryModel model = new SubCategoryModel();
                                model.setId(sub.getId());
                                model.setName(sub.getName());
                                model.setDescription(sub.getDescription());
                                model.setActive(sub.getActive());
                                model.setCategory(target);
                                return model;
                            })
                            .collect(Collectors.toSet())
            );
        }
    }
}
