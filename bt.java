import java.io.*;

public class bt {
    static int order = 7;
    static BNode root = new BNode(order, null);
    static int splitCounter = 0;  //data.bt
    static int insertCounter = -1; //data.values
    static int keyPointer;     
    static int childPointer;
    static RandomAccessFile raf;
    
    public bt(String s) throws IOException{
        raf = new RandomAccessFile(s, "rwd");
        raf.seek(0);
        raf.writeLong(1);
        raf.seek(8);
        raf.writeLong(0);
    }

    public static void treeInsert(int key) throws IOException{
        if(search(root, key) == null){
            recursiveinsert(root, key);
            if(root.count == order){

                BNode s = new BNode(order, null);
                root = s;
                s.leaf = false;
                s.count = 0;
                s.child[0] = root;

                
                System.out.println("SPLIT CALLED IN TREE INSERT");
                System.out.println("root.count = " + root.count);
            }
            System.out.println("LMAOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            raf.seek(0);
            raf.writeLong(2*splitCounter+1);
            setRecordNos(root, 0);
            print(root, 0);
        }else{
             System.out.println("ERROR: " + key + " already exists");
        }
    }
    
    public static void recursiveinsert(BNode a, int key){
        int i = a.count; //previously a.count //MAYBE A.COUNT - 1 (VERIFY)
      
        insertCounter++;
        if(a.leaf){
            //locate where you insert the incoming element
            
            while(i >= 1 && key < a.key[i - 1]){ //previously while(i >= 1)
                a.key[i] = a.key[i - 1];
                a.offSet[i] = a.offSet[i - 1];
                i--;
            }
            System.out.println("incoming element belongs at i = " + (i + 1));
            a.key[i] = key;
            System.out.println(i);
            System.out.println("a.offSet[i] contains: " + a.offSet[i]);
            a.offSet[i] = insertCounter;
            a.count++;
        }else{
            int j = 0;
            System.out.println("a.count = " + a.count);
//                if(key < a.key[j])
//                    shiftKeysRight(a);
            while(j < a.count && key > a.key[j]){
                j++;
            }
            recursiveinsert(a.child[j], key); //POSSIBLE CORRECTION: ADD J++ SOMEWHERE
            if(a.child[j].count == order){
//                System.out.println("NODE IS FULL. MUST SPLIT");
//                System.out.println("Num of keys of child: " + a.child[j].count);

                System.out.println("SPLIT CALLED IN RECURSIVEINSERT METHOD");
                System.out.println("split at j = " + j + " (after recursiveinsert text)");
                split(a, j, a.child[j]);
            }
        }
    }
    
    public static void split(BNode l, int i, BNode r)
    {
        //YOU SPLIT WHEN NODE OVERFLOWS
        //MEANING NODE.COUNT = ORDER
        
        //l is the root node, r is the node where the incoming element is
        //supposed to be inserted into 
        //m at the end, is supposed to contain the right half
        //of BNode r (node to be splitted)
        if(order % 2 != 0)
        {
            BNode m = new BNode(order, null);
            //m leaf = r leaf; they are both children of the l node
            m.leaf = r.leaf;

            //side of m node since it contains the right half of r
            m.count = order / 2;

            System.out.println("=======BEFORE OPERATIONS=======");
            
            System.out.print("Keys of node l: ");
            for(int j = 0; j < l.count; j++)
            {
                System.out.print(l.key[j] + " ");
            }
            System.out.println();
            
            System.out.print("Children of node l: ");
            for(int j = 0; j < l.child.length; j++)
            {
                if(l.child[j] != null)
                System.out.print(l.child[j].key[0] + " ");
                else
                    System.out.print("NULL ");
            }
            System.out.println();
            
            System.out.print("Keys of node r: ");
            for(int j = 0; j < r.count; j++)
            {
                System.out.print(r.key[j] + " ");
            }
            System.out.println();
            
            System.out.print("Children of node r: ");
            for(int j = 0; j < r.child.length; j++)
            {
                 if(r.child[j] != null)
                System.out.print(r.child[j].key[0] + " ");
                else
                    System.out.print("NULL ");
            }
            System.out.println();
            //move right half of r to m (keys)
            for(int j = 0; j < order / 2; j++)
            {
                m.key[j] = r.key[j + (order / 2) + 1];
                m.offSet[j] = r.offSet[j + (order / 2) + 1];
            }
            //move right half of r to m (children)
            if(!r.leaf)
            {
                for(int j = 0; j <= order / 2; j++)
                {
                    m.child[j] = r.child[j + (order / 2)]; //check
                }
            }

            //updated size of r, without its right half values
            r.count = order / 2;

            //MOVE THE BITCHES OVER HERE
            
            for(int j = l.count; j > i; j--)
            {
//                System.out.println("MOVING BRO: " + i);
                System.out.println("j is at: " + j);
                System.out.println("key at j: " + l.key[j]);
                System.out.println("key at j - 1: " + l.key[j - 1]);
                l.key[j] = l.key[j - 1];
                l.offSet[j] = l.offSet[j - 1];
                System.out.println("======after swap=====");
                                System.out.println("key at j: " + l.key[j]);
                System.out.println("key at j - 1: " + l.key[j - 1]);
                
            }
            
            for(int j = l.count; j > i; j--)
            {
//                System.out.println("MOVING CHILD LOOPS EXECUTES AT i = " + i);
//                System.out.println("l.count = " + l.count);
//                System.out.println("first key of child at j + 1: " + l.child[j + 1].key[0]);
//                System.out.println("first key of child at j: " + l.child[j].key[0]);
                l.child[j + 1] = l.child[j];
//                System.out.println("======after swap=====");
//                System.out.println("first key of child at j + 1: " + l.child[j + 1].key[0]);
//                System.out.println("first key of child at j: " + l.child[j].key[0]);
            }
            
            //promote median value to l node
            l.key[i] = r.key[order / 2];

            //assign new node as child of l node
//            System.out.println("====before child reassignment======");
//            System.out.println(l.child[i + 1].key[0]);
            l.child[i + 1] = m;
            System.out.println("====after child reassignment======");
            System.out.println(l.child[i + 1].key[0]);

            for(int j = 0; j < order / 2; j++)
            {
                r.key[j + (order) / 2] = 0;
                r.offSet[j + (order / 2)] = 0;
            }
            
            //CONFIRM THIS;;
            if(!r.leaf)
            {
                for(int j = 0; j <= order / 2; j++)
                {
                    r.child[j + (order / 2)] = null;
                }
            }
            l.count++;


            System.out.println("=======AFTER OPERATIONS=======");
            System.out.print("Keys of node l: ");
            for(int j = 0; j < l.count; j++)
            {
                System.out.print(l.key[j] + " ");
            }
            System.out.println();
            
            System.out.print("Children of node l: ");
            for(int j = 0; j < l.child.length; j++)
            {
                if(l.child[j] != null)
                System.out.print(l.child[j].key[0] + " ");
                else
                    System.out.print("NULL ");
            }
            System.out.println();
            
            
            System.out.print("Keys of node r: ");
            for(int j = 0; j < r.count; j++)
            {
                System.out.print(r.key[j] + " ");
            }
            System.out.println();
            
            System.out.print("Children of node r: ");
            for(int j = 0; j < r.child.length; j++)
            {
                 if(r.child[j] != null)
                System.out.print(r.child[j].key[0] + " ");
                else
                    System.out.print("NULL ");
            }
            System.out.println();
            
  
            System.out.print("Keys of node m: ");
            for(int j = 0; j < m.count; j++)
            {
                System.out.print(m.key[j] + " ");
            }
            System.out.println();
            
            System.out.print("Children of node m: ");
            for(int j = 0; j < m.child.length; j++)
            {
                 if(m.child[j] != null)
                System.out.print(m.child[j].key[0] + " ");
                else
                    System.out.print("NULL ");
            }
            System.out.println();
        }
//        else
//        {
//            BNode m = new BNode(order, null);
//            m.leaf = r.leaf;
//            m.count = (order / 2);
//            
//            System.out.println("=======BEFORE OPERATIONS=======");
//            System.out.print("Keys of node l: ");
//            for(int j = 0; j < l.count; j++)
//            {
//                System.out.print(l.key[j] + " ");
//            }
//            System.out.println();
//            System.out.print("Keys of node r: ");
//            for(int j = 0; j < r.count; j++)
//            {
//                System.out.print(r.key[j] + " ");
//            }
//            System.out.println();
//            
//            //shift them keys
//            for(int j = 0; j < order / 2; j++)
//            {
//                m.key[j] = r.key[(order / 2) + j];
//            }
//            //shift them kids
//            if(!r.leaf)
//            {
//                for(int j = 0; j < (order / 2); j++)
//                {
//                    m.child[j] = r.child[(order / 2) + j]; //check
//                }
//            }
//            //updated size of r, with one less node than m
//            r.count = (order / 2) - 1;
//            //promote median - 1 value to l node
//            l.key[i] = r.key[(order / 2) - 1];
//            //assign child appropriately
//            l.child[i + 1] = m;
//            for(int j = 0; j < order / 2; j++)
//            {
//                r.key[(order / 2) + j] = 0;
//            }
//            l.count++;
//            
//            
//            System.out.println("=======AFTER OPERATIONS=======");
//            System.out.print("Keys of node l: ");
//            for(int j = 0; j < l.count; j++)
//            {
//                System.out.print(l.key[j] + " ");
//            }
//            System.out.println();
//            System.out.print("Keys of node r: ");
//            for(int j = 0; j < r.count; j++)
//            {
//                System.out.print(r.key[j] + " ");
//            }
//            System.out.println();
//            System.out.print("Keys of node m: ");
//            for(int j = 0; j < m.count; j++)
//            {
//                System.out.print(m.key[j] + " ");
//            }
//            System.out.println();
//        }
    }
    
    public static void setRecordNos(BNode n, long q){
        n.setRecordNo(q); 
        if(!n.leaf){
            for(int j = 0; j <= n.count; j++){
                if(n.getChild(j) != null){
                    setRecordNos(n.getChild(j), q++);
                }
            }
        }
    }
    
    public static void print(BNode n, int q) throws IOException{ //prints the tree in preorder traversal
        raf.seek(q*176+16);
        if(n.parent == null){
            raf.writeLong(-1);
        }else{
            raf.writeLong((n.parent).recordNo);
        }
        for(int j = 0; j < n.count; j++){
            if(n.getChild(j) != null){
                raf.writeLong((n.getChild(j)).recordNo);
                raf.writeLong(n.getKey(j));
                raf.writeLong(n.getOffSet(j));
            }else{
                raf.writeLong(-1);
                raf.writeLong(n.getKey(j));
                raf.writeLong(n.getOffSet(j));
            }
        }
        if(n.getChild(4) == null){
            raf.writeLong(-1);
        }else{
            raf.writeLong((n.getChild(4)).recordNo);
        }
        
        if(!n.leaf){
            for(int j = 0; j <= n.count; j++){
                if(n.getChild(j) != null){
                    print(n.getChild(j), q++);
                }
            }
        }
    }
   
    
    public static int getRecNo(int key) throws IOException{
        int ans = -1;
        BNode temp = new BNode(order, null);
        temp = search(root, key);
        if(temp == null){
            System.out.println("ERROR: " + key + " does not exist.");
        }else{
            for(int i = 0; i < 176*(temp.recordNo+1)+16; i += 24){
                raf.seek(176*(temp.recordNo)+16+i);
                if(raf.readLong() == key){
                    raf.seek(176*(temp.recordNo)+16+i+8);
                    ans = (int)raf.readLong();
                }
            }
        }
        return ans;
    }
    
    public static BNode search(BNode root, int key){
        int i = 0;
        while(i < root.count && key > root.key[i]){
            i++;
        }
        if(i <= root.count && key == root.key[i]){
            return root;
        }
        if(root.leaf){
            return null;
        }else{
            return search(root.getChild(i), key);     
        }
    }
    
    public static class BNode{
        int m;          //order of tree
        int count;      //number of keys in node
        BNode child[];  //array of references
        int key[];      //array of key values
        int offSet[];   //array of offSet
        boolean leaf;   //leaf determinant
        BNode parent;   //parent of current node
        long recordNo;

        public BNode(int m, BNode parent){
            this.m = m;
            this.parent = parent;
            count = 0;
            offSet = new int[m];
            key = new int[m];
            child = new BNode[m];
            leaf = true;
        }
        
         public void setRecordNo(long n){
            recordNo = n;
         }
        
        public int getKey(int index){
            return key[index];
        }
        
        public int getOffSet(int index){
            return offSet[index];
        }
        
        public BNode getChild(int index){
            return child[index];
        }
    }
}