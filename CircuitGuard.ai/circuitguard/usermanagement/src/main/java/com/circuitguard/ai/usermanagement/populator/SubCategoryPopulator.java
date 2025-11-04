package com.circuitguard.ai.usermanagement.populator;

import com.circuitguard.ai.usermanagement.dto.SubCategoryDTO;
import com.circuitguard.ai.usermanagement.model.CategoryModel;
import com.circuitguard.ai.usermanagement.model.SubCategoryModel;
import com.circuitguard.utils.Populator;
import org.springframework.stereotype.Component;

@Component
public class SubCategoryPopulator implements Populator<SubCategoryModel, SubCategoryDTO> {

    @Override
    public void populate(SubCategoryModel source, SubCategoryDTO target) {
        if (source == null || target == null) {
            return;
        }

        target.setId(source.getId());
        target.setCategoryId(source.getCategory() != null ? source.getCategory().getId() : null);
        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setActive(source.getActive());
    }

    public void populate(SubCategoryDTO source, SubCategoryModel target, CategoryModel parentCategory) {
        if (source == null || target == null) {
            return;
        }

        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setActive(source.getActive());
        target.setCategory(parentCategory);
    }
}
