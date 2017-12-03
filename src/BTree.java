
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
        keyPointer = order / 2;
        if(order % 2 == 0)
            childPointer = order / 2;
        else
            childPointer = order / 2 + 1;
    }
    
    public BNode search(BNode root, int key)
    {
        int i = 0;
        while(i < root.count && key > root.key[i])
            i++;
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
        int i = a.count; //previously a.count //MAYBE A.COUNT - 1 VERIFY
        insertCounter++;
        if(a.leaf)
        {
            //locate where you insert the incoming element
            while(i >= 1 && key < a.key[i - 1]) //previously while(i >= 1)
            {
                a.key[i] = a.key[i - 1];
                i--;
            }
            a.key[i] = key;
            a.count++;
        }
        else
        {
            int j = 0;
            while(j < a.count && key > a.key[j])
                j++;
            
            if(a.child[j].count == order - 1) //previously order - 1
            {
                split(a, j, a.child[j]);
                if(key > a.key[j])
                {
                    j++;
                }
            }
            recursiveinsert(a.child[j], key);
        }
    }
    public void split(BNode l, int i, BNode r)
    {
        //l is the root node, r is the node where the incoming element is
        //supposed to be inserted into 
        //m at the end, is supposed to contain the right half
        //of BNode r (node to be splitted)
        if(order % 2 != 0)
        {
            BNode m = new BNode(order, null);
            splitCounter++;
            m.leaf = r.leaf;
            m.count = order / 2; //m is the RIGHT HALF; so m will contain one more key than r 
            for(int j = 0; j < r.key.length / 2; j++)
            {
                m.key[j] = r.key[(r.key.length / 2) + j];
            }
            if(!r.leaf)
            {
                for(int j = 0; j < (r.child.length / 2) + 1; j++)
                {
                    m.child[j] = r.child[r.child.length / 2 + j];
                }
            }
            r.count = (order / 2) - 1; //will have less keys than m 
            for(int j = l.count - 1; j > i; j--)
            {
                l.child[j + 1] = l.child[j]; 
            }
            l.child[i + 1] = m;
            for(int j = l.count - 1; j > i; j--) //VERIFY; MAYBE J = L.COUNT - 2;
            {
                l.key[j + 1] = l.key[j];
            }
            l.key[i] = r.key[(r.key.length / 2) - 1];
            r.key[(r.key.length / 2) - 1] = 0;
            for(int j = 0; j < (r.key.length / 2); j++)
            {
                r.key[j + (r.key.length / 2)] = 0;
            }
            l.count++;
        }
        else
        {
            BNode m = new BNode(order, null);
            splitCounter++;
            m.leaf = r.leaf;
            m.count = order / 2;
            for(int j = 0; j < r.key.length / 2; j++)
            {
                m.key[j] = r.key[j + ((r.key.length / 2) + 1)];
            }
            if(!r.leaf)
            {
                for(int j = 0; j < r.child.length / 2; j++)
                {
                    m.child[j] = r.child[j + (r.child.length / 2)];
                }
            }
            r.count = order / 2;
            for(int j = l.count - 1; j > i; j--)
            {
                l.child[j + 1] = l.child[j];
            }
            l.child[i + 1] = m;
            for(int j = l.count - 2; j >= i; j--) //MAYBE J = L.COUNT - 2;
            {
                l.key[j + 1] = l.key[j];
            }
            l.key[i] = r.key[r.key.length / 2];
            r.key[r.key.length / 2] = 0;
            for(int j = 0; j < r.key.length / 2; j++)
            {
                r.key[j + ((r.key.length / 2) + 1)] = 0;
            }
            l.count++;
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
            split(s, 0, root);
            recursiveinsert(s, key);
        }
        else
            recursiveinsert(root, key);
    }
    
    public void preorderPrint(BNode n) //prints the tree in preorder traversal
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
                    preorderPrint(n.getChild(j));
                }
            }
        }
    }
    
    public void searchNode(BTree t, int key)
    {
        BNode temp = new BNode(order, null);
        temp = search(t.root, key);
        if(temp == null)
        {
            System.out.println("Key does not exist");
        }
        else
            preorderPrint(temp);
    }
}
