package maksim.lisau.rabobankattempt2.database;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created by Maks on 07-Oct-17.
 */

public class DatabaseHandler {
    public static Context context;
    public static boolean loading=true;
    public static HashMap<String, Branch> branchHashMap=new HashMap<>();
    public static HashMap<String,Supplier> supplierHashMap=new HashMap<>();
    public static HashMap<Long, Invoice> invoiceHashMap=new HashMap<>();
    public static String sysdir=System.getProperty("user.dir");
    public static String filename="database";
    public static void storeData() throws IOException{
        //Access file
        File f=new File(sysdir+filename);
        RandomAccessFile fr=null;
        fr=new RandomAccessFile(f,"rw");
        //Storing branches
        //Conversion from HashMap to array
        Branch[] branches=new Branch[branchHashMap.values().size()];
        branchHashMap.values().toArray(branches);
        for (int i=0; i<branches.length; i++) {
            //Storing address
            fr.writeUTF(branches[i].address.getStorageAddress());
            //Storing all invoice keys
            for (int j=0; j<branches[i].transactionIDs.size(); i++) {
                fr.writeLong(branches[i].transactionIDs.get(i));
            }
        }
        //Sentinel value for branches
        fr.writeUTF("s");
        //Storing suppliers
        //Conversion from HashMap to array
        Supplier[] suppliers=new Supplier[supplierHashMap.values().size()];
        supplierHashMap.values().toArray(suppliers);
        for (int i=0; i<suppliers.length; i++) {
            //Storing address
            fr.writeUTF(suppliers[i].address.getStorageAddress());
            //Storing name
            fr.writeUTF(suppliers[i].name);
            //Storing contact number
            fr.writeUTF(suppliers[i].contact);
            //Storing all invoice keys
            for (int j = 0; j < suppliers[i].transactionIDs.size(); j++) {
                fr.writeLong(suppliers[i].transactionIDs.get(i));
            }
        }
        //Sentinel value
        fr.writeUTF("i");
        //Storing invoices
        //Indication of number of invoices
        fr.writeInt(invoiceHashMap.values().size());
        //Conversion from HashMap to array
        Invoice[] invoices=new Invoice[invoiceHashMap.values().size()];
        invoiceHashMap.values().toArray(invoices);
        //Storing all invoices
        for (int i=0; i<invoices.length; i++) {
            //Storing primary key, cash amount and date.
            fr.writeLong(invoices[i].transactionID);
            fr.writeFloat(invoices[i].cashAmount);
            fr.writeUTF(String.valueOf(invoices[i].date));
        }

    }
    public static void retreiveData() throws IOException {
        //Access file
        File f=new File(sysdir+filename);
        RandomAccessFile fr=null;
        fr=new RandomAccessFile(f,"rw");
        //Retreive all branches. "s" is used as indicator for end of branches.
        String nextString=fr.readUTF();
        while (nextString!="s") {
            Branch b=new Branch();
            //Retreiving address
            b.address=new Address(nextString);
            //Retreiving transaction IDs
            int numberOfTransactions=fr.readInt();
            b.transactionIDs=new ArrayList();
            for (int i=0; i<numberOfTransactions; i++) {
                b.transactionIDs.add(fr.readLong());
            }
            //Storing in HashMap
            branchHashMap.put(b.returnName(),b);
            nextString=fr.readUTF();
        }
        //Retreive all suppliers. "i" is used as indicator for end of suppliers.
        nextString=fr.readUTF();
        while(nextString!="i") {
            Supplier s=new Supplier();
            //Retreiving address,name and contact number.
            s.address=new Address(nextString);
            s.name=fr.readUTF();
            s.contact=fr.readUTF();
            //Retreiving invoices.
            int numberOfTransactions=fr.readInt();
            for (int i=0; i<numberOfTransactions; i++) {
                s.transactionIDs.add(fr.readLong());
            }
            nextString=fr.readUTF();
            supplierHashMap.put(s.name,s);
        }
        //Retreive all invoices.
        int numberOfInvoices=fr.readInt();
        for (int i=0; i<numberOfInvoices; i++) {
            Invoice invoice=new Invoice();
            //Retreiving primary key, cash amount and date.
            invoice.transactionID=fr.readLong();
            invoice.cashAmount=fr.readFloat();
            invoice.date= Date.valueOf(fr.readUTF());
            String s=String.valueOf(invoice.date);
            //Just making sure that storage and retreival works correctly.
            assert(Date.valueOf(s)==invoice.date);
        }
    }
    public static void addBranch(Branch b) {
        branchHashMap.put(b.returnName(), b);
    }
    public static void addSupplier(Supplier s) {
        supplierHashMap.put(s.name, s);
    }
    public static void addInvoice(Invoice i, String branchName, String supplierName) {
        try{
            //Add normally if invoice transaction key exists.
            branchHashMap.get(branchName).transactionIDs.add(i.transactionID);
            supplierHashMap.get(supplierName).transactionIDs.add(i.transactionID);
            invoiceHashMap.put(i.transactionID, i);
        } catch (Exception e) {
            //Else
            //Assign random value to ID
            i.transactionID=(long)(Math.random()*Long.MAX_VALUE);
            while (invoiceHashMap.containsKey(i.transactionID)) {
                i.transactionID=(long)(Math.random()*Long.MAX_VALUE);
            }
            //Add normally
            branchHashMap.get(branchName).transactionIDs.add(i.transactionID);
            supplierHashMap.get(supplierName).transactionIDs.add(i.transactionID);
            invoiceHashMap.put(i.transactionID, i);
        }
    }
    static class retreiveCSV extends AsyncTask<String, Void, String> {
        private Exception exception;

        @Override
        protected String doInBackground(String... urls) {
            URL url= null;
            try {
                url = new URL("https://storage.googleapis.com/rabostorage/NSBHS_Rabo_Datasheet.csv");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            BufferedReader in= null;
            try {
                in = new BufferedReader(new InputStreamReader(url.openStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            String output=null;
            try {
                String next=in.readLine();
                while (next!=null) {
                    output+= next;
                    next=in.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //gets the actual data itself.
            output=output.substring(149);
            System.out.println(output);
            String[] separated=separate(output);
            int current=0;
            System.out.println(separated.length);
            while (current<separated.length){
                Address address;
                //Branch
                String country=separated[current];
                current++;
                String state=separated[current];
                current++;
                String name=separated[current];
                current++;
                String street=separated[current];
                current++;
                address=new Address(country,state,street);
                System.out.println(address.getFormattedAddressFull());
                //Supplier
                String suppName=separated[current];
                current++;
                System.out.println("Name:"+suppName);
                String suppAddress=separated[current];
                current++;
                System.out.println("Address:"+suppAddress);
                String suppContact=separated[current];
                current++;
                System.out.println("Contact:"+suppContact);
                //Invoice
                String invDate=separated[current];
                current++;
                System.out.println("Date:"+invDate);
                String invAmount=separated[current];
                current++;

                //Add invoice to list
                Invoice inv=new Invoice();
                inv.transactionID= DatabaseHandler.invoiceHashMap.size();
                SimpleDateFormat dateFormat=new SimpleDateFormat();
                dateFormat.applyPattern("dd/MM/yyyy");
                try {
                    inv.date=dateFormat.parse(invDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                invAmount=invAmount.substring(1);
                StringTokenizer st=new StringTokenizer(invAmount);
                String test="";
                while (st.hasMoreTokens()) {
                    test+=st.nextToken(",");
                }
                invAmount=test;
                inv.cashAmount=Float.parseFloat(invAmount);
                inv.branchName=name;
                inv.suppName=suppName;
                invoiceHashMap.put(inv.transactionID,inv);
                //Add link to branch
                if (DatabaseHandler.branchHashMap.containsKey(name)) {
                    //If branch already exists
                    DatabaseHandler.branchHashMap.get(name).transactionIDs.add(inv.transactionID);
                } else {
                    //Add new branch.
                    Branch b=new Branch();
                    b.address=address;
                    b.name=name;
                    DatabaseHandler.addBranch(b);
                    DatabaseHandler.branchHashMap.get(name).transactionIDs.add(inv.transactionID);
                }
                //Add link to supplier
                if (DatabaseHandler.supplierHashMap.containsKey(suppName)) {
                    DatabaseHandler.supplierHashMap.get(suppName).transactionIDs.add(inv.transactionID);
                } else {
                    //Add new supplier if not there
                    Supplier s=new Supplier();
                    s.address=new Address(suppAddress);
                    s.contact=suppContact;
                    s.name=suppName;
                    s.tempStorage=0;
                    s.transactionIDs.add(inv.transactionID);
                    DatabaseHandler.supplierHashMap.put(suppName,s);
                }
            }
            loading=true;
            return output;
        }
    }
    public static void retreiveFromCSV() throws IOException{
        new retreiveCSV().execute("https://storage.googleapis.com/rabostorage/NSBHS_Rabo_Datasheet.csv");

    }
    public static ArrayList<String> getTopSuppliers(String branchKey) {
        Branch branch = branchHashMap.get(branchKey);
        Comparator<Supplier> comparator = new Comparator<Supplier>() {
            @Override
            public int compare(Supplier supplier1, Supplier supplier2) {
                return supplier2.tempStorage - supplier1.tempStorage;
            }
        };
        Supplier[] topSuppliers = new Supplier[supplierHashMap.size()];
        for (int i = 0; i < branch.transactionIDs.size(); i++) {
            System.out.println("Transaction: "+invoiceHashMap.get(branch.transactionIDs.get(i)));
            System.out.println("Invoice: "+invoiceHashMap.get(branch.transactionIDs.get(i)));
            System.out.println("Supplier: "+supplierHashMap.get(invoiceHashMap.get(branch.transactionIDs.get(i)).getSuppName()));
            supplierHashMap.get(invoiceHashMap.get(branch.transactionIDs.get(i)).getSuppName()).tempStorage += invoiceHashMap.get(branch.transactionIDs.get(i)).cashAmount;
        }
        String[] keys = new String[topSuppliers.length];
        supplierHashMap.keySet().toArray(keys);
        for (int i = 0; i < supplierHashMap.size(); i++) {
            topSuppliers[i] = supplierHashMap.get(keys[i]);
        }
        Arrays.sort(topSuppliers, comparator);
        ArrayList<String> supplierNames=new ArrayList();
        for (int i=0; i<topSuppliers.length; i++) {
            if (topSuppliers[i].tempStorage!=0) {
                supplierNames.add(topSuppliers[i].getName());
            }
        }
        for (int i = 0; i < branch.transactionIDs.size(); i++) {
            supplierHashMap.get(invoiceHashMap.get(branch.transactionIDs.get(i)).getSuppName()).tempStorage=0;
        }
        
        return supplierNames;
    }
    public static String[] separate(String s) {
        boolean quotation=false;
        int current=0;
        int currentColumn=0;
        ArrayList<String> strings=new ArrayList();
        while (current<s.length()) {
            String currentString="";
            //If there is a quotation, ignore the comma.
            while (((current<s.length()-1&&((s.charAt(current)!=','&&s.charAt(current)!='\n'&&s.charAt(current)!='\n')||(quotation))))&& (!"\"N".equals(s.substring(current, current + 1)) && !"\"A".equals(s.substring(current, current + 1)))) {
                currentString+=s.charAt(current);
                if (s.charAt(current)=='\"') {
                    quotation=!quotation;
                }
                if (s.charAt(current)=='\"'&&(s.charAt(current+1)=='N'||s.charAt(current+1)=='A')) {
                    break;
                }
                if (s.charAt(current+1)=='N'&&s.charAt(current+2)=='Z') {
                    break;
                } else if (s.charAt(current+1)=='A'&&s.charAt(current+2)=='U') {
                    break;
                }
                current++;
            }
            if (current<s.length()) {
                current++;
            }
            System.out.println(current+"string: "+currentString+"quotation:"+quotation);
            if (currentString.length()>0&&currentString.charAt(0)=='\"') {
                currentString=currentString.substring(1,currentString.length()-1);
            }
            System.out.println(current+"modified string: "+currentString+"quotation:"+quotation);
            strings.add(currentString);
            currentColumn++;
        }
        String[] stringArray=new String[strings.size()];
        for (int i=0; i<stringArray.length; i++) {
            stringArray[i]=strings.get(i);
        }
        System.out.println("Size: "+strings.size());
        return stringArray;

    }

}
