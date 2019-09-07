package cpnds.CommPort;

import cpnds.MyMathLib.Complex;
import cpnds.MyMathLib.FFT;
import cpnds.data.LogReports;
import cpnds.data.MyData;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.io.File; 
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;

/**
 * @type     : Java Class
 * @name     : MyCommPort
 * @file     : MyCommPort.java
 * @created  : Jan 11, 2011 1:08:07 PM
 * @version  : 1.2
 */
public class MyCommPort {
    public static boolean STOPPED = false;
    public static int TIME_OUT = 1800;//30 minutes
    private final int SPEED = 115200;
    private final int MAX_CHUNCK = 100*1024;
    private SerialPort serialPort;
    private OutputStream out;
    private InputStream in;
    private byte[] dataBuffer = new byte[MyData.DATA_BUFFER_SIZE];
    private int bytesRead;

    //ERROR FRAME
    private byte[] errorFrame = new byte[18];
    private boolean lastWasErrorFrame = false;
    private byte[] lastFrameSent = new byte[18];

    public static final int RESULT_TYPE_FLOATS = 0;
    public static final int RESULT_TYPE_FFT = 1;
    public static final int RESULT_TYPE_PCA = 2;
    /**
     *
     */
    public MyCommPort(){
        errorFrame[0] = 0x45;//E
        errorFrame[1] = 0x52;//R
        for(int i=2;i<=15;i++){
            errorFrame[i] = 0x00;
        }
        errorFrame[16] = 0x45;//E
        errorFrame[17] = 0x52;//R
    }
    /**
     *
     * @throws Exception
     */
    public static String[] listPorts() throws Exception{
        try{
            if(MyData.GENERATE_DUMMY_DATA) return null;
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
            if(MyData.GENERATE_DUMMY_DATA) return;
            CommPortIdentifier cpi = CommPortIdentifier.getPortIdentifier(MyData.comPort);
            if(cpi==null){
                throw new Exception("FailedToOpenPort");
            }
            if (cpi.isCurrentlyOwned()) {
                throw new Exception("PortAlreadyOccupied");
            }
            CommPort commPort = cpi.open(this.getClass().getName(), 2000);
            if (!(commPort instanceof SerialPort)) {
                try{
                    commPort.close();
                }catch(Exception ex){
                }
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
        if(MyData.GENERATE_DUMMY_DATA) return;
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
     * @param checkADCAcqDone reply[10] should be 1
     */
    public void testBBStatus(boolean checkADCAcqDone) throws Exception{
        if(MyData.GENERATE_DUMMY_DATA) return;
        flushInputBuffer();
        //make UART_TEST frame
        byte[] frame = new byte[18];
        frame[0] = 0x4F;//O
        frame[1] = 0x4B;//K
        frame[2] = 100;
        for(int i=3;i<=15;i++){
            frame[i] = 0x00;//padding
        }
        frame[16] = 0x4F;//O
        frame[17] = 0x4B;//K
        out.write(frame);
        //waiting time for test frame 10 sec max
        int i=0;
        do{
            Thread.sleep(200);
            if(in.available() > 0){
                break;
            }
            i++;//***
        }while(i<200);
        //i==200 means no response from the BB after 40 seconds
        if(i==10){
            throw new Exception("BB not responding. Waited for 40 sec.");
        }
        //read UART_TEST reply
        byte[] reply = new byte[18];
        in.read(reply);
        //expected reply frame
        byte[] expectedReply = new byte[18];
        expectedReply[0] = 0x4F;//O
        expectedReply[1] = 0x4B;//K
        expectedReply[2] = 0x55;//U
        expectedReply[3] = 0x41;//A
        expectedReply[4] = 0x52;//R
        expectedReply[5] = 0x54;//T
        expectedReply[6] = 0x54;//T
        expectedReply[7] = 0x45;//E
        expectedReply[8] = 0x53;//S
        expectedReply[9] = 0x54;//T
        //expected 10 is either 0 OR 1 ***
        for(i=11;i<=15;i++){
            expectedReply[i] = 0x00;//padding
        }
        expectedReply[16] = 0x4F;//O
        expectedReply[17] = 0x4B;//K
        //check the reply frame HEADER FOOTER
        for(i=0;i<10;i++){
            if(expectedReply[i]!= reply[i]){
                throw new Exception("BB communication error!");
            }
        }
        for(i=11;i<18;i++){
            if(expectedReply[i]!= reply[i]){
                throw new Exception("BB communication error!!");
            }
        }
        //check ADCAcqDone
        if(checkADCAcqDone && reply[10] != 1){
            throw new Exception("ADCAcqData at BB not done! Pl do ADCAcq first.");
        }
    }
    /**
     *
     * @throws Exception
     */
    private void writeErrorFrame() throws Exception{
        if(MyData.GENERATE_DUMMY_DATA) return;
        flushInputBuffer();
        out.write(errorFrame);
        lastWasErrorFrame = true;
    }
    /**
     *
     * @throws Exception
     */
    private void writeLastFrameAgain() throws Exception{
        if(MyData.GENERATE_DUMMY_DATA) return;
        flushInputBuffer();
        if(lastWasErrorFrame){
            out.write(errorFrame);
        }else{
            out.write(lastFrameSent);
        }
    }
    /**
     * 
     */
    public void close(){
        try{
            if(out != null){
                out.close();
            }
            out = null;
        }catch(Exception e){
            LogReports.logError(e);
        }
        try{
            if(in != null){
                flushInputBuffer();
            }
        }catch(Exception e){
            LogReports.logError(e);
        }
        try{
            if(in != null){
                in.close();
            }
            in = null;
        }catch(Exception e){
            LogReports.logError(e);
        }
        try{
            if(serialPort != null){
                serialPort.close();
            }
             serialPort = null;
        }catch(Exception e){
            LogReports.logError(e);
        }
        MyData.portOpened = false;
    }
    /**
     * try 10 times wait for 100 ms each time
     * and read as many bytes as available
     * @return
     * @throws Exception
     */
    private boolean read() throws Exception{
        int trial = 0;
        int count = 0;
        while ((count = in.available()) <= 0){
            if(++trial < 10){
                System.out.println("****waiting");
                Thread.sleep(100);
                continue;
            }else{
                return false;
            }
        }

        //read data 100K at a time
        if(count > MAX_CHUNCK){
            count = MAX_CHUNCK;
        }

       // if((bytesRead+count)>dataBuffer.length){
         //   throw new Exception("BUFFER_FULL");
        //}
        int count2 = in.read(dataBuffer, bytesRead, count);
        bytesRead += count2;
        return true;
    }
    /**
     * read read in buffer1
     * bytesRead1 keeps len of data received
     * @throws Exception
     */
    private void readDataInBuffer() throws Exception{
        bytesRead = 0;
        STOPPED = false;
        //read all available bytes in buffer
        while(read()){
            if(STOPPED){
                throw new Exception("Stopped by the user.");
            }
        }
    }
    /**
     * Code is OK - 27-05-2011 if need can be used straight away.
     * Beagle Board inserts extra 0x0D before every 0x0A
     * while sending on serial port.
     * this method removes those extra 0x0D's from the data in buffer1
     * and places the result data in buffer1 back.
     * bytesRead1 keeps length of data after removing extra 0x0D's.
     * @throws Exception
     *
    private void remove0D() throws Exception{
        //replave 0x0D 0x0A with 0x0A and place the result in buffer1 back
        //no need of another buffer
        //bytesRead changed to new length
        int destIndex = 0;
        int srcIndex=0;
        for(;srcIndex<bytesRead-1;srcIndex++){
            if(dataBuffer[srcIndex]==0x0D && dataBuffer[srcIndex+1]==0x0A){
                dataBuffer[destIndex++] = 0x0A;
                srcIndex++;//additional incr
            }else{
                dataBuffer[destIndex++] = dataBuffer[srcIndex];
            }
        }
        if(srcIndex<bytesRead){
            dataBuffer[destIndex++] = dataBuffer[srcIndex];
        }
        bytesRead = destIndex;
    }*/

    /**
     *
     * Read float array send by the BB in a packet format
     * Packet format is OK+LEN(4 bytes)+4*LEN bytes of DATA+OK
     * if any problem occurred it tries 10 more times
     * it sends ERROR_FRAME if data not in desired form
     * it resend the last frame sent if it gets ERROR from BB
     * @param file
     * @param resultType 0
     * @return
     * @throws Exception
     */
    public int readDataFrame(File file, int resultType) throws Exception{
        //if DUMMY is true no actual comm is done
        if(MyData.GENERATE_DUMMY_DATA){
            if(resultType == RESULT_TYPE_FFT){
                return generateDummyFFTData(file);
            }else if(resultType == RESULT_TYPE_FLOATS){
                return generateDummyData(file);
            }else{
                return generateDummyPCAResult(file);
            }
        }
        //following is the ERROR frame stuff
        //10 times it sends error frame/last frame sent
        int trial=0;
        int maxTrials = 11;//1 + 10 more after sending error_frame
        for(; trial<maxTrials; trial++){
            //receive as much data as available
            readDataInBuffer();

            //check whether packet has min len or not
            if(dataBuffer==null || bytesRead<8){
                //reply frame not proper
                writeErrorFrame();
            }else if(dataBuffer[0] == 0x4F &&//O
                     dataBuffer[1] == 0x4B &&//K
                     dataBuffer[bytesRead-2] == 0x4F &&
                     dataBuffer[bytesRead-1] == 0x4B){
                //if start end if OK go out of loop
                break;
            }else if(dataBuffer[0] == 0x45 &&//E
                     dataBuffer[1] == 0x52 &&//R
                     dataBuffer[bytesRead-2] == 0x45 &&
                     dataBuffer[bytesRead-1] == 0x52){
                //reply frame is error frame
                //resend the last frame sent again
                writeLastFrameAgain();
            }else{
                //reply frame not under stood neither OK nor ER
                writeErrorFrame();
            }
            //we have just sent another frame so wait for reply
            waitForData();
        }
        //trial reached max means OK frame not found
        if(trial >= maxTrials){
            throw new Exception("BB LINK ERROR. TRIALS MADE = "+maxTrials);
        }

        //in case of FFT len should be multiple of 8
        //otherwise len shoulld be multiple of 4
        if(resultType == RESULT_TYPE_FFT){
            if((bytesRead-8)%8 != 0){
                throw new Exception("LenOfPacketNot 8*X = " + (bytesRead-8));
            }
        }else{
            if((bytesRead-8)%4 != 0){
                throw new Exception("LenOfPacketNot 4*X = " + (bytesRead-8));
            }
        }

        //check 4byteInt == (bytesRead1-8)/4
        int floatsReceived = (bytesRead-8)/4;
        int floatsExpected = MyData.getInt(dataBuffer, 2);
        if(floatsReceived!=floatsExpected){
            throw new Exception("WrongLen Expected-" +floatsExpected+
                                 ", Received-"+floatsReceived );
        }
        
        //write data to the file
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(dataBuffer, 6, bytesRead-8);
        fos.close();
        
        return floatsExpected;
    }

    /**
     * writes a frame of len 18
     * @param frame
     * @throws Exception
     */
    public void writeFrame(byte[] frame) throws Exception{
        if(MyData.GENERATE_DUMMY_DATA){
            return;
        }
        //clear old data if any at input buffer
        //this may not be required but in case of previous result
        //is not read properly then next result should not be effected.
        flushInputBuffer();

        //check len
        if(frame.length != 18){
            throw new Exception("INVALID FRAME LEN "+frame.length);
        }

        //check start OK
        if(frame[0]!=0x4F || frame[1]!=0x4B){
            throw new Exception("INVALID FRAME - START NOT OK");
        }

        //check end OK
        if(frame[16] != 0x4F || frame[17] != 0x4B){
            throw new Exception("INVALID FRAME - END NOT OK");
        }
        //write all 18 bytes of frame data
        out.write(frame);

        //keep a copy of frame just sent
        //for resend if needed
        lastWasErrorFrame = false;
        System.arraycopy(frame, 0, lastFrameSent, 0, 18);

        //endLine changed from 0x0D,0x0A to 0x0A only
        //after discussion with Nidhi on 21-05-2010
        //now no need of sending even 0x0A - 27-05-2010
        //byte[] endLine = {0x0A};
        //out.write(endLine);
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
        //this is for testing purpose only
        if(MyData.GENERATE_DUMMY_DATA){
            for(int i=0;i<5;i++){
                if(STOPPED){
                    throw new Exception("Stopped by the user.");
                }
                Thread.sleep(500);
            }
            return;
        }
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
    /**
     * len no. of float no.s and save in file
     * @param f
     * @param len
     * @throws Exception
     */
    private int generateDummyData(File f) throws Exception{
        FileOutputStream fos = null;
        try{
            int len = DUMMY_POINTS;
            float[] data = new float[len];
            for (int i = 0; i < len; i++) {
                data[i] =  (float) Math.sin(10*(dummyFreq+1)*Math.PI*i/len);
                data[i] *=  (new Random().nextInt(45876))*1.9873f;
            }
            fos = new FileOutputStream(f);
            byte[] buffer = new byte[4];
            for(int i=0;i<len;i++){
                int intBits = Float.floatToIntBits(data[i]);
                buffer[0] = (byte)(intBits & 0x00FF);
                intBits = intBits >> 8;
                buffer[1] = (byte)(intBits & 0x00FF);
                intBits = intBits >> 8;
                buffer[2] = (byte)(intBits & 0x00FF);
                intBits = intBits >> 8;
                buffer[3] = (byte)(intBits & 0x00FF);
                fos.write(buffer);
            }
            dummyFreq = (dummyFreq+1)%10;
            return len;
        }finally{
            if(fos != null) fos.close();
        }
    }
    /**
     * len no. of float no.s and save in file
     * @param f
     * @param len
     * @throws Exception
     */
    private int generateDummyFFTData(File f) throws Exception{
        FileOutputStream fos = null;
        try{
            int len = DUMMY_POINTS;
            Complex[] data = new Complex[len];
            for (int i = 0; i < len; i++) {
                data[i] = new Complex((float)(Math.sin((2.0*Math.PI*i)/len)+2567.50),0);
            }
            Complex[] fft = FFT.computeFFT(data);
            fos = new FileOutputStream(f);
            byte[] buffer = new byte[4];
            //write all real parts
            for(int i=0;i<len;i++){
                int intBits = Float.floatToIntBits(fft[i].real());
                buffer[0] = (byte)(intBits & 0x00FF);
                intBits = intBits >> 8;
                buffer[1] = (byte)(intBits & 0x00FF);
                intBits = intBits >> 8;
                buffer[2] = (byte)(intBits & 0x00FF);
                intBits = intBits >> 8;
                buffer[3] = (byte)(intBits & 0x00FF);
                fos.write(buffer);
            }
            //write all imag parts
            for(int i=0;i<len;i++){
                int intBits = Float.floatToIntBits(fft[i].imag());
                buffer[0] = (byte)(intBits & 0x00FF);
                intBits = intBits >> 8;
                buffer[1] = (byte)(intBits & 0x00FF);
                intBits = intBits >> 8;
                buffer[2] = (byte)(intBits & 0x00FF);
                intBits = intBits >> 8;
                buffer[3] = (byte)(intBits & 0x00FF);
                fos.write(buffer);
            }
            dummyFreq = (dummyFreq+1)%10;
            return 2*len;
        }finally{
            if(fos != null) fos.close();
        }
    }
    /**
     * len no. of float no.s and save in file
     * @param f
     * @param len
     * @throws Exception
     */
    private int generateDummyPCAResult(File f) throws Exception{
        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(f);
            float pcaVal = 12345.6789f;
            byte[] buffer = new byte[4];
            int intBits = Float.floatToIntBits(pcaVal);
            buffer[0] = (byte)(intBits & 0x00FF);
            intBits = intBits >> 8;
            buffer[1] = (byte)(intBits & 0x00FF);
            intBits = intBits >> 8;
            buffer[2] = (byte)(intBits & 0x00FF);
            intBits = intBits >> 8;
            buffer[3] = (byte)(intBits & 0x00FF);
            fos.write(buffer);
            return 1;
        }finally{
            if(fos != null) fos.close();
        }
    }
    //To generate dummy data if BB is not connected
    private int dummyFreq = 0;
    private int DUMMY_POINTS = 32*1024;
}