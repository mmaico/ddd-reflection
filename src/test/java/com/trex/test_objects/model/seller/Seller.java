package com.trex.test_objects.model.seller;


import com.trex.shared.annotations.Model;
import com.trex.test_objects.model.negotiation.AbstractModel;


@Model
public class Seller extends AbstractModel {

  private Long id;

  private String name;

  //private List<Role> roles = Lists.newArrayList();


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

//  public List<Role> getRoles() {
//    return roles;
//  }

//  public Boolean hasRole(RoleType type) {
//    return roles.stream().filter(role -> role.getTypes().contains(type)).count() > 0;
//  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Negotiation Seller {");
    sb.append("id=").append(getId());
    sb.append('}');
    return sb.toString();
  }
}
