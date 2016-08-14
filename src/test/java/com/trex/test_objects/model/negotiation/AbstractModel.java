package com.trex.test_objects.model.negotiation;

import com.google.common.collect.Sets;
import com.trex.shared.annotations.UpdateAttributes;

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
