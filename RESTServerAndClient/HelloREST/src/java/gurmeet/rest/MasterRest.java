package gurmeet.rest;

import gurmeet.dao.MasterDao;
import gurmeet.entity.master.Master;
import gurmeet.interceptor.MyRestInterceptor;
import java.util.List;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import gurmeet.auth.Secured;
//import gurmeet.auth.User;

/**
 * REST Web Service
 *
 * @author Gurmeet Singh, gsdhillon@gmail.com
 */
@Interceptors(MyRestInterceptor.class)
@Path("master")
public class MasterRest{
    @Inject
    MasterDao masterDao;

//  if logged in user info required use following
//    @Inject
//    protected User user;

    /**
     * Creates a new instance of UserResource
     */
    public MasterRest() {
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Secured(roles = {"admin", "user"})
    public String post(Master master) {
//        System.out.println(user); //user can be passed to Dao if needed
        return masterDao.create(master);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Secured(roles = {"admin"})
    public String put(Master master) {
        return masterDao.update(master);
    }
    
    @DELETE
    @Path("{id}")
    @Produces(MediaType.TEXT_PLAIN)
    @Secured(roles = {"admin"})
    public String delete(@PathParam("id") Integer id) {
        return masterDao.remove(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured(roles = {"admin", "user"})
    public List<Master> getAll() {
        return masterDao.findAll();    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Secured(roles = {"admin", "user"})
    public Master getById(@PathParam("id") Integer id) {
        return masterDao.find(id);
    }
}