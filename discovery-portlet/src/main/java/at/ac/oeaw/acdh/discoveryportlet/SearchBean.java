package at.ac.oeaw.acdh.discoveryportlet;

import at.ac.oeaw.acdh.discoveryportlet.filter.Record;
import at.ac.oeaw.acdh.discoveryportlet.service.AbstractSearch;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;










@ManagedBean
@ViewScoped
public class SearchBean
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private Record selectedRecord;
  private String[] selectedCollections;
  private String[] selectedLanguages;
  private String[] selectedNationalProjects;
  private String[] selectedResourceTypes;
  private List<Record> records;
  @ManagedProperty("#{dummySearch}")
  private AbstractSearch searchService;
  
  public SearchBean() {}
  
  @PostConstruct
  public void init()
  {
    records = searchService.getRecords();
  }
  
  public List<String> getCollections()
  {
    return searchService.getCollections();
  }
  
  public List<String> getLanguages()
  {
    return searchService.getLanguages();
  }
  
  public List<String> getNationalProjects() {
    return searchService.getNationalProjects();
  }
  
  public List<String> getResourceTypes() {
    return searchService.getResourceTypes();
  }
  
  public List<Record> getRecords() {
    return records;
  }
  
  public Record getSelectedRecord() { return selectedRecord; }
  
  public void setSelectedRecord(Record selectedRecord) {
    this.selectedRecord = selectedRecord;
  }
  
  public String[] getSelectedCollections() { return selectedCollections; }
  
  public void setSelectedCollections(String[] selectedCollections) {
    this.selectedCollections = selectedCollections;
  }
  
  public String[] getSelectedLanguages() { return selectedLanguages; }
  
  public void setSelectedLanguages(String[] selectedLanguages) {
    this.selectedLanguages = selectedLanguages;
  }
  
  public String[] getSelectedNationalProjects() { return selectedNationalProjects; }
  
  public void setSelectedNationalProjects(String[] selectedNationalProjects) {
    this.selectedNationalProjects = selectedNationalProjects;
  }
  
  public String[] getSelectedResourceTypes() { return selectedResourceTypes; }
  
  public void setSelectedResourceTypes(String[] selectedResourceTypes) {
    this.selectedResourceTypes = selectedResourceTypes;
  }
  
  public void setSearchService(AbstractSearch searchService) { this.searchService = searchService; }
}