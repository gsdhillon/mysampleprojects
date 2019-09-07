package rest;

import entity.Master;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
/**
 *
 * @author Gurmeet Singh, gsdhillon@gmail.com
 */
public abstract class MyRestClient<T> {
    private final String baseUri = "http://localhost:8080/HelloREST/rest";
    private final String uri;
    
    
    private final Class<T> type;
    
    public MyRestClient(String entity, Class<T> type){
        uri = baseUri+"/"+entity;
        this.type = type;
    }
    
    private WebTarget getTarget(String param) throws Exception{
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(uri+"/"+param+"?");
        return target;  
    }

    protected abstract GenericType<List<T>> getCollectionType();
    public List<T> findAll() throws Exception{
        return getTarget("").request(MediaType.APPLICATION_JSON).get(getCollectionType());
    }
    
    public int create(Object obj) throws Exception{
        Entity<Object> entity = Entity.entity(obj, MediaType.APPLICATION_JSON);
        String msg = getTarget("").request(MediaType.TEXT_PLAIN).post(entity).readEntity(String.class);
        String[] tokens = msg.split(":");
        if(!"OK".equals(tokens[0])){
            throw new Exception(msg);
        }
        return Integer.parseInt(tokens[1]);
    }
    
    public String update(Object obj) throws Exception{
        return getTarget("").request(MediaType.TEXT_PLAIN).put(Entity.entity(obj, MediaType.APPLICATION_JSON)).readEntity(String.class);
    }
    
    
    public Object find(String params) throws Exception{
          return getTarget(params).request(MediaType.APPLICATION_JSON).get(type);
    }

    public Object remove(String params) throws Exception{
          return getTarget(params).request(MediaType.APPLICATION_JSON).delete(type);
    }

    
}
