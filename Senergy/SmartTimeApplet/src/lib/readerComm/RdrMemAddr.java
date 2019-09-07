/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.readerComm;

/**
 *
 * @author Gaurav
 */
public class RdrMemAddr {
    public static final int cMenuOffset = 0x8A;
    public static final int rdrAdd_Menu1 = 0 + cMenuOffset;
    public static final int rdrAdd_Menu2 = 12 + rdrAdd_Menu1;
    public static final int rdrAdd_AID = 8 + rdrAdd_Menu2;
    public static final int rdrAdd_ShiftTimings = rdrAdd_AID + 4;
    public static final int rdrAdd_Holidays = rdrAdd_ShiftTimings + 24;
    public static final int rdrAdd_EmployeeStartEnd = rdrAdd_Holidays + 30;
    public static final int rdrAdd_ConnTimelCDDelay = rdrAdd_EmployeeStartEnd + 8; //valid list
    public static final int rdrAdd_string = rdrAdd_ConnTimelCDDelay + 2;
    public static final int rdrAdd_IPSettings = rdrAdd_string + 16;
    public static final int rdrAdd_Building = rdrAdd_IPSettings + 24;
    public static final int rdrAdd_Melody = rdrAdd_Building + 2;
    public static final int rdrAdd_OutputStruct = rdrAdd_Melody + 76;
    public static final int rdrAdd_Pointer = rdrAdd_OutputStruct + 58;
    public static final int rdrAdd_AlarmBat = rdrAdd_Pointer + 22;         //Output Control + Alarm
    public static final int rdrAdd_HostDoorOpenClose = rdrAdd_Menu1 + 258;
    public static final int OnLineResponce = 0x80;
    public static final int rdrAdd_Command = 0;
    public static final int rdr_Records = 1;     //'Restarts Here Check For Clash With Other Values
    public static final int rdr_GetEmpDB = 2;
    public static final int rdr_ReadRecord = 3;
    public static final int rdr_EmployeeStartEnd = 4;
    public static final int rdr_lCDDelay = 5;
    public static final int rdr_InOPStatus = 6;
    public static final int rdr_GetDateTime = 7;
}
