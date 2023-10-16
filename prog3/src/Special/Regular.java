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
    	else if(t.isNull()) {
			System.out.print(")");
    	}
    	else if(t.isSymbol()||t.isNumber()||t.isString()||t.isBoolean())
			System.out.print(t.toString().trim());
    	else 
    		return;
    	print(t.getCar(),n, p);   
    	if(t.getCdr()!=null && !t.getCdr().isNull())
			System.out.print(" ");
		print(t.getCdr(),n, p);    }
}