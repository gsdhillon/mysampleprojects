package javacode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
/**
 * Class MySQLQuery
 * Created on Aug 31, 2013
 * @version 1.0.0
 * @author
 */
public class MySQLQuery {
    private static String SQL_PATH = "";
    public static void setSQLPath(HttpServlet servlet) {
        ServletContext context = servlet.getServletContext();
        SQL_PATH = context.getRealPath("/WEB-INF/sql");
        System.out.println(SQL_PATH);
    }
    private String queryName;
    private String originalQuery;
    private String preparedQuery;
    public MySQLQuery(String queryName) throws Exception{
        try{
            this.queryName = queryName;
            BufferedReader br = new BufferedReader(new FileReader(new File(SQL_PATH+"/"+queryName+".sql")));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = br.readLine()) != null ){
                int end = line.indexOf("--");
                if(end < 0){
                    sb.append(line.substring(0)).append("\n");
                }else if(end > 0){
                    sb.append(line.substring(0, end)).append("\n");
                }
            }
            originalQuery = sb.toString().toUpperCase();
            preparedQuery = originalQuery;
        }catch(Exception e){
            throw new Exception("Query "+queryName+" not found");
        }
    }
    public MySQLQuery(String queryName, String query) throws Exception{
        this.queryName = queryName;
        originalQuery = query;
        preparedQuery = originalQuery;
    }
    public void setNumber(String field, String value) throws Exception{
        preparedQuery = preparedQuery.replaceFirst("#"+field.toUpperCase()+"#", value);
    }
    public void setNumber(String field, int value) throws Exception{
        setNumber(field, String.valueOf(value));
    }
    public void setNumber(String field, double value) throws Exception{
        preparedQuery = preparedQuery.replaceFirst("#"+field.toUpperCase()+"#", String.valueOf(value));
    }
    public void setString(String field, String value) throws Exception{
        preparedQuery = preparedQuery.replaceFirst("#"+field.toUpperCase()+"#", "'"+value+"'");
    }
    public void setDate(String field, String value, String format) throws Exception{
        preparedQuery = preparedQuery.replaceFirst("#"+field.toUpperCase()+"#", "to_date('"+value+"' , '"+format+"')");
    }
    public String getQuery(){
        log();
        return preparedQuery;
    }
    public void reset(){
        preparedQuery = originalQuery;
    }
    @SuppressWarnings("CallToThreadDumpStack")
    public void log(){
        try{
            try (PrintWriter pw = new PrintWriter(new File(SQL_PATH+"/"+queryName+"__log.sql"))) {
                pw.print(preparedQuery);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}