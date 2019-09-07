/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.ComboArray;

import javax.swing.JComboBox;

/**
 *
 * @author nbpatil
 */
public class ComboArray {

    public static String[] FillStates() {
        String State[] = {"AndraPradesh", "ArunachalPradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat", "Haryana", "HimachalPradesh", "JammuandKashmir", "Jharkhand", "Karnataka", "Kerala", "MadyaPradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Orissa", "Punjab", "Rajasthan", "Sikkim", "TamilNadu", "Tripura", "Uttaranchal", "UttarPradesh", "WestBenga"};
        return State;
    }

    public static String[] FillCity(String statename) {
        String arrtemp[] = null;
//     ArrayList cities = null;
        String AndraPradesh[] = {"Hyderabad", "Visakhapatnam", "Vijayawada", "Guntur,", "Warangal", "Nellore", "Kurnool", "Rajahmundry", "Kadapa", "Kakinada", "Nizamabad", "Tirupati", "Anantapur", "Karimnagar", "Ramagundam", "Vizianagaram"};
        String ArunachalPradesh[] = {"Itanagar","Naharlagun", "Namsai", "Pasighat","Roing","Seppa"};
        String Assam[] = {"Guwahati"};
        String Bihar[] = {"Patna", "Gaya", "Bhagalpur", "Muzaffarpur", "Bihar Sharif", "Darbhanga", "Purnia", "Arrah", "Begusarai", "Katihar"};
        String Chhattisgarh[] = {"Raipur", "Bhilai", "Korba", "Bilaspur", "Durg"};
        String Goa[] = {"Panaji","Margao","Mapusa","Ponda"};
        String Gujarat[] = {"Ahmedabad", "Surat", "Vadodara", "Rajkot,", "Bhavnagar", "Jamnagar", "Junagadh", "Gandhidham"};
        String Haryana[] = {"Faridabad", "Gurgaon", "Rohtak", "Hisar,", "Panipat", "Karnal", "Sonipat"};
        String HimachalPradesh[] = {"Sirmaur","Palampur","Parwanoo","Kasauli","Kinnaur","Kangra","Chamba","Solan","Shimla" ,"Mandi","Lahaul & Spiti","Kullu"};
        String JammuandKashmir[] = {"Srinagar", "Jammu"};
        String Jharkhand[] = {"Dhanbad", "Ranchi", "Jamshedpur", "Bokaro,", "Mango"};
        String Karnataka[] = {"Bangalore", "Hubballi-Dharwad", "Mysore", "Gulbarga", "Belgaum", "Mangalore", "Davanagere", "Bellary", "Bijapur", "Shivamogga", "Tumkur", "Raichur"};
        String Kerala[] = {"Trivandrum", "Kochi", "Kozhikode", "Kollam", "Thrissur"};
        String MadyaPradesh[] = {"Indore", "Bhopal", "Jabalpur", "Gwalior", "Ujjain", "Dewas", "Satna", "Sagar", "Ratlam", "Rewa"};
        String Maharashtra[] = {"Mumbai", "Pune", "Nagpur", "Thane", "Chinchwad", "Nashik", "Kalyan", "Vasai","Virar", "Aurangabad", "Navi Mumbai", "Solapur","Boisar", "Bhayandar", "Bhiwandi", "Amravati", "Nanded", "Kolhapur", "Ulhasnagar", "Kupwad", "Malegaon", "Jalgaon", "Akola", "Latur", "Dhule", "Ahmednagar", "Chandrapur", "Parbhani", "Ichalkaranji", "Jalna", "Ambernath"};
        String Manipur[] = {"Imphal"};
        String Meghalaya[] = {"Shilaang"};
        String Mizoram[] = {"Aizawl"};
        String Nagaland[] = {"Kohima"};
        String Orissa[] = {"Bhubaneswar", "Cuttack", "Brahmapur", "Rourkela"};
        String Punjab[] = {"Ludhiana", "Amritsar", "Jalandhar", "Patiala", "Bathinda"};
        String Rajasthan[] = {"Jaipur", "Jodhpur", "Kota", "Bikaner", "Ajmer", "Udaipur", "Bhilwara", "Alwar", "Bharatpur", "Sikar", "Pali", "Sri Ganganagar"};
        String Sikkim[] = {};
        String TamilNadu[] = {"Chennai", "Coimbatore", "Madurai", "Tiruchirappalli", "Salem", "Ambattur", "Tirunelveli", "Tirupur", "Avadi", "Tiruvottiyur", "Thoothukudi", "Nagercoil", "Thanjavur"};
        String Tripura[] = {"Agartala"};
        String Uttaranchal[] = {"Dehradun", "Haridwar"};
        String UttarPradesh[] = {"Lucknow", "Kanpur", "Ghaziabad", "Ghaziabad", "Meerut", "Varanasi", "Allahabad", "Bareilly", "Moradabad", "Aligarh", "Saharanpur", "Gorakhpur", "Noida", "Firozabad", "Loni", "Jhansi", "Muzaffarnagar", "Mathura", "Shahjahanpur", "Rampur", "Mau", "Farrukhabad", "Hapur", "Etawah", "Mirzapur", "Bulandshahr"};
        String WestBenga[] = {"Kolkata", "Howrah", "Durgapur", "Asansol", "Siliguri", "Maheshtala", "Rajpur Sonarpur", "South Dumdum", "Gopalpur", "Bhatpara", "Panihati", "Panihati", "Bardhaman", "Kulti", "Bally", "Barasat", "North Dumdum", "Baranagar"};
        switch (statename) {
            case "AndraPradesh":
                arrtemp = AndraPradesh;
                break;
            case "ArunachalPradesh":
                arrtemp = ArunachalPradesh;
                break;
            case "Assam":
                arrtemp = Assam;
                break;
            case "Bihar":
                arrtemp = Bihar;
                break;
            case "Chhattisgarh":
                arrtemp = Chhattisgarh;
                break;
            case "Goa":
                arrtemp = Goa;
                break;
            case "Gujarat":
                arrtemp = Gujarat;
                break;
            case "Haryana":
                arrtemp = Haryana;
                break;
            case "HimachalPradesh":
                arrtemp = HimachalPradesh;
                break;
            case "JammuandKashmir":
                arrtemp = JammuandKashmir;
                break;
            case "Jharkhand":
                arrtemp = Jharkhand;
                break;

            case "Karnataka":
                arrtemp = Karnataka;
                break;

            case "Kerala":
                arrtemp = Kerala;
                break;

            case "MadyaPradesh":
                arrtemp = MadyaPradesh;
                break;

            case "Maharashtra":
                arrtemp = Maharashtra;
                break;

            case "Manipur":
                arrtemp = Manipur;
                break;

            case "Meghalaya":
                arrtemp = Meghalaya;
                break;

            case "Mizoram":
                arrtemp = Mizoram;
                break;

            case "Nagaland":
                arrtemp = Nagaland;
                break;

            case "Orissa":
                arrtemp = Orissa;
                break;


            case "Punjab":
                arrtemp = Punjab;
                break;

            case "Rajasthan":
                arrtemp = Rajasthan;
                break;

            case "Sikkim":
                arrtemp = Sikkim;
                break;

            case "TamilNadu":
                arrtemp = TamilNadu;
                break;

            case "Tripura":
                arrtemp = Tripura;
                break;

            case "Uttaranchal":
                arrtemp = Uttaranchal;
                break;

            case "UttarPradesh":
                arrtemp = UttarPradesh;
                break;
            case "WestBenga":
                arrtemp = WestBenga;
                break;

        }
//        MyUtils.showMessage("arrtemp : " + arrtemp + " arrtemp[0]" + arrtemp[0]);
        return arrtemp;
    }

    public static void fillArrayToCombo(String array[], JComboBox mycombo) {
        mycombo.removeAllItems();
        for (String str : array) {
            mycombo.addItem(str);
        }
    }
}
