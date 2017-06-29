package at.ac.oeaw.acdh.discoveryportlet.service;

import at.ac.oeaw.acdh.discoveryportlet.filter.Record;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;



@ManagedBean
@ApplicationScoped
public class DummySearch
  extends AbstractSearch
{
  private static final long serialVersionUID = 1L;
  
  public DummySearch()
  {
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
    

    records = new ArrayList<Record>();
    
    Random random = new Random();
    
    for (int i = 0; i < 50; i++) {
      records.add(new Record(
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
}