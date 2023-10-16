// Ident -- Parse tree node class for representing identifiers

package Tree;

public class Ident extends Node {
    public String name;
    public boolean isQuote;
    public Ident(String n) {
        name = n;
    }
    public void print(int n) {
        for (int i = 0; i < n; i++)
            System.out.print(" ");
        System.out.println(name);
    }
    public boolean isSymbol(){
        return name != null;
    }
    public String toString() {
    	return name;
    }
    public Node eval(Environment env) {
    	if(isQuote()) {
    		isQuote=false;
    		return this;
    	}
    	else if(env.lookup(this.toString().trim())!=null) {
        	return env.lookup(this.toString().trim());
    	}
    	return this;

    }
    	
    
}