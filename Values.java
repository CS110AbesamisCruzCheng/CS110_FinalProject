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
        raf.seek(8 + numRecords*256);
        byte[] byteArray = s.getBytes("UTF8");
        raf.writeShort(byteArray.length);
        raf.write(byteArray);
        raf.seek(0);
        raf.writeLong(numRecords);
    }
    
    //Updates the string and its length of a given record number
    public void update(String s, int n) throws IOException{
        raf.seek(8 + n*256);
        byte[] byteArray = s.getBytes("UTF8");
        raf.writeShort(byteArray.length);
        raf.write(byteArray);
    }
    
    //Returns the string of a given record number
    public String select(int n) throws IOException{
        raf.seek(8 + n*256);
        int len = raf.readShort();
        byte[] byteArray = new byte[len];
        raf.read(byteArray);
        return new String(byteArray, "UTF8");
    }
}
