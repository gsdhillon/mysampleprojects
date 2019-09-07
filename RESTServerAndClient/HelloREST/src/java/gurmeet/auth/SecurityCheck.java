package gurmeet.auth;

import java.io.IOException;
import java.lang.reflect.Method;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
@Secured
@Priority(Priorities.AUTHENTICATION)
public class SecurityCheck implements ContainerRequestFilter {
    @Context
    private ResourceInfo resourceInfo;

    @Inject
    private User user;
 
    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public void filter(ContainerRequestContext requestContext) throws IOException {
        try{
            //get request info and required roles from the @Secured annotation
            Method method = resourceInfo.getResourceMethod();
            String requestInfo = requestContext.getUriInfo().getPath()+method.getName();
            Secured secured = method.getAnnotation(Secured.class);
            String[] requiredRoles = secured.roles();
            
            // Get the HTTP Authorization header from the request
            String token = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
            
            //verify token
            if(!user.verify(token, requiredRoles, requestInfo)){
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }
        } catch (Exception e) {
            e.printStackTrace();
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}
