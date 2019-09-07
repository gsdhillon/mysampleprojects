package server_code;

import java.io.IOException;
import javax.servlet.*;

/**
 *
 * @author ADMIN
 */
public class HitCount implements Filter {
    public static int hitCount = 0;
    private static final boolean debug = false;
    private FilterConfig filterConfig = null;
    
    public HitCount() {
    }    
    
    private void doBeforeProcessing()
            throws IOException, ServletException {
        ++hitCount;
        if (debug) {
            log("HitCount:DoBeforeProcessing"+ (hitCount));
        }
    }    
    
    private void doAfterProcessing()
            throws IOException, ServletException {
        if (debug) {
            log("HitCount:DoAfterProcessing");
        }
    }

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @SuppressWarnings("UseSpecificCatch")
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        if (debug) {
            log("HitCount:doFilter()");
        }
 
        doBeforeProcessing();
  
        try {
            chain.doFilter(request, response);
        } catch (Throwable t) {
            System.out.println("Throwable t - "+t.getMessage());
        }
        
        doAfterProcessing();

     
    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    @Override
    public void destroy() {        
    }

    /**
     * Init method for this filter
     */
    @Override
    public void init(FilterConfig filterConfig) {        
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (debug) {                
                log("HitCount: Initializing filter");
            }
        }
    }
   
    
    public void log(String msg) {
        System.out.println(msg);        
    }

}
