import SwipeCollection.SwipeCollection;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author Administrator
 */
public class MyAppListener implements ServletContextListener{
    private SwipeCollection SCollector;
    public MyAppListener(){
        
    }
    @Override
    public void contextInitialized(ServletContextEvent sce) {
//        System.out.println("Context Listener Initialized");
//        SCollector=new SwipeCollection();
//        SCollector.StartServer();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
//        SCollector.StopServer();
    }
    
}
