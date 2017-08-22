/**
 * 
 */
package at.ac.oeaw.acdh.nerlix.discoveryportlet.datasource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.LazyDataModel;

import java.util.Map;
import java.util.HashMap;


/**
 * 
 * This class provides dummy data for testing without database connection
 * 
 * @author WolfgangWalter SAUER (wowasa) <wolfgang.sauer@oeaw.ac.at>
 *
 */

public class RandomDataSource extends AbstractDataSource implements Serializable{
	  protected List<String> collections;
	  protected List<String> languages;
	  protected List<String> nationalProjects;
	  protected List<String> resourceTypes;
	  protected List<Result> results;
	  
	public RandomDataSource(){
	    languages = new ArrayList<String>();
	    collections = new ArrayList<String>();
	    resourceTypes = new ArrayList<String>();
	    nationalProjects = new ArrayList<String>();
	    
	    for (int i = 1; i < 5; i++) {
	      languages.add("Language-" + i);
	      collections.add("Collection-" + i);
	      resourceTypes.add("Resourcetype-" + i);
	      nationalProjects.add("National project-" + i);
	    }
	    
	    this.results = new ArrayList<Result>();
	    
	    Random random = new Random();
	    
	    for (int i = 0; i < 50; i++) {
	      this.results.add(new Result(
	        UUID.randomUUID().toString(), "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, "
	        		+ "sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. "
	        		+ "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. "
	        		+ "At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.", 
	        		"collection-" + (random.nextInt(4) + 1), 
	        		"language-" + (random.nextInt(4) + 1), 
	        		"national project-" + (random.nextInt(4) + 1), 
	        		"resource type-" + (random.nextInt(4) + 1)
	        		)
	    		  );
	    }
	}

	/* (non-Javadoc)
	 * @see at.ac.oeaw.acdh.nerlix.discoveryportlet.datasource.AbstractDataSource#getCollections()
	 */
	@Override
	public List<?> getCollections() {
		return this.collections;
	}

	/* (non-Javadoc)
	 * @see at.ac.oeaw.acdh.nerlix.discoveryportlet.datasource.AbstractDataSource#getLanguages()
	 */
	@Override
	public List<?> getLanguages() {
		return this.languages;
	}

	/* (non-Javadoc)
	 * @see at.ac.oeaw.acdh.nerlix.discoveryportlet.datasource.AbstractDataSource#getRessourceTypes()
	 */
	@Override
	public List<?> getRessourceTypes() {
		return this.resourceTypes;
	}

	/* (non-Javadoc)
	 * @see at.ac.oeaw.acdh.nerlix.discoveryportlet.datasource.AbstractDataSource#getNationalProjects()
	 */
	@Override
	public List<?> getNationalProjects() {
		return this.nationalProjects;
	}

	/* (non-Javadoc)
	 * @see at.ac.oeaw.acdh.nerlix.discoveryportlet.datasource.AbstractDataSource#getResults(java.lang.Object)
	 */
	@Override
	public LazyDataModel getResults(Map filter) {
			return new LazyDataModel(){	
		};
	}
	
	public class Result{
		private HashMap<String, Object> map;
		public Result(String name, String description, String collection, String language, String nationalProject, String resourceType){
			this.map = new HashMap<String, Object>();
			
			this.map.put("name", name);
			this.map.put("description", description);
			this.map.put("language", language);
			this.map.put("nationalProject", nationalProject);
			this.map.put("resourceType", resourceType);
		}
		
		public void setField(String name, Object value){
			this.map.put(name, value);
		}
		
		public Object getFieldValue(String name){
			return this.map.get(name);
		}
	}

}
