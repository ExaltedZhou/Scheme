// Cons -- Parse tree node class for representing a Cons node

package Tree;

import Special.Special;
import Special.Begin;
import Special.Cond;
import Special.Define;
import Special.If;
import Special.Lambda;
import Special.Let;
import Special.Quote;
import Special.Regular;
import Special.Set;

public class Cons extends Node {
    private Node car;
    private Node cdr;
    private Special form;

    public Cons(Node a, Node d) {
        car = a;
        cdr = d;
        parseList();
    }

    // parseList() `parses' special forms, constructs an appropriate
    // object of a subclass of Special, and stores a pointer to that
    // object in variable form. It would be possible to fully parse
    // special forms at this point. Since this causes complications
    // when using (incorrect) programs as data, it is easiest to let
    // parseList only look at the car for selecting the appropriate
    // object from the Special hierarchy and to leave the rest of
    // parsing up to the interpreter.
    void parseList() {    	
    	/*if(car.isSymbol()) {
    		if(cdr.isPair()) {  		
    			if(car.getNodeId().equals( "begin")){
    				form=new Begin();		
    			}
    			else if(car.getNodeId().equals( "cond")){
    				form=new Cond();		
    			}
    			else if(car.getNodeId().equals( "define")){
    				form=new Define();		
    			}
    			else if(car.getNodeId().equals( "if")){
    				form=new If();		
    			}
    			else if(car.getNodeId().equals( "lambda")){
    				form=new Lambda();		
    			}
    			else if(car.getNodeId().equals( "let")){
    				form=new Let();		
    			}   
    			else if(car.getNodeId().equals( "set")){
    				form=new Set();		
    			}
    		}
    	}
        else if(car.isQuote()&&car.isPair()) {
        	form= new Quote();
        }
        else {
            form = new Regular();
        }*/
    	form=new Regular();
    }          
    // to the class hierarchy as needed.
  
    public boolean isPair(){
        return true;
    }
    public Node getCar(){
        return car;
    }
    public Node getCdr(){
        return cdr;
    }
    public void setCar(Node a){
        car = a;       
    }
    public void setCdr(Node b){
        cdr = b;       
    }
   
    public void print(int n) {
        form.print(this, n, false);
    }

    public void print(int n, boolean p) {
       form.print(this, n, p);
    }
}