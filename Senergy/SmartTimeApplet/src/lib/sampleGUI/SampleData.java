package lib.sampleGUI;

import javax.swing.JCheckBox;

/**
 * Class SampleData
 * Created on Jun 30, 2012
 * @version 1.0.0
 * @author
 */
public class SampleData {
    //TODO MANOJ for check box in first column
    public JCheckBox checkBox = new JCheckBox();
    public String prsn_id;//int
    public String name;//text
    public String someDate;//date dd/mm/yyyy hh:mi:ss
    public String shift;
    public SampleData(String id, String name, String date, String shift){
        this.prsn_id = id;
        this.name = name;
        this.someDate = date;
        this.shift = shift;
    }
    public static SampleData[] createDummyData(){
        return dummyData;
    }
    private static SampleData[] dummyData = {
        new SampleData("1001", "Ram Kumar", "24/01/2012 12:39:00", "R1"),
        new SampleData("1003", "Manoj Yadav", "21/05/2012 12:39:00", "R1"),
        new SampleData("1009", "Pritesh", "23/05/2010 12:39:00", "R1"),
        new SampleData("1004", "Puja Khandelwal", "22/05/2012 12:39:00", "R1"),
        new SampleData("1005", "Sachin Dhiman", "24/04/2011 01:39:00", "R1"),
        new SampleData("1007", "Mohit Kumar", "21/05/2012 02:39:00", "R1"),
        new SampleData("1006", "Sohan Lal", "23/05/2012 03:39:00", "R1"),
        new SampleData("1002", "Ritu Choudhary", "22/09/2011 04:00:00", "R1"),
        new SampleData("1008", "Garima Bansal", "22/05/2012 05:39:00", "R1"),
        new SampleData("1009", "Ajay Kumar", "24/01/2012 12:39:00", "R1"),
        new SampleData("1010", "Rajesh Choudary", "21/05/2012 12:39:00", "R1"),
        new SampleData("1011", "Mahesh Kumar", "23/05/2010 12:39:00", "R1"),
        new SampleData("1012", "Rajesh Vishnoi", "22/05/2012 12:39:00", "R1"),
        new SampleData("1013", "Punkaj Saxena", "24/04/2011 01:39:00", "R1"),
        new SampleData("1014", "Sachin Sharma", "21/05/2012 02:39:00", "R1"),
        new SampleData("1015", "Rohit Sharma", "23/05/2012 03:39:00", "R1"),
        new SampleData("1016", "Ritu Raj Jain", "22/09/2011 04:00:00", "R1"),
        new SampleData("1017", "Manish Gadiya", "22/05/2012 05:39:00", "R1")
    };
}