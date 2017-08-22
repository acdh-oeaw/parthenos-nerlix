package at.ac.oeaw.acdh.nerlix.discoveryportlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.faces.bean.*;

import org.primefaces.model.LazyDataModel;

import at.ac.oeaw.acdh.nerlix.discoveryportlet.datasource.AbstractDataSource;


@ManagedBean
@ViewScoped
public class ViewBean implements Serializable
{
  private static final long serialVersionUID = 1L;
  private Object selectedResult;

  private HashMap<String,Object> filter;
  
  private LazyDataModel<?> results;

  
  @ManagedProperty("#{solrDataSource}")
  private AbstractDataSource<?, ?> dataSource;
  
  


  public ViewBean(){
	  
	  this.filter = new HashMap<String,Object>(); 

  }


  	public List<?> getCollections()
  {
    return this.dataSource.getCollections();
  }
  
  public List<?> getLanguages()
  {
    return this.dataSource.getLanguages();
  }
  
  public List<?> getNationalProjects() {
    return null;
  }
  
  public List<?> getResourceTypes() {
    return this.dataSource.getRessourceTypes();
  }
  
  public LazyDataModel<?> getResults() {
	  if(this.results == null)
		  this.results = dataSource.getResults(this.filter);
	  return this.results;
  }
  
  public String getGlobalFilter() {
	return (String)this.filter.get("globalFilter");
}


public void setGlobalFilter(String globalFilter) {
	this.filter.put("globalFilter", globalFilter);
}


public Object getSelectedResult(){ 
	  return selectedResult; 
  }
  
  
  public void setSelectedResult(Object selectedResult) {
    this.selectedResult = selectedResult;
  }
  
  public String[] getSelectedCollections(){ 
	  return (String[]) this.filter.get("datasourcenameforbrowsing");
  }
  
  public void setSelectedCollections(String[] selectedCollections) {
	  this.filter.put("datasourcenameforbrowsing", selectedCollections);
	  this.results = null;
  }
  
  public String[] getSelectedLanguages(){ 
	  return (String[]) this.filter.get("language"); 
  }
  
  public void setSelectedLanguages(String[] selectedLanguages){
    this.filter.put("language", selectedLanguages);
    this.results = null;
  }
  
  public String[] getSelectedNationalProjects(){ 
	  return null; 
  }
  
  public void setSelectedNationalProjects(String[] selectedNationalProjects){
    //nothing yet
  }
  
  public String[] getSelectedResourceTypes(){ 
	  return (String[]) this.filter.get("type");
  }
  
  public void setSelectedResourceTypes(String[] selectedResourceTypes) {
    this.filter.put("type", selectedResourceTypes);
    this.results = null;
  }
  
  public void setDataSource(AbstractDataSource<?, ?> dataSource) { 
	  this.dataSource = dataSource; 
  }
}