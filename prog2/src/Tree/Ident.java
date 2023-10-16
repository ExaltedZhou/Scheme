// Ident -- Parse tree node class for representing identifiers

package Tree;

public class Ident extends Node {
    public String name;

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
    	if(env.lookup(this.toString().trim())==null) {
    		System.out.println("no result");
    	}
    	return env.lookup(this.toString().trim());

    }
    	
    
}