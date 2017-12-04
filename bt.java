import java.io.*;
import java.util.*;

public class bt {
    static int order = 7;
    static int nodeCounter = 1;  //data.bt
    static int insertCounter = -1; //data.values
    static RandomAccessFile raf;
    
    public bt(String s) throws IOException{
        raf = new RandomAccessFile(s, "rwd");
        raf.seek(0);
        raf.writeLong(1);
        raf.writeLong(0);
        raf.writeLong(-1);
        for(int i = 0; i < order-1; i++){
            raf.writeLong(-1);
            raf.writeLong(-1);
            raf.writeLong(-1);
        }
        raf.writeLong(-1);
    }

    public static boolean treeInsert(int key) throws IOException{
        insertCounter++;
        if(search(key, 0) == -1){
            raf.seek(8);
            long rootNo = raf.readLong(); 
            if(noKeys(rootNo)==order-1){
                nodeCounter += 2;
                raf.seek((((nodeCounter-1)*(3*order-1))*8)+16);
                raf.writeLong(-1);
                for(int i = 0; i < order-1; i++){
                    if(i==0){
                        raf.writeLong(rootNo); 
                    }else{
                        raf.writeLong(-1);
                    }
                    raf.writeLong(-1);
                    raf.writeLong(-1);
                }
                raf.writeLong(-1);
                
                long newRoot = nodeCounter-1;
                raf.writeLong(newRoot);    
            }
            raf.seek(8);
            rootNo = raf.readLong(); 
            insert(rootNo, key, insertCounter);
            raf.seek(0);
            raf.writeLong(nodeCounter);
            return true;
        }else{
            System.out.println("ERROR: " + key + " already exists");
            return false;
        }
    }
    
    public static void split(long rootNode, int j, long[] Arr) throws IOException{
        nodeCounter++;
        raf.seek(0);
        raf.seek((((raf.readLong()-1)*(3*order-1))*8)+16);
        raf.writeLong(-1);
        for(int i = 0; i < order-1; i++){
            raf.writeLong(-1);
            raf.writeLong(-1);
            raf.writeLong(-1);
        }
        raf.writeLong(-1);
        
        raf.seek(0);
        raf.writeLong(nodeCounter);
        
        raf.seek(((rootNode*(3*order-1))*8)+16);
        raf.writeLong(-1);
        for(int i = 0; i < order-1; i++){
            raf.writeLong(-1);
            raf.writeLong(-1);
            raf.writeLong(-1);
        }
        raf.writeLong(-1);
                
        raf.seek(0);
        long numRecs = raf.readLong();
        if(order % 2 != 0){
            int index = 0;
            raf.seek(((rootNode*(3*order-1))*8)+16);
            raf.writeLong(Arr[index]);
            index++;
            for(int i = 0; i < (order-1)/2; i++){
                raf.writeLong(Arr[index]);
                index++;
                raf.writeLong(Arr[index]);
                index++;
                raf.writeLong(Arr[index]);
                index++;
            }
            raf.writeLong(Arr[index]);
            index++;
            
            long k = Arr[index];
            index++;
            long os = Arr[index];
            index++;
            insert(Arr[0], k, os);
            raf.seek(((Arr[0]*(3*order-1))*8)+16+8+(j*24));
            raf.writeLong(nodeCounter-1);
            
            raf.seek((((nodeCounter-1)*(3*order-1))*8)+16);
            raf.writeLong(Arr[index]);
            index++;
            for(int i = 0; i < (order-1)/2; i++){
                raf.writeLong(Arr[index]);
                index++;
                raf.writeLong(Arr[index]);
                index++;
                raf.writeLong(Arr[index]);
                index++;
            }
            
            setParent(numRecs-1);
        }
    }
    
    public static long search(int key, long node) throws IOException{
        raf.seek(0);        
        long numRecs = raf.readLong();
        long ans = -1;
        if(noKeys(node)>0){
            for(int i = 0; i<order-1; i++){
                raf.seek(((node*(3*order-1))*8)+16+16+(i*24));
                long k = raf.readLong();
                if(k==key){
                    ans = node;
                }else if(k>key){
                    //go to left child of k
                    raf.seek(((node*(3*order-1))*8)+16+8+(i*24));
                    long n = raf.readLong();
                    if(n!=-1){
                        ans = search(key, n); 
                    }else{
                        ans = -1;
                    }
                }else{
                    raf.seek(((node*(3*order-1))*8)+16+8+((order-1)*24));
                    long n = raf.readLong();
                    if(n!=-1){
                        ans = search(key, n); 
                    }else{
                        ans = -1;
                    }
                }
            }
        }
        System.out.println(key + "    " +ans);
        return ans;
    }
    
     public static long select(int key, long node) throws IOException{
        raf.seek(0);        
        long numRecs = raf.readLong();
        long ans = -1;
        if(noKeys(node)>0){
            for(int i = 0; i<order-1; i++){
                raf.seek(((node*(3*order-1))*8)+16+16+(i*24));
                long k = raf.readLong();
                if(k==key){
                    ans = raf.readLong();
                }else if(k>key){
                    //go to left child of k
                    raf.seek(((node*(3*order-1))*8)+16+8+(i*24));
                    long n = raf.readLong();
                    if(n!=-1){
                        ans = search(key, n); 
                    }else{
                        ans = -1;
                    }
                }else{
                    raf.seek(((node*(3*order-1))*8)+16+8+((order-1)*24));
                    long n = raf.readLong();
                    if(n!=-1){
                        ans = search(key, n); 
                    }else{
                        ans = -1;
                    }
                }
            }
        }
        System.out.println(key + "    " +ans);
        return ans;
    }
    
    public static void insert(long node, long key, long offSet) throws IOException
    {
        long[] arr = new long[3*order+1];
        raf.seek(((node*(3*order-1))*8)+16);        
        for(int j = 0; j < 3*order-1; j++){
            arr[j] = raf.readLong();
        }
        
        int i = noKeys(node);
        System.out.println();
        //raf.seek((+2+(3*(i-1)));
        
        boolean leaf = true;
        for(int q = 0; q<order; q++){
            raf.seek(((node*(3*order-1))*8)+16+8+(q*24));
            if(raf.readLong()!=-1){
                leaf = false;
            }
        }
        if(leaf){
            if(i!=0){
                for(int j = i-1; j >= 0; j--){
                    if(arr[2 + 3 * j]>key){
                        arr[2 + 3 * j + 3] = arr[2 + 3 * j];
                        arr[2 + 3 * j + 4] = arr[2 + 3 * j + 1];
                        arr[2 + 3 * j + 5] = arr[2 + 3 * j + 2];
                    }else{
                        arr[2 + 3 * j + 3] = key;
                        arr[2 + 3 * j + 4] = offSet;
                        arr[2 + 3 * j + 5] = -1;
                        break;
                    }
                    if(j == 0 && arr[2]>key){
                        arr[2] = key;
                        arr[3] = offSet;
                        arr[4] = arr[1];
                        arr[1] = -1;
                    }
                }
            }else{
                arr[2] = key; 
                arr[3] = offSet; 
                arr[4] = -1; 
            }
            raf.seek(((node*(3*order-1))*8)+16);
            for(int a = 0; a < 3*order-1; a++){
                raf.writeLong(arr[a]);
            }
        }else{
            int j = 0;
            while(j < noKeys(node) && key > arr[2 + 3 * (j - 1)])
            {
                j++;
            }
            insert(arr[1 + (3 * j)], key, offSet);
            if(noKeys(arr[1 + (3 * j)]) == order)
            {
                split(node , j ,arr);
            }
            else
            {
                raf.seek(((node*(3*order-1))*8)+16);
                for(int a = 0; a < 3*order-1; a++){
                     raf.writeLong(arr[a]);
                }
            }
        }        
    }
    
    public static int noKeys(long node) throws IOException{
        int keys = 0;
        for(int i = 0; i<order-1; i++){
            raf.seek(((node*(3*order-1))*8)+16+16+(i*24));
            if(raf.readLong()!=-1){
                keys++;
            }
        }
        return keys;
    }
    
    public static void setParent(long node) throws IOException
    {
        for(int j = 0; j < order; j++)
        {
            raf.seek(((node*(3*order-1))*8)+16+8+(j*24));
            long curr = raf.readLong();
            if(curr != - 1)
            {
                raf.seek(((curr*(3*order-1))*8)+16);
                raf.writeLong(node);
            }
        }
    }
    
    public static void print(int node) throws IOException{
        raf.seek(((node*(3*order-1))*8)+16);
        System.out.println("parent   " + raf.readLong());
        for(int i = 0; i < order-1; i++){
            System.out.println("child  " + (i+1) + ": " + raf.readLong());
            System.out.println("key  " + (i+1) + ": " + raf.readLong());
            System.out.println("offSet  " + (i+1) + ": " + raf.readLong());
        }
        System.out.println("child  " + order + ": " + raf.readLong());
    }
}