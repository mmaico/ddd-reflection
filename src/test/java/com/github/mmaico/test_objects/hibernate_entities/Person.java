package com.github.mmaico.test_objects.hibernate_entities;

//@Entity
//@Table(name = "persons")
public class Person {

  private static final long serialVersionUID = -6416371282639932944L;

  //@Id
  //@GeneratedValue
  private Long id;

  private String name;

  private Boolean active = Boolean.TRUE;

  public Person() {
    super();
  }

  public Person(String name) {
    super();
    this.name = name;
  }

  public Person(Long id) {
    super();
    this.id = id;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Person{");
    sb.append("id=").append(getId());
    sb.append(", name='").append(name).append('\'');
    sb.append('}');
    return sb.toString();
  }

}
