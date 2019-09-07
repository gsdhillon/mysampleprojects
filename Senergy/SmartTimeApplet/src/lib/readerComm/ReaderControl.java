/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.readerComm;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Calendar;
import lib.utils.Depacketizer;

/**
 *
 * @author Gaurav
 */
public class ReaderControl {

    private static int timeOut = 1000;
    private static InetAddress address;
    private static DatagramSocket socket;

    public static void main(String[] args) {
        try {
            //openSocket(3000);
            String ip = "abcdef";
            byte[] a = ip.getBytes();
            Calendar c = Calendar.getInstance();
            System.out.println(" " + c.get(Calendar.DATE) + " " + c.get(Calendar.MONTH) + "  " + c.get(Calendar.YEAR) + " " + c.get(Calendar.HOUR_OF_DAY) + " " + c.get(Calendar.MINUTE) + " " + c.get(Calendar.SECOND) + " " + c.get(Calendar.DAY_OF_WEEK));

//            setAddress(ip);
//            byte[] data = {(byte)1,(byte)8,(byte)13,(byte)0,(byte)0,(byte)17,(byte)21,(byte)17,(byte)150,(byte)0,(byte)0,(byte)0};
//            System.out.println("value" + data[0]);
//              Packetizer p = new Packetizer();
//              p.addInt(1);p.addInt(8);p.addInt(13);p.addInt(0);p.addInt(0);p.addInt(17);p.addInt(21);p.addInt(17);p.addInt(150);
//              p.addInt(0);p.addInt(0);p.addInt(0);
//              byte[] data2=CreateArrayMenu1(p.getPacket());
//            System.out.println("value" + data2[1]);
////              
//            writeFrame((byte) 1, RdrMemAddr.rdrAdd_Menu1_Menu2, 6, (byte) 12, data);
//            readFrame((byte) 1, RdrMemAddr.rdrAdd_Command, 10);

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
    }

    private static byte[] CreateArrayMenu1(String packet) throws Exception {
        byte[] data = new byte[12];
        Depacketizer dp = new Depacketizer(packet);
        for (int i = 0; i < 12; i++) {
            Byte j = ((Integer) dp.getInt()).byteValue();
            data[i] = j;
        }
        return data;
    }

    public static int[] readFrame(byte unitID, int intAddr, int intLen) throws Exception {

        try {
            int[] response;
            byte[] recieveBuffer = new byte[5 + 2 * intLen];

            DatagramPacket receivePacket = new DatagramPacket(recieveBuffer, recieveBuffer.length);
            byte[] ReadFrame = ModbusRTU.MakeReadFrame(unitID, intAddr, intLen);
            if (address == null) {
                throw new Exception("Reader Address not set");
            }
            DatagramPacket sendPacket = new DatagramPacket(ReadFrame, 0, ReadFrame.length, address, 3000);

            if (socket == null) {
                System.out.println("Socket in read");
                socket = new DatagramSocket(3000);
                socket.setReuseAddress(true);
            }


            boolean received = false;
            boolean validate = false;
            int i = 0, j = 0;
            System.out.println("socket " + socket);
            socket.send(sendPacket);
            while (!validate && j < 3) {
                while (!received && i < 3) {
                    try {
                        socket.setSoTimeout(timeOut);
                        socket.receive(receivePacket);
                        received = true;
                    } catch (Exception e) {
                        i++;
                        socket.send(sendPacket);
                    }
                }
                if (!received) {
                    throw new Exception("No reply from reader");
                }
                Byte validateByte = ModbusRTU.ValidateReadFrame(receivePacket, unitID, intLen);
                if (validateByte.equals((byte) 1)) {
                    validate = true;
                } else {
                    i = 0;
                    received = false;
                    socket.send(sendPacket);
                }
                j++;
            }
            if (received && validate) {
                response = ModbusRTU.ResponseReadFrame(receivePacket, intLen);
                for (int k = 0; k < 2 * intLen; k++) {
                    System.out.println("Recieved Response " + response[k]);
                }
            } else {
                throw new Exception("Invalid response from reader");
            }
            return response;
        } catch (Exception ex) {
            System.out.println(ex);
            throw ex;
        } finally {
            socket.close();
            socket = null;
        }
    }

    public static boolean writeFrameCheck(byte unitID, int intAddr, int intLen, byte byteCount, byte[] data) throws Exception {
        try {
            byte[] receiveBuffer = new byte[8];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            if (address == null) {
                throw new Exception("Reader Address not set");
            }
            if (socket == null) {
                System.out.println("Socket in write");
                socket = new DatagramSocket(3000);
                socket.setReuseAddress(true);
            }
            byte[] WriteFrame = ModbusRTU.MakeWriteFrame(unitID, intAddr, intLen, byteCount, data);
            DatagramPacket sendPacket = new DatagramPacket(WriteFrame, 0, WriteFrame.length, address, 3000);

            boolean received = false;
            boolean validate = false;
            int i = 0, j = 0;
            socket.send(sendPacket);
            while (!validate && j < 3) {
                while (!received && i < 3) {
                    try {
                        socket.setSoTimeout(timeOut);
                        socket.receive(receivePacket);
                        received = true;
                    } catch (Exception e) {
                        int[] valid = ReaderControl.readFrame(unitID, 0, 1);
                        if (valid[0] == (~data[0])) {
                            received = true;
                            receiveBuffer = ModbusRTU.MakeWriteValidation(unitID);
                            break;
                        }

                        i++;
                        socket.send(sendPacket);
                    }
                }
                if (!received) {
                    throw new Exception("No reply from reader");
                }
                Byte validateByte = ModbusRTU.ValidateWriteFrame(receivePacket.getOffset(), receiveBuffer, unitID, intLen);
                if (validateByte.equals((byte) 1)) {
                    validate = true;
                } else {

                    i = 0;
                    received = false;
                    socket.send(sendPacket);
                }
                j++;
            }

            if (received && validate) {
                return true;
            } else {

                return false;
            }
        } catch (Exception ex) {
            System.out.println(ex);
            throw ex;
        } finally {
            socket.close();
            socket = null;
        }
    }

    public static boolean writeFrame(byte unitID, int intAddr, int intLen, byte byteCount, byte[] data) throws Exception {
        try {
            byte[] receiveBuffer = new byte[8];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            if (address == null) {
                throw new Exception("Reader Address not set");
            }
            if (socket == null) {
                System.out.println("Socket in write");
                socket = new DatagramSocket(3000);
            }
            byte[] WriteFrame = ModbusRTU.MakeWriteFrame(unitID, intAddr, intLen, byteCount, data);
            for (int i = 0; i < WriteFrame.length; i++) {
                System.out.println(i + " frame byte " + WriteFrame[i]);
            }
            DatagramPacket sendPacket = new DatagramPacket(WriteFrame, 0, WriteFrame.length, address, 3000);

            boolean received = false;
            boolean validate = false;
            int i = 0, j = 0;
            socket.send(sendPacket);
            while (!validate && j < 3) {
                while (!received && i < 3) {
                    try {
                        socket.setSoTimeout(timeOut);
                        socket.receive(receivePacket);
                        received = true;
                    } catch (Exception e) {
                        i++;
                        socket.send(sendPacket);
                    }
                }
                if (!received) {
                    throw new Exception("No reply from reader");
                }
                Byte validateByte = ModbusRTU.ValidateWriteFrame(receivePacket.getOffset(), receiveBuffer, unitID, intLen);
                if (validateByte.equals((byte) 1)) {
                    validate = true;
                } else {

                    i = 0;
                    received = false;
                    socket.send(sendPacket);
                }
                j++;
            }

            if (received && validate) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            System.out.println(ex);
            throw ex;
        } finally {
            socket.close();
            socket = null;
        }
    }

    public static void setAddress(int a, int b, int c, int d) throws Exception {

        byte[] ret = new byte[4];
        ret[0] = (byte) a;
        ret[1] = (byte) b;
        ret[2] = (byte) c;
        ret[3] = (byte) d;
        address = InetAddress.getByAddress(ret);
    }

    public static void setAddress(String ip) throws Exception {
        byte[] ret = new byte[4];
        String[] a = ip.split("\\.");
        if (a.length != 4) {
            throw new Exception("Invalid IP Address");
        }
        ret[0] = (byte) Integer.parseInt(a[0]);
        ret[1] = (byte) Integer.parseInt(a[1]);
        ret[2] = (byte) Integer.parseInt(a[2]);
        ret[3] = (byte) Integer.parseInt(a[3]);
        address = InetAddress.getByAddress(ret);
    }

    public int getTimeOut() {
        return timeOut;
    }

    public static void setTimeOut(int time) {
        timeOut = time;
    }

    public static void openSocket(int port) throws SocketException {

        socket = new DatagramSocket(port);
        socket.setReuseAddress(true);
    }

    public static void closeSocket() {
        socket.close();
    }
}
