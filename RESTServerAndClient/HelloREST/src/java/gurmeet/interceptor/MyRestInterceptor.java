package gurmeet.interceptor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
 
public class MyRestInterceptor {
    @AroundInvoke
    public Object intercept(InvocationContext context) throws Exception {
        String className = context.getTarget().getClass().getName();
        className = className.substring(0, className.indexOf("Rest")+4);
        String methodName = context.getMethod().getName();
        System.out.println("calling "+className+"."+methodName+"() -->" );
        //
        Object result = context.proceed();
        //
        System.out.println("---> retured from "+className+"."+methodName+"()" );
        return result;
    }
}
