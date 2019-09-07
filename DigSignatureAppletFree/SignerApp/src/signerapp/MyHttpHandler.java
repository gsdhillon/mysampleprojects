package signerapp;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Gurmeet Singh, gsdhillon@gmail.com
 */
@SuppressWarnings("CallToThreadDumpStack")
public class MyHttpHandler implements HttpHandler {

    

    @Override
    
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            String response = parseReqAndSign(httpExchange);

            System.out.println("response: " + response);

            //
            httpExchange.getResponseHeaders().set("Content-Type", "text/json");//text/json application/json text/html     
            httpExchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            httpExchange.sendResponseHeaders(200, response.length());
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
            String res = "Exception on SignerApp - " + e.getMessage();
            httpExchange.sendResponseHeaders(400, res.length());
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(res.getBytes());
            }
        }
    }

    private String parseReqAndSign(HttpExchange httpExchange) {
        try {
            // System.out.println("httpExchange.getRemoteAddress().getHostString(): "+httpExchange.getRemoteAddress().getHostString());
            String clientHostName = httpExchange.getRemoteAddress().getHostName();
            if (!("127.0.0.1".equalsIgnoreCase(clientHostName)
                    || "0:0:0:0:0:0:0:1".equalsIgnoreCase(clientHostName))) {
                System.out.println("ClientHostName: " + clientHostName);
                throw new Exception("not allowed");
            }

//            String req = httpExchange.getRequestURI().getQuery();
            // read the query string from the request body
            String req;
            try (InputStream in = httpExchange.getRequestBody()) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte buf[] = new byte[4096];
                for (int n = in.read(buf); n > 0; n = in.read(buf)) {
                    out.write(buf, 0, n);
                }
                req = new String(out.toByteArray(), "UTF-8");
            }
            if (req != null && req.trim().length() > 0) {
                System.out.println("From " + clientHostName + ", POST Request: " + req);
            } else {
                //body null read GET requet params
                req = httpExchange.getRequestURI().getQuery();
                //
                if (req != null && req.trim().length() > 0) {
                    System.out.println("From " + clientHostName + ", GET Request: " + req);
                } else {
                    throw new Exception("req null");
                }
            }


            //
            Map<String, String> parms = queryToMap(req);
            //  query.append("<html><body>");
            StringBuilder reqparams = new StringBuilder();
            String delim = "";
            for (String param : parms.keySet()) {
                reqparams.append(delim).append(param).append(":").append(parms.get(param));
                delim = ", ";
            }
            //  query.append("</body></html>");
            System.out.println("Request params: " + reqparams);
            //
            String empno = parms.get("EMP_NO");
            String certsno = parms.get("CERT_SNO");
            String dataBase64 = parms.get("DATA");
            //
            String data = new String(Base64.base64Decode(dataBase64), "UTF-8");
            //
            return sign(empno, certsno, data);
        } catch (Exception e) {
            e.printStackTrace();
            return "{ "
                    + "\"status\": \"FAILED\", "
                    + "\"result\": \"Parse error: " + e.getMessage() + "\" "
                    + "}";
        }
    }

    private String sign(String empNo, String certSno, String data) {
        try {
            String signBase64 = MySigner.getSign(empNo, certSno, data);

            return "{ "
                    + "\"status\": \"SUCCESS\", "
                    + "\"result\": \"" + signBase64 + "\" "
                    + "}";
        } catch (Exception e) {
            // e.printStackTrace();
            return "{ "
                    + "\"status\": \"FAILED\", "
                    + "\"result\": \"Sign failed: " + e.getMessage() + "\" "
                    + "}";
        }
    }

    /**
     * returns the url parameters in a map
     *
     * @param query
     * @return map
     */
    private Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        for (String param : query.split(",")) {//&
            String pair[] = param.split(":");//=
            if (pair.length > 1) {
                result.put(pair[0], pair[1]);
            } else {
                result.put(pair[0], "");
            }
        }
        return result;
    }
}