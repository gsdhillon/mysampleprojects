/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SwipeCollection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javacodes.connection.AppContext;
import javacodes.connection.DBConnection;
import lib.utils.Packetizer;

/**
 *
 * @author Administrator
 */
public class SwipeCollection {

    private SwipeProcessor swipeServer;

    public SwipeCollection() {
        swipeServer = new SwipeProcessor(getPort());
    }

    public void StartServer() {
        swipeServer.start();
        System.out.println("Server Started");
    }

    public void StopServer() {
        swipeServer.stop();
    }

    private int getPort() {
        int port = 3001;
        DBConnection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = AppContext.getDBConnection();
            conn.connect();
            String query = "Select ServerPort from readersetting";
            ResultSet rs;
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();
            Packetizer p = new Packetizer();
            p.setCounter();
            if (rs.next()) {
                port = Integer.parseInt(rs.getString("ServerPort"));
            }
        } catch (Exception e) {
            e.printStackTrace();;
        } finally {
            AppContext.close(pstmt, conn);
        }
        return port;
    }
}
