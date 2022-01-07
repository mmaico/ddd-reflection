package com.github.mmaico.test_objects.model.negotiation;

import com.github.mmaico.shared.annotations.UpdateAttributes;
import com.google.common.collect.Sets;

import java.util.Set;


public abstract class AbstractModel {

    @UpdateAttributes
    private Set<String> updateAttributes = Sets.newHashSet();

    public Set<String> getUpdateAttributes() {
        return updateAttributes;
    }

    public void setUpdateAttributes(Set<String> updateAttributes) {
        this.updateAttributes = updateAttributes;
    }
}
