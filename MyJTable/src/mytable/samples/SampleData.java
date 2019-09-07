package mytable.samples;

/**
 * Created on Jun 30, 2012
 * @version 1.0.0
 * @author
 */
public class SampleData {
    public Boolean checked = Boolean.FALSE;
    public String prsn_id;//int
    public String name;//text
    public String someDate;//date dd/mm/yyyy hh:mi:ss
    public String doubleVal;
    public String shift;
    public int srNumber= -1;
    public SampleData(String id, String name, String date, String doubleVal, String shift){
        this.prsn_id = id;
        this.name = name;
        this.someDate = date;
        this.doubleVal = doubleVal;
        this.shift = shift;
    }
    
    public void setData(int col, Object value) {
        switch (col) {
            case 0:
                checked = (Boolean)value;
                break;
            case 1:
                prsn_id = (String) value;
                break;
            case 2:
                name = (String) value;
                break;
            case 3:
                someDate = (String) value;
                break;
            case 4:
                doubleVal = (String) value;
                break;
            case 5:
                shift = (String) value;
                break;
            default:
        }
        System.out.println(checked+", "+prsn_id+", "+name);
    }
    
    public Object getData(int col) {
        switch (col) {
            case 0:
                return checked;
            case 1:
                return prsn_id;
            case 2:
                return name;
            case 3:
                return someDate;
            case 4:
                return doubleVal;
            case 5:
                return shift;
            default:
                return "##";
        }
    }
    
    
    
    public static SampleData[] createDummyData(){
        int n = 50;
        SampleData[] data = new SampleData[20*n];
        for(int i=0;i<n;i++){
            SampleData[] dummyData = new SampleData[]{
                new SampleData("1234510001", "Ram Kumar", "24/01/2012 12:39:00", "22.00000123456789", "R1"),
                new SampleData("1234510002", "Puja Khandelwal", "22/05/2012 12:39:00", "22.00000123456788", "R1"),
                new SampleData("1234510003", "Sachin Saxena", "24/04/2011 01:39:00", "3434.5454","R2"),
                new SampleData("1234510004", "Mohit Kumar", "21/05/2012 02:39:00", "3242.4543", "G1"),
                new SampleData("1234510005", "Sohan Lal", "23/05/2012", "432535.67565", "R1"),
                new SampleData("1234510006", "Ritu Choudhary", "22/09/2011 04:00:00", "2342.43543", "R3"),
                new SampleData("1234510007", "Garima Bansal", "22/05/2012 05:39:00", "100000.90234324",  ""),
                new SampleData("1234510008", "Ajay Kumar", "24/01/2012 12:39:00", "10099.00002", "R1"),
                new SampleData("1234510009", "Rajesh Choudary", "01/01/1857 12:39:00", "12312.436544", "R1"),
                new SampleData("1234510010", "Mahesh Kumar", "01/01/1970", "4365436534.645645", "G2"),
                new SampleData("1234510011", "Rajesh Yadav", "22/05/2012 12:39:00", "65465456.6456", ""),
                new SampleData("1234510012", "Pankaj Saxena", "24/04/2011 01:39:00", "000778.788", "R1"),
                new SampleData("1234510013", "Rohit Sharma", "23/05/2012 03:39:00", "6757.6457657657657", "G3"),
                new SampleData("1234510014", "Ritu Raj", "22/09/2011 04:00:00", "56756756765.6577777", "OFF"),
                new SampleData("1234510015", "Manish Jain", "22/05/2012 05:39:00", "2345442.43543534534", "R1"),
                new SampleData("1234510016", "Ajaybir Singh", "22/05/2012 12:39:00", "654645455456.6456", ""),
                new SampleData("1234510017", "Rahul Deo", "24/04/2011 01:39:00", "0454500778.788", "R1"),
                new SampleData("1234510018", "Mohit Kumar", "23/05/2012 03:39:00", "674545457.6457657657657", "G3"),
                new SampleData("1234510019", "Vijay Kumar Singh", "22/09/2011 04:00:00", "56745454556756765.6577777", "OFF"),
                new SampleData("1234510020", "Nitin Yadav", "22/05/2012 05:39:00", "234545454542.43543534534", "R1")
            }; 
            System.arraycopy(dummyData, 0, data, i*20, 20);
        }
        for(int i=0;i<data.length;i++){
            data[i].srNumber = i+1;
        }
        return data;
    }
}