package at.ac.oeaw.acdh.discoveryportlet.filter;

import java.io.Serializable;

public class Record implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String name;
  private String description;
  private String collection;
  private String language;
  private String nationalProject;
  private String resourceType;
  
  public Record(String name, String description, String collection, String language, String nationalProject, String resourceType)
  {
    this.name = name;
    this.description = description;
    this.collection = collection;
    this.language = language;
    this.nationalProject = nationalProject;
    this.resourceType = resourceType;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getShortDescription() {
    return description.substring(0, description.indexOf(' ', 149)) + "...";
  }
  
  public String getDescription() {
    return description;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
  
  public String getCollection() {
    return collection;
  }
  
  public void setCollection(String collection) {
    this.collection = collection;
  }
  
  public String getLanguage() {
    return language;
  }
  
  public void setLanguage(String language) {
    this.language = language;
  }
  
  public String getNationalProject() {
    return nationalProject;
  }
  
  public void setNationalProject(String nationalProject) {
    this.nationalProject = nationalProject;
  }
  
  public String getResourceType() {
    return resourceType;
  }
  
  public void setResourceType(String resourceType) {
    this.resourceType = resourceType;
  }
}