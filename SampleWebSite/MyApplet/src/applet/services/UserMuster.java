package applet.services;

import applet.reports.Attendance;
import lib.User;
/**
 * Class UserMuster
 * Created on Nov 3, 2012
 * @version 1.0.0
 * @author
 */
public class UserMuster {
    public Attendance[] attendance = null;
    public User user = new User();
    /**
     *
     */
    public void calculateAggregateValues(){
        int totalSecondsWorked = 0;
        float totalDayWorked = 0.0f;
        for(int i=0;i<attendance.length;i++){
           if(attendance[i].lateArrival){
               user.numLate++;
           }
           if(attendance[i].earlyDeparture){
               user.numEarly++;
           }
           float dayWorked = attendance[i].getDayFullOrHalf();
           if(dayWorked > 0){
               totalSecondsWorked += attendance[i].secondsWorked;
               totalDayWorked += dayWorked;
           }
           if(attendance[i].isOnLeave()){
              user.numLeave++; 
           }
           if(attendance[i].isAbscent()){
              user.numAbsent++;
           }
        }
        //to avoid devide by ZERO
        if(totalDayWorked > 0){
           user.avgSecondsWorked = (int)(totalSecondsWorked/totalDayWorked); 
        }
    }
}