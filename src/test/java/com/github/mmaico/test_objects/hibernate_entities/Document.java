package com.github.mmaico.test_objects.hibernate_entities;


public class Document {
  public enum DocumentTypeEnum {PASSPORT, SOCIAL_SECURITY_CARD}
  private String document;
  private DocumentTypeEnum type;

  public Document(){}
  public Document(String document, DocumentTypeEnum type) {
    this.document = document;
    this.type = type;
  }

  public String getDocument() {
    return document;
  }

  public void setDocument(String document) {
    this.document = document;
  }

  public DocumentTypeEnum getType() {
    return type;
  }

  public void setType(DocumentTypeEnum type) {
    this.type = type;
  }
}
