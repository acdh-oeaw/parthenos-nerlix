/**
 * 
 */
package at.ac.oeaw.acdh.nerlix.discoveryportlet.datasource;

import java.util.Map;
import java.util.List;
import org.primefaces.model.LazyDataModel;

/**
 * @author WolfgangWalter SAUER (wowasa) <wolfgang.sauer@oeaw.ac.at>
 *
 */
public abstract class AbstractDataSource<T, K>{
	
	public abstract List<T> getCollections();
	
	public abstract List<T> getLanguages(); 
	
	public abstract List<T> getRessourceTypes();
	
	public abstract List<T> getNationalProjects();
	
	public abstract LazyDataModel<K> getResults(Map<String, Object> filter);
	

}
