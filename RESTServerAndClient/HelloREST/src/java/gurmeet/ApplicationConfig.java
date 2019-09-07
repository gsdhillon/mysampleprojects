package gurmeet;


import java.util.Set;
import javax.ws.rs.core.Application;

/**
 * Glassfish 4.1.1 
 * @author Gurmeet Singh, gsdhillon@gmail.com
 */
@javax.ws.rs.ApplicationPath("rest")
public class ApplicationConfig extends Application {
    

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        
        //default jaxb provider is MOXy - has a Bug
        //replcae org.eclipse.persistence.moxy.jar  with
        //  -- org.eclipse.persistence.moxy-2.6.1.jar to remove Bug
        
        // set master = null from all details after fetching and before passing it to 
        // jaxb otherwise it will cause a cycle and stackoverflow problem
        
        // annotate master field in detail as @XMLTransient to exclude from 
        // xml/json binding
        
//        // register JacksonFeature so that glassfish will use jackson instead of moxy.
//        // add jersey-media-json-jackson-2.5.2.jar in application
        //add foolowing in glassfish/modules/
        //----  jackson-core-2.5.4.jar
        //----  jackson-annotations-2.5.4.jar
        //----  jackson-databind-2.5.4.jar
        //----  jackson-jaxrs-base-2.5.4.jar
        //----  jackson-jaxrs-json-provider-2.5.4.jar
        //----  jackson-module-jaxb-annotations-2.5.4.jar
//        try{
//            resources.add(org.glassfish.jersey.jackson.JacksonFeature.class);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
        
        
        return resources;
    }


    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(gurmeet.auth.SecurityCheck.class);
        resources.add(gurmeet.rest.MasterRest.class);
        resources.add(gurmeet.rest.UserRest.class);
    }
    
}
