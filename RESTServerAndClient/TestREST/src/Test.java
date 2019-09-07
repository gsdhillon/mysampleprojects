
import entity.Detail;
import entity.Master;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import rest.MyRestClient;

/**
 *
 * @author Gurmeet Singh, gsdhillon@gmail.com
 */
public class Test {
    public static void main(String[] args) {
        try{
            //USER
            Form form = new Form();
            form.param("role", "user");
            form.param("userId", "24196");
            form.param("password", "fw0r");
 
            //LOGIN
            Client client1 = ClientBuilder.newClient();
            WebTarget target1 = client1.target("http://localhost:8080/HelloREST/rest/user/");
            Invocation.Builder builder1 = target1.path("login").request(MediaType.APPLICATION_JSON_TYPE);
            Response response1 = builder1.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
            if(response1.getStatus() != 200){
                System.out.println("Response code - "+response1.getStatus());
                return;
            }
            String token = response1.getHeaderString(HttpHeaders.AUTHORIZATION);
            if(token == null){
                System.out.println("Auth token null ");
                return;
            }
            System.out.println("token - "+token);
            //MASTER
            Master master1 = new Master("X");
            List<Detail> details = new ArrayList<>();
            details.add(new Detail(1, "X1"));
            details.add(new Detail(2, "X2"));
            details.add(new Detail(3, "X3"));
            master1.setDetailList(details);
            
            //POST
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target("http://localhost:8080/HelloREST/rest/master/");
            Invocation.Builder builder = target.request(MediaType.TEXT_PLAIN);
            Response response = builder.header(HttpHeaders.AUTHORIZATION, token).post(Entity.entity(master1, MediaType.APPLICATION_JSON));
            String result = response.readEntity(String.class);
            System.out.println("post result - "+ result);
            if("OK".equals(result.split(":")[0])){
                int id = Integer.parseInt(result.split(":")[1]);
                master1.setId(id);
            }else{
                return;
            }
//            
//            System.in.read();
           
//            //REMOVE
////            client = ClientBuilder.newClient();
//            target = client.target("http://localhost:8080/HelloREST/rest/master/"+master1.getId());
//            builder = target.request(MediaType.TEXT_PLAIN);
//            result = builder.delete(String.class);
//            System.out.println("delete result - "+ result);
            
            //PUT
//            Client client = ClientBuilder.newClient();
//            WebTarget target = client.target("http://localhost:8080/HelloREST/rest/master/");
//            Invocation.Builder builder = target.request(MediaType.TEXT_PLAIN);

//            master1.setName("Y");
//            List<Detail> details2 = new ArrayList<>();
//            details2.add(new Detail(1, "Y1"));
//            details2.add(new Detail(2, "Y2"));
//            details2.add(new Detail(3, "Y3"));
//            master1.setDetailList(details2);
//
//            response = builder.put(Entity.entity(master1, MediaType.APPLICATION_JSON));
//            result = response.readEntity(String.class);
//            System.out.println(result);
//            if("OK".equals(result.split(":")[0])){
//                int id = Integer.parseInt(result.split(":")[1]);
//                master1.setId(id);
//            }
           
            
            //FIND_ALL
//            Client client = ClientBuilder.newClient();
//            WebTarget target = client.target("http://localhost:8080/HelloREST/rest/master");
//            builder = target.request(MediaType.APPLICATION_JSON);
//            List<Master> masterList = builder.get(new GenericType<List<Master>>(){});
//            masterList.forEach((u) -> System.out.println(u));


            //FIND
//            Client client = ClientBuilder.newClient();
//            target = client.target("http://localhost:8080/HelloREST/rest/master/1");
//            builder = target.request(MediaType.APPLICATION_JSON);
//            Master master = builder.get(Master.class);
//            System.out.println(master);


        }catch(Exception e){
            e.printStackTrace();
        }
        
//        testMaster();
    }

    private static void testMaster() {
        try{
            MyRestClient mrc = new MyRestClient<Master>("master", Master.class) {
                @Override
                protected GenericType<List<Master>> getCollectionType() {
                    return new GenericType<List<Master>>(){};
                }
            };

//            //
//            List<Master> list1 = mrc.findAll();
            
//            //create
            Master master1 = new Master("Raj Singh Dhillon");

            List<Detail> details = new ArrayList<>();
            details.add(new Detail(1, 1, "Gurmeet"));
            details.add(new Detail(1, 2, "Hardeep"));
            master1.setDetailList(details);

            master1.setId(mrc.create(master1));

//            //update     
//            String name = "Raj Singh Dhillon";
//            master1.setName(name);
//            mrc.update(master1);
// 
////            //find
//              Master master2 = (Master) mrc.find(""+1);
//              System.out.println(master2);
//            
//            //list
//            List<Master> list2 = mrc.findAll();
//            list2.forEach((u) -> System.out.println(u));
//           
//            //
//            assertEquals(name, master2.getName());
//            
//            //remove
//            Master master3 = (Master) mrc.remove(""+master2.getId());
//            
//            //
//            assertEquals(master2.getName(), master3.getName());
//            
//            //
//            List<Master> list3 = mrc.findAll();
//            
//            //
//            assertEquals(list1, list3);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    


    private static void assertEquals(String name, String name0) {
        System.out.println(name +"     =    "+name0);
    }

    private static void assertEquals(List list1, List list3) {
        System.out.println(list1.size() + "    =    " + list3.size());
    }
}
