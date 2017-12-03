
public class BTree {
    static int order;
    BNode root;
    long splitCounter; //data.bt
    long insertCounter; //data.values
    int keyPointer;
    int childPointer;
    
    public BTree(int order)
    {
        this.order = order;
        root = new BNode(order, null);
        splitCounter = -1;
        insertCounter = -1;
    }
    
    public BNode search(BNode root, int key)
    {
        int i = 0;
        while(i < root.count && key > root.key[i])
            i++;
//        System.out.println("element searched for located on i = " + i);
        if(i <= root.count && key == root.key[i])
        {
            return root;
        }
        if(root.leaf)
            return null;
        else
           return search(root.getChild(i), key);     
    }

    public void recursiveinsert(BNode a, int key)
    {
        int i = a.count; //previously a.count //MAYBE A.COUNT - 1 (VERIFY)
      System.out.println("First key of root: " + root.key[0]);
        //insertCounter++;
        if(a.leaf)
        {
            //locate where you insert the incoming element
            
            while(i >= 1 && key < a.key[i - 1]) //previously while(i >= 1)
            {
                a.key[i] = a.key[i - 1];
                i--;
            }
            System.out.println("incoming element belongs at i = " + (i + 1));
            a.key[i] = key;
            a.count++;
        }
        else
        {
                int j = 0;
                System.out.println("a.count = " + a.count);
//                if(key < a.key[j])
//                    shiftKeysRight(a);
                while(j < a.count && key > a.key[j])
                    j++;
                recursiveinsert(a.child[j], key); //POSSIBLE CORRECTION: ADD J++ SOMEWHERE
                if(a.child[j].count == order)
                {
    //                System.out.println("NODE IS FULL. MUST SPLIT");
    //                System.out.println("Num of keys of child: " + a.child[j].count);

                    System.out.println("SPLIT CALLED IN RECURSIVEINSERT METHOD");
                    System.out.println("split at j = " + j + " (after recursiveinsert text)");
                    split(a, j, a.child[j]);
                }
            
        }
    }
    public void split(BNode l, int i, BNode r)
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
            for(int j = 0; j < l.key.length; j++)
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
            for(int j = 0; j < r.key.length; j++)
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
            }
            //move right half of r to m (children)
            if(!r.leaf)
            {
                for(int j = 0; j <= order / 2; j++)
                {
                    System.out.println("MOVING THEM CHILDREN");
                    System.out.println("m.child[j]:  " + m.child[j]);
                    System.out.println("r.child[j + (order / 2)]:  " + r.child[j + (order / 2)]);
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

            for(int j = 0; j <= order / 2; j++)
            {
                r.key[j + (order / 2)] = 0;
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
            for(int j = 0; j < l.key.length; j++)
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
            for(int j = 0; j < r.key.length; j++)
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
            for(int j = 0; j < m.key.length; j++)
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
        else
        {
            BNode m = new BNode(order, null);
            m.leaf = r.leaf;
            m.count = order / 2;
            
                        System.out.println("=======BEFORE OPERATIONS=======");
            
            System.out.print("Keys of node l: ");
            for(int j = 0; j < l.key.length; j++)
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
            for(int j = 0; j < r.key.length; j++)
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
            
            for(int j = 0; j < order / 2; j++)
            {
                m.key[j] = r.key[j + (order / 2)];
            }
            
            if(!r.leaf)
            {
                for(int j = 0; j <= order / 2; j++)
                {
                    m.key[j] = r.key[(order / 2) - 1 + j];
                }
            }
            
            
            r.count = (order / 2) - 1;
            
            l.key[i] = r.key[(order / 2) - 1];
            l.child[i + 1] = m;
            
            for(int j = 0; j <= order / 2; j++)
            {
                r.key[(order / 2) - 1 + j] = 0;
            }
            l.count++;
            
                       System.out.println("=======AFTER OPERATIONS=======");
            System.out.print("Keys of node l: ");
            for(int j = 0; j < l.key.length; j++)
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
            for(int j = 0; j < r.key.length; j++)
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
            for(int j = 0; j < m.key.length; j++)
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

    }
    
                
    public void treeInsert(BTree t, int key)
    {
        BNode root = t.root;
        
        if(root.count == order - 1)
        {
            
            BNode s = new BNode(order, null);
            t.root = s;
            s.leaf = false;
            s.count = 0;
            s.child[0] = root;
            
            recursiveinsert(s, key);
            System.out.println("LMAOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            System.out.println("SPLIT CALLED IN TREE INSERT");
            System.out.println("root.count = " + root.count);
            //split(s, 0, root);
        }
        else
        recursiveinsert(root, key);
//        if(t.root.count == order)
//        {
//            
//            BNode s = new BNode(order, null);
//            t.root = s;
//            s.leaf = false;
//            s.count = 0;
//            s.child[0] = root;
//            
////            recursiveinsert(s, key);
//            System.out.println("LMAOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
//            System.out.println("SPLIT CALLED IN TREE INSERT");
//            System.out.println("root.count = " + t.root.count);
////            split(s, 0, root);
//        }
    }
    
    public void print(BNode n) //prints the tree in preorder traversal
    {
        for(int i = 0; i < n.count; i++)
        {
            System.out.print(n.getKey(i) + " ");
        }
        
        if(!n.leaf)
        {
            for(int j = 0; j <= n.count; j++)
            {
                if(n.getChild(j) != null)
                {
                    System.out.println();
                    print(n.getChild(j));
                }
            }
        }
    }
    
    public void SearchPrintNode(BTree t, int key)
    {
        BNode temp = new BNode(order, null);
        temp = search(t.root, key);
        if(temp == null)
        {
            System.out.println("Key does not exist");
        }
        else
            printNode(temp);
    }
    
    public void printNode(BNode n)
    {
        for(int i = 0; i < n.count; i++)
        {
            System.out.print(n.getKey(i) + " ");
        }
    }
    
//    public BNode getParent(BNode n)
//    {
//        return n.parent;
//    }
//    
//    public int getParentRecNum(BNode n)
//    {
//        BNode temp = getParent(n);
//        return temp.recordNo;
//    }
    
}


//        if(order % 2 != 0)
//        {
//            BNode m = new BNode(order, null);
//            splitCounter++;
//            m.leaf = r.leaf;
//            m.count = order / 2; //m is the RIGHT HALF; so m will contain one more key than r 
//            for(int j = 0; j < r.key.length / 2; j++)
//            {
//                m.key[j] = r.key[(r.key.length / 2) + j];
//            }
//            if(!r.leaf)
//            {
//                for(int j = 0; j < (r.child.length / 2) + 1; j++)
//                {
//                    m.child[j] = r.child[r.child.length / 2 + j];
//                }
//            }
//            r.count = (order / 2) - 1; //will have less keys than m 
//            for(int j = l.count - 1; j > i; j--)
//            {
//                l.child[j + 1] = l.child[j]; 
//            }
//            l.child[i + 1] = m;
//            for(int j = l.count - 1; j > i; j--) //VERIFY; MAYBE J = L.COUNT - 2;
//            {
//                l.key[j + 1] = l.key[j];
//            }
//            l.key[i] = r.key[(r.key.length / 2) - 1];
//            r.key[(r.key.length / 2) - 1] = 0;
//            for(int j = 0; j < (r.key.length / 2); j++)
//            {
//                r.key[j + (r.key.length / 2)] = 0;
//            }
//            l.count++;
//        }
//        else
//        {
//            BNode m = new BNode(order, null);
//            splitCounter++;
//            m.leaf = r.leaf;
//            m.count = order / 2;*
//            for(int j = 0; j < r.key.length / 2; j++)
//            {
//                m.key[j] = r.key[j + ((r.key.length / 2) + 1)];
//            }
//            if(!r.leaf)
//            {
//                for(int j = 0; j < r.child.length / 2; j++)
//                {
//                    m.child[j] = r.child[j + (r.child.length / 2)];
//                }
//            }
//            r.count = order / 2;
//            for(int j = l.count - 1; j > i; j--)
//            {
//                l.child[j + 1] = l.child[j];
//            }
//            l.child[i + 1] = m;
//            for(int j = l.count - 2; j >= i; j--) //MAYBE J = L.COUNT - 2;
//            {
//                l.key[j + 1] = l.key[j];
//            }
//            l.key[i] = r.key[r.key.length / 2];
//            r.key[r.key.length / 2] = 0;
//            for(int j = 0; j < r.key.length / 2; j++)
//            {
//                r.key[j + ((r.key.length / 2) + 1)] = 0;
//            }
//            l.count++;
//        }