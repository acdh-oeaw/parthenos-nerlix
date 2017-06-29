package at.ac.oeaw.acdh.discoveryportlet.service;

import at.ac.oeaw.acdh.discoveryportlet.filter.Record;
import java.io.Serializable;
import java.util.List;

public abstract class AbstractSearch implements Serializable
{
  private static final long serialVersionUID = 1L;
  protected List<String> collections;
  protected List<String> languages;
  protected List<String> nationalProjects;
  protected List<String> resourceTypes;
  protected List<Record> records;
  
  public AbstractSearch() {}
  
  public List<String> getCollections()
  {
    return collections;
  }
  
  public void setCollections(List<String> collections) {
    this.collections = collections;
  }
  
  public List<String> getLanguages() {
    return languages;
  }
  
  public void setLanguages(List<String> languages) {
    this.languages = languages;
  }
  
  public List<String> getNationalProjects() {
    return nationalProjects;
  }
  
  public void setNationalProjects(List<String> nationalProjects) {
    this.nationalProjects = nationalProjects;
  }
  
  public List<String> getResourceTypes() {
    return resourceTypes;
  }
  
  public void setResourceTypes(List<String> resourceTypes) {
    this.resourceTypes = resourceTypes;
  }
  
  public List<Record> getRecords() {
    return records;
  }
  
  public void setRecords(List<Record> records) {
    this.records = records;
  }
}