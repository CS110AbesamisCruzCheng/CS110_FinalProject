import java.io.*;

public class Values{
    long numRecords = 0;
    RandomAccessFile raf;
    
    //Instantiates the values class and a random access file containing
    //all the inserted strings
    public Values(String s) throws IOException{   
        raf = new RandomAccessFile(s, "rwd");
        raf.seek(0);
        raf.writeLong(numRecords);
    }   
    
    //Inserts a string and its length to the appropirate record number
    public void insert(String s) throws IOException{
        numRecords++;
        raf.seek(8+(numRecords*256));
        byte[] byteArray = s.getBytes("UTF8");
        raf.writeShort(byteArray.length);
        raf.write(byteArray);
        raf.seek(0);
        raf.writeLong(numRecords);
    }
    
    //Updates the string and its length of a given record number
    public void update(String s, long n) throws IOException{
        raf.seek(8+((n+1)*256));
        byte[] byteArray = s.getBytes("UTF8");
        raf.writeShort(byteArray.length);
        raf.write(byteArray);
    }
    
    //Returns the string of a given record number
    public String select(long n) throws IOException{
        System.out.println("nnn -->  " + n +"  \n jsgj ");
        raf.seek(0);
        System.out.println("  " + raf.readLong());
        int x = 0;
        for(int i = 1; i <= 4; i++){
            raf.seek(8+(x*256));
            int len = raf.readShort();
            byte[] byteArray = new byte[len];
            raf.read(byteArray);
            System.out.print("  " + len);
            System.out.print("  " + new String(byteArray, "UTF8"));
            x++;
        }
        raf.seek(8+((n+1)*256));
        int len = raf.readShort();
        byte[] byteArray = new byte[len];
        raf.read(byteArray);
        System.out.println(new String(byteArray, "UTF8"));
        return new String(byteArray, "UTF8");
    }
}
