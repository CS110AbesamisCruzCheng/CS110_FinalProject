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
                if(tree.treeInsert(key)){
                    val.insert(in.nextLine());
                    System.out.println(key + " inserted.");
                }else{
                    in.nextLine();
                }
            }else if(command.equals("update")){         //Updates the value associated with a key
                if(tree.search(key, 0)!=-1){
                    System.out.println(tree.search(key, 0));
                    val.update(in.nextLine(), tree.select(key));
                    System.out.println(key + " updated.");
                }else{
                    in.nextLine();
                }
            }else if(command.equals("select")){         //Print value associated with a key
                if(tree.search(key, 0)!=-1){
                    System.out.println(key + " => " + val.select(tree.select(key)));
                }
            }else if(command.equals("print")){
                tree.print(key);
            }else{
                System.out.println("ERROR: invalid command.");
            }
            command = in.next();
        }
    }
}