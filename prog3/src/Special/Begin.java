// Begin -- Parse tree node strategy for printing the special form begin

package Special;

import Tree.Node;

public class Begin extends Special {
 
    public void print(Node t, int n, boolean p) {
       t.print(n,p);
       p=true;
       n=n+2;
    }
}