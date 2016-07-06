package com.trex.clone.objects.ddd.seller;


import java.util.Set;


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
