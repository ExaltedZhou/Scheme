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
import Special.Util;
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
    			if(car.toString().equals( "begin")){
    				form=new Begin();		
    			}
    			else if(car.toString().equals( "cond")){
    				form=new Cond();		
    			}
    			else if(car.toString().equals( "define")){
    				form=new Define();		
    			}
    			else if(car.toString().equals( "if")){
    				form=new If();		
    			}
    			else if(car.toString().equals( "lambda")){
    				form=new Lambda();		
    			}
    			else if(car.toString().equals( "let")){
    				form=new Let();		
    			}   
    			else if(car.toString().equals( "set")){
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
    public Node eval(Environment env) {
    	if(car.toString().trim().equals("define")){
			if(!cdr.getCar().isPair()) {
				env.define(cdr.getCar().toString().trim(), cdr.getCdr().getCar().eval(env));
			}
			else if(cdr.getCar().isPair()) {
				Node fu = new Cons(cdr.getCar().getCdr(),cdr.getCdr().getCar());
				env.define(cdr.getCar().getCar().toString().trim(), new Closure(fu,env));
				
			}
			return env;
		}
		else if(car.toString().trim().equals("quote")) {
			return cdr;
		}
		else if(car.toString().trim().equals("if")) {
			if(cdr.getCar().eval(env) == BooleanLit.getInstance(true)) {
				return cdr.getCdr().getCar();
			}
			else
				return cdr.getCdr().getCdr().getCar();
		}
		else if(car.toString().trim().equals("begin")) {
			while(cdr!=null) {
				cdr.getCar().eval(env);
				cdr = cdr.getCdr();
			}
			return env;
		}
		else if(car.toString().trim().equals("set!")) {
			env.assign(cdr.getCar().toString().trim(), cdr.getCdr().getCar());
			return env;
		}
		else if(car.toString().trim().equals("quote")) {
			return cdr;
		}
		else if(car.toString().trim().equals("lambda")) {
			return new Closure(cdr,env);
		}
		else if(car.toString().trim().equals("list")) {
			return cdr;
		}
		if(env.isKey(car.toString().trim())){
			Node lk = env.lookup(car.toString().trim());
			if(lk.isBuilt()) {
				if(cdr==null) return car.eval(env);
				return lk.apply(Util.mapeval(cdr, env));
			}
			else if(lk.isClosure()) {
				Environment envCall = new Environment(env);
				if(cdr==null) return car.eval(env);
				envCall.define(lk.getFun().getCar().getCar().toString().trim(), cdr.getCar().eval(env));
			//	envCall.define(car.toString().trim(), lk.apply(Util.mapeval(lk.getFun().getCdr().getCdr(), envCall)));
			//	env.define(car.toString().trim(), lk.apply(Util.mapeval(lk.getFun().getCdr().getCdr(), envCall)));
				return lk.apply(Util.mapeval(lk.getFun().getCdr().getCdr(), envCall));
			}
		}
		return Nil.getInstance();
    }
    public String toString() {
    	String s = car.toString();
    	return s+" "+cdr.toString();
    }
}