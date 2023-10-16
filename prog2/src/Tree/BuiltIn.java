// BuiltIn.java -- the data structure for function closures

// Class BuiltIn is used for representing the value of built-in functions
// such as +.  Populate the initial environment with
// (name, new BuiltIn(name)) pairs.

// The object-oriented style for implementing built-in functions would be
// to include the Java methods for implementing a Scheme built-in in the
// BuiltIn object.  This could be done by writing one subclass of class
// BuiltIn for each built-in function and implementing the method apply
// appropriately.  This requires a large number of classes, though.
// Another alternative is to program BuiltIn.apply() in a functional
// style by writing a large if-then-else chain that tests the name of
// of the function symbol.

package Tree;

import java.io.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

import Parse.Parser;

import Parse.Scanner;
import Special.Util;

public class BuiltIn extends Node {
    // TODO: For allowing the built-in functions to access the environment,
    // keep a copy of the Environment here and synchronize it with
    // class Scheme4101.

    private static Environment globalEnv = null;
    
    public static void setGlobalEnv(Environment env) {
    	env.define("+",new BuiltIn(new Ident("+")));
    	env.define("-",new BuiltIn(new Ident("-")));
    	env.define("*",new BuiltIn(new Ident("*")));
    	env.define("/",new BuiltIn(new Ident("/")));
    	env.define("<",new BuiltIn(new Ident("<")));
    	env.define("=",new BuiltIn(new Ident("=")));
    	env.define("b+",new BuiltIn(new Ident("b+")));
    	env.define("b-",new BuiltIn(new Ident("b-")));
    	env.define("b*",new BuiltIn(new Ident("b*")));
    	env.define("b/",new BuiltIn(new Ident("b/")));
    	env.define("b=",new BuiltIn(new Ident("b=")));
    	env.define("b<",new BuiltIn(new Ident("b<")));
    	env.define("symbol?",new BuiltIn(new Ident("symbol?")));
    	env.define("number?",new BuiltIn(new Ident("number?")));
    	env.define("car",new BuiltIn(new Ident("car")));
    	env.define("cdr",new BuiltIn(new Ident("cdr")));
    	env.define("cons",new BuiltIn(new Ident("cons")));
    	env.define("set-car!",new BuiltIn(new Ident("set-car!")));
    	env.define("set-cdr!",new BuiltIn(new Ident("set-cdr!")));
    	env.define("null?",new BuiltIn(new Ident("null?")));
    	env.define("pair?",new BuiltIn(new Ident("pair?")));
    	env.define("eq?",new BuiltIn(new Ident("eq?")));
    	env.define("procedure?",new BuiltIn(new Ident("procedure?")));
    	env.define("read",new BuiltIn(new Ident("read")));
    	env.define("write",new BuiltIn(new Ident("write")));
    	env.define("display",new BuiltIn(new Ident("display")));
    	env.define("newline",new BuiltIn(new Ident("newline")));
    	env.define("load",new BuiltIn(new Ident("load")));
    	env.define("eval",new BuiltIn(new Ident("eval")));
    	env.define("apply",new BuiltIn(new Ident("apply")));
    	env.define("append",new BuiltIn(new Ident("append")));
    	env.define("list",new BuiltIn(new Ident("list")));

    	globalEnv = env;
    }

    private Node symbol;

    public BuiltIn(Node s) {
        symbol = s;
    }

    public Node getSymbol() {
        return symbol;
    }

    public boolean isProcedure() {
        return true;
    }

    public void print(int n) {
        // there got to be a more efficient way to print n spaces
        for (int i = 0; i < n; i++)
            System.out.print(' ');
        System.out.print("#{Built-in Procedure ");
        if (symbol != null)
            symbol.print(-Math.abs(n) - 1);
        System.out.print('}');
        if (n >= 0)
            System.out.println();
    }

    // TODO: The method apply() should be defined in class Node
    // to report an error. It should be overwritten only in classes
    // BuiltIn and Closure.
    public Node apply(Node args) {
    	int nar = Util.length(args);
    	if (symbol.toString().trim().equals("load")) {
            if (!args.getCar().isString()) {
                System.err.println("Error: wrong type of argument");
                return Nil.getInstance();
            }
            
            String filename = args.getNodeStr();
            try {
                Scanner scanner = new Scanner(new FileInputStream(filename));
                Parser parser = new Parser(scanner);
       
                Node root = parser.parseExp();
                while (root != null) {
                    root.eval(globalEnv);
                    root = parser.parseExp();
                }
            } catch (IOException e) {
                System.err.println("Could not find file " + filename);
            }
             return Nil.getInstance();  // or Unspecific.getInstance();
        }
    	else if(symbol.toString().trim().equals("+")) {
    		int sum = 0;
    		int i = 0;
    		while(i<nar) {
    			sum+=args.getCar().getNodeInt();
    			args = args.getCdr();
    			i++;
    		}
    		return new IntLit(sum);
    	}
    	else if(symbol.toString().trim().equals("-")) {
    		int sub = args.getCar().getNodeInt();
    		args = args.getCdr();
    		int i= 1;
    		while(i<nar) {
    			sub -= args.getCar().getNodeInt();
    			args = args.getCdr();
    			i++;
    		}
    		return new IntLit(sub);
    	}
    	else if(symbol.toString().trim().equals("*")) {
    		int mul = args.getCar().getNodeInt();
    		args = args.getCdr();
    		int i= 1;
    		while(i<nar) {
    			mul *= args.getCar().getNodeInt();
    			args = args.getCdr();
    			i++;
    		}
    		return new IntLit(mul);
    	}
    	else if(symbol.toString().trim().equals("/")) {
    		int div = args.getCar().getNodeInt();
    		args = args.getCdr();
    		int i= 1;
    		while(i<nar) {
    			div /= args.getCar().getNodeInt();
    			args = args.getCdr();
    			i++;
    		}
    		return new IntLit(div);
    	}
    	else if(symbol.toString().trim().equals("<")) {
    		if(nar!=2) {
    			System.out.println("2 args required");
    			return Nil.getInstance();
    		}
    		return new BooleanLit(args.getCar().getNodeInt()>args.getCdr().getCar().getNodeInt());
    	}
    	else if(symbol.toString().trim().equals("symbol?")) {
    		if(nar!=1) {
    			System.out.println("1 args required");
    			return Nil.getInstance();
    		}
    		return new BooleanLit(args.isSymbol());
    	}
    	else if(symbol.toString().trim().equals("number?")) {
    		if(nar!=1) {
    			System.out.println("1 args required");
    			return Nil.getInstance();
    		}
    		return new BooleanLit(args.isNumber());
    	}
    	else if(symbol.toString().trim().equals("car")) {
    		if(args.getCar().getCar()==null || !args.isPair()) {
    			//System.out.println("1 pair required");
    			return Nil.getInstance();
    		}
    		return args.getCar().getCar();
    	}
    	else if(symbol.toString().trim().equals("cdr")) {
    		return args.getCdr();
    	}
    	else if(symbol.toString().trim().equals("cons")) {
    		return new Cons(args.getCar(),args.getCdr());
    	}
    	else if(symbol.toString().trim().equals("set-car!")) {
    		if(!args.getCar().isPair()) {
    			System.out.println("not cons");
    		}
    		args.getCar().setCar(args.getCdr().getCar());
    		return args.getCar();
    	}
    	else if(symbol.toString().trim().equals("null?")) {
    		return new BooleanLit(args.isNull());
    	}
    	else if(symbol.toString().trim().equals("pair?")) {
    		return new BooleanLit(args.isPair());
    	} 
    	else if(symbol.toString().trim().equals("eq?")) {
    		return new BooleanLit(args.getCar().toString()==args.getCdr().toString());
    	}
    	else if(symbol.toString().trim().equals("procedure?")) {
    		return new BooleanLit(args.isProcedure());
    	}
    	else if(symbol.toString().trim().equals("read")) {
    		String filename = args.getNodeStr();
            try {
                Scanner scanner = new Scanner(new FileInputStream(filename));
                Parser parser = new Parser(scanner);
       
                Node root = parser.parseExp();
                while (root != null) {
                    root.eval(globalEnv);
                    root = parser.parseExp();
                }
            } catch (IOException e) {
                System.err.println("Could not find file " + filename);
            }
             return Nil.getInstance();  // or Unspecific.getInstance();
    	}
    	else if(symbol.toString().trim().equals("write")) {
    		String filename = args.getCdr().getCar().toString();
    		try {
    			Writer fw = new FileWriter(filename);
				fw.write(args.getCar().toString().trim());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		return Nil.getInstance();
    	}
    	else if(symbol.toString().trim().equals("display")) {
    		System.out.println(args.toString().trim());
    		return Nil.getInstance();
    	}
    	else if(symbol.toString().trim().equals("newline")) {
    		System.out.println();
    		return Nil.getInstance();
    	}
    	else if(symbol.toString().trim().equals("b+")) {
    		if(nar!=2) {
    			System.out.println("2 args required");
    			return Nil.getInstance();
    		}
    		return new IntLit(args.getCar().getNodeInt()+args.getCdr().getCar().getNodeInt());
    	}
    	else if(symbol.toString().trim().equals("b-")) {
    		if(nar!=2) {
    			System.out.println("2 args required");
    			return Nil.getInstance();
    		}
    		return new IntLit(args.getCar().getNodeInt()-args.getCdr().getCar().getNodeInt());
    	}
    	else if(symbol.toString().trim().equals("b*")) {
        	if(nar!=2) {
        		System.out.println("2 args required");
        		return Nil.getInstance();
        	}
        	return new IntLit(args.getCar().getNodeInt()*args.getCdr().getCar().getNodeInt());
    	}
        else if(symbol.toString().trim().equals("b/")) {
        	if(nar!=2) {
        		System.out.println("2 args required");
        		return Nil.getInstance();
        	}
        	return new IntLit(args.getCar().getNodeInt()/args.getCdr().getCar().getNodeInt());
        }
        else if(symbol.toString().trim().equals("b=")) {
        	if(nar!=2) {
        		System.out.println("2 args required");
        		return Nil.getInstance();
        	}
        	return new BooleanLit(args.getCar().getNodeInt()==args.getCdr().getCar().getNodeInt());
        }
        else if(symbol.toString().trim().equals("b<")) {
        	if(nar!=2) {
        		System.out.println("2 args required");
        		return Nil.getInstance();
        	}
        	return new BooleanLit(args.getCar().getNodeInt()<args.getCdr().getCar().getNodeInt());
        }
        else if(symbol.toString().trim().equals("apply")) {
    		return (apply(args));
    	}
        else if(symbol.toString().trim().equals("eval")) {
        	return (args.eval(globalEnv));
    	}
        else if(symbol.toString().trim().equals("append")) {
        	return new Cons(args.getCar().getCar(),new Cons(args.getCar().getCdr(),args.getCdr()));
        }
        else if(symbol.toString().trim().equals("list")) {
        	return args;
        }
    	return Nil.getInstance();
    // The easiest way to implement BuiltIn.apply is as an
    // if-then-else chain testing for the different names of
    // the built-in functions.  E.g., here's how load could
    // be implemented:
    }
    public boolean isBuilt() {
    	return true;
    }
    
}