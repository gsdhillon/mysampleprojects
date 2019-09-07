package mmi.CommPort;

import mmi.data.LogReports;
import mmi.data.MyData;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.io.File; 
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;

/**
 * @type     : Java Class
 * @name     : MyCommPort
 * @file     : MyCommPort.java
 * @created  : Jan 11, 2011 1:08:07 PM
 * @version  : 1.0.0
 */
public class MyCommPort {
    public static boolean STOPPED = false;
    public static int TIME_OUT = 1800;//30 minutes
    private final int SPEED = 48000;
    private SerialPort serialPort;
    private OutputStream out;
    private InputStream in;
    /**
     *
     */
    public MyCommPort(){
    }
    /**
     *
     * @throws Exception
     */
    public static String[] listPorts() throws Exception{
        try{
            String[] allPorts = new String[50];
            int numPorts = 0;
            Enumeration portList = CommPortIdentifier.getPortIdentifiers();
            while (portList.hasMoreElements()) {
                CommPortIdentifier cpi = (CommPortIdentifier)portList.nextElement();
                int portType = cpi.getPortType();
                if (portType == CommPortIdentifier.PORT_SERIAL ) {
                    //&& !cpi.isCurrentlyOwned()
                    //System.out.println("PortName = " + cpi.getName());
                    allPorts[numPorts++] = cpi.getName()+" - SERIAL";
                }else if(portType == CommPortIdentifier.PORT_PARALLEL){
                    allPorts[numPorts++] = cpi.getName()+" - PARALLEL";
                }else if(portType == CommPortIdentifier.PORT_RS485){
                    allPorts[numPorts++] = cpi.getName()+" - RS485";
                }else if(portType == CommPortIdentifier.PORT_RAW){
                    allPorts[numPorts++] = cpi.getName()+" - RAW";
                }else if(portType == CommPortIdentifier.PORT_I2C){
                    allPorts[numPorts++] = cpi.getName()+" - I2C";
                }else{
                    allPorts[numPorts++] = cpi.getName()+" - UNKNOWN";
                }
            }
            String[] serialPorts = new String[numPorts];
            System.arraycopy(allPorts, 0, serialPorts, 0, numPorts);
            return serialPorts;
        }catch(java.lang.UnsatisfiedLinkError er){
            throw new Exception("ERROR "+er.getMessage());
        }catch(java.lang.NoClassDefFoundError er){
            throw new Exception("ERROR "+er.getMessage());
        }
    }
    /**
     *
     * @param portName
     * @throws Exception
     */
    public void open(){
        try{
            CommPortIdentifier cpi = CommPortIdentifier.getPortIdentifier(MyData.comPort);
            if(cpi==null){
                throw new Exception("FailedToOpenPort");
            }
            if (cpi.isCurrentlyOwned()) {
                throw new Exception("PortAlreadyOccupied");
            }
            CommPort commPort = cpi.open(this.getClass().getName(), 2000);
            if (!(commPort instanceof SerialPort)) {
                throw new Exception("PortNotASerialPort");
            }
            serialPort = (SerialPort) commPort;
            serialPort.setSerialPortParams(SPEED, SerialPort.DATABITS_8,
                                    SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            out = serialPort.getOutputStream();
            in = serialPort.getInputStream();

            //MyData.showInfoMessage("Port Opened - "+COM_PORT);
            MyData.portOpened = true;
        }catch(java.lang.UnsatisfiedLinkError ex){
            MyData.portOpened = false;
            MyData.showErrorMessage("UnsatisfiedLinkError opening port "+MyData.comPort);
        }catch(java.lang.NoClassDefFoundError ex){
            MyData.portOpened = false;
            MyData.showErrorMessage("NoClassDefFoundError opening port "+MyData.comPort);
        }catch(gnu.io.NoSuchPortException ex){
            MyData.portOpened = false;
            MyData.showErrorMessage("NoSuchPortException opening port "+MyData.comPort);
        }catch(Exception e){
            MyData.portOpened = false;
            LogReports.logError(e);
            MyData.showErrorMessage("Exception opening port "+MyData.comPort,e);
        }
    }
    /**
     * it will clear input buffer
     * this method should be called before sending parameter for an operation
     * and waitForData should be called
     * @return
     * @throws Exception
     */
    private void flushInputBuffer() throws Exception{
        if(in == null){
            throw new Exception("No Connection.");
        }
        byte[] buffer = new byte[128];
        while (in.available() > 0){
            in.read(buffer);
        }
        return;
    }
    /**
     * 
     */
    public void close(){
        try{
            if(out != null){
                out.close();
            }
            if(in != null){
                in.close();
            }

            if(serialPort != null){
                serialPort.close();
            }
        }catch(Exception e){
            //LogReports.logError(e);
        }
        MyData.portOpened = false;
    }
    /**
     *
     * @param data
     * @param delay
     * @throws Exception
     */
    public void writeData(byte[] data, int delay) throws Exception{
        //clear old data if any at input buffer
        //this may not be required but in case of previous result
        //is not read properly then next result should not be effected.
        flushInputBuffer();
        for(int i=0;i<data.length;i++){
            out.write(data[i]);
            if(delay>0){
                MyData.waitForTime(delay);
            }
        }
    }

    /**
     * this is an blocking call time out is 30 minutes
     * it stops the calling thread execution until some data
     * on serial port is not available
     * @return
     * @throws Exception
     */
    public void waitForData() throws Exception{
        //first of all set stopped = false
        STOPPED = false;
        //time out set to 30 min ahead current time
        long timeOut = new Date().getTime()+TIME_OUT*1000;
        long time;
        while (in.available() <= 0){
            if(STOPPED){
                throw new Exception("Sstopped by the user.");
            }
            Thread.sleep(100);
            time = new Date().getTime();
            if(time>timeOut){
                throw new Exception("Connection TimeOut "+TIME_OUT+"Secs");
            }
        }
    }
}