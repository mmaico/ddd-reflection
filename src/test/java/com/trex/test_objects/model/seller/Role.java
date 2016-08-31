package com.trex.test_objects.model.seller;


import com.trex.shared.annotations.Model;

import java.util.Set;

@Model
public class Role {

  private Long id;

  private Set<RoleType> types;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Set<RoleType> getTypes() {
    return types;
  }

  public void setTypes(Set<RoleType> types) {
    this.types = types;
  }
}
