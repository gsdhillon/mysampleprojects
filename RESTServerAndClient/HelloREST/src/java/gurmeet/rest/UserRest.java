package gurmeet.rest;

import gurmeet.auth.User;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * REST Web Service
 *
 * @author Gurmeet Singh, gsdhillon@gmail.com
 */
@Path("user")
@RequestScoped
public class UserRest {
    @Inject 
    User user;
    
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response authenticateUser(
            @FormParam("role") String role,
            @FormParam("userId") String userId,
            @FormParam("password") String password) {
        try {
            user.setRole(role);
            user.setUserId(userId);
                
            
            // Return the token on the response
            if(!user.authenticate(password)){
                return Response.status(Status.UNAUTHORIZED).build();
            }
            
            String token = user.getToken();
            System.out.println("user logged in "+user);
            return Response.ok().header(HttpHeaders.AUTHORIZATION, token).build();
         } catch (Exception e) {
            return Response.status(Status.UNAUTHORIZED).build();
        }
    }
 
    
}
