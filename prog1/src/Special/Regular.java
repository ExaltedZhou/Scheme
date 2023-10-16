// Regular -- Parse tree node stratagy for printing regular lists

package Special;

import Tree.Node;

public class Regular extends Special {	
	private boolean isRoot=true;
    public void print(Node t, int n, boolean p) {
    	if(t==null)return;
    	if(t.isPair()) {  		
    		if(isRoot||t.getCar().isPair()) {   	
    			System.out.print("(");
    			p=true;
    			isRoot=false;}   
    	}
    	else t.print(n,p);
    	print(t.getCar(),n, true);   
		print(t.getCdr(),n, true);    }
}