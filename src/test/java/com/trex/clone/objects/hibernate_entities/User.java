package com.trex.clone.objects.hibernate_entities;

import java.util.List;
import java.util.Objects;

//@Entity
//@Table(name="users")
public class User {

  /**
   *
   */
  private static final long serialVersionUID = 282685797677443589L;

  //@Id
  //@GeneratedValue
  private Long id;

  private String login;

  private String password;

  private String passwordConfirm;

  private String name;

  private String lastname;

  private String email;

  private List<Document> documents;


  public User() {
    super();
  }

  public User(Long id) {
    this.id = id;
  }

  public User(String login, String password) {
    super();
    this.login = login;
    this.password = password;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPasswordConfirm() {
    return passwordConfirm;
  }

  public void setPasswordConfirm(String passwordConfirm) {
    this.passwordConfirm = passwordConfirm;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }


  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }


  public List<Document> getDocuments() {
    return documents;
  }

  public void setDocuments(List<Document> documents) {
    this.documents = documents;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("User{");
    sb.append("id=").append(getId());
    sb.append(", login='").append(login).append('\'');
    sb.append(", password='").append(password).append('\'');
    sb.append('}');
    return sb.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    User user = (User) o;
    return Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(login);
  }
}
