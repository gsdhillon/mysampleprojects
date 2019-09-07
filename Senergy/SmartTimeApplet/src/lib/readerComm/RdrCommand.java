/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.readerComm;

/**
 *
 * @author Gaurav
 */
public class RdrCommand {

    public static final byte rdr_CMD_ChangeTime  =  (byte) 0xA1;
    public static final byte rdr_CMD_GetTime  =  (byte) 0xA2;
    public static final byte rdr_CMD_ChangeKey  =  0x6C;
    public static final byte rdr_CMD_ReadRecord  =  (byte) 0xA4;
    public static final byte rdr_CMD_AddBlockList  =  (byte) 0xA5;
    public static final byte rdr_CMD_EndBlockList  =  (byte) 0xA8;
    public static final byte rdr_CMD_UdateConfig  =  (byte) 0xAA;
    public static final byte rdr_CMD_UdateEmployeeDB  =  (byte) 0xB0;
    public static final byte rdr_CMD_UdateEmployeeEND  =  (byte) 0xB2;
    public static final byte rdr_CMD_GetEmpDB  =  (byte) 0xB4;
    public static final byte rdr_CMD_EraseEmpDB  =  (byte) 0xBA;
    public static final byte rdr_CMD_ResetCount  =  (byte) 0xAB;
    public static final byte rdr_CMD_AllowAllEmp =  (byte) 0xC2;
    public static final byte rdr_CMD_BlockAllEmp  = (byte) 0xC3;
    public static final byte rdr_CMD_AddUID  =  (byte) 0xCA;
    public static final byte rdr_CMD_DeleteUID  =  (byte) 0xCB;
    public static final byte rdr_CMD_SearchUID  =  (byte) 0xCC;
    public static final byte rdr_CMD_UpdateConfig  =  (byte) 0xAA;
    
}