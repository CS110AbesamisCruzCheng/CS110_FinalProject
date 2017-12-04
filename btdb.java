import java.util.*;
import java.io.*;

public class btdb{
    public static void main(String[] args) throws IOException{
        Scanner in = new Scanner(System.in);
        
        //Instantiates the values and keys classes
        bt tree = new bt(args[0]);
        Values val = new Values(args[1]);
        
        //Runs a given command
        String command = in.next();
        while(!command.equals("exit")){
            
            int key = in.nextInt();
            if(command.equals("insert")){               //Inserts a new key with an associated value
                tree.treeInsert(key);
                val.insert(in.nextLine());
                System.out.println(key + " inserted.");
            }else if(command.equals("update")){         //Updates the value associated with a key
                val.update(in.nextLine(), tree.getRecNo(key));
                System.out.println(key + " updated.");
            }else if(command.equals("select")){         //Print value associated with a key
                System.out.println(key + " => " + val.select(tree.getRecNo(key)));
            }else{
                System.out.println("ERROR: invalid command.");
            }
            
            command = in.next();
        }
    }
}