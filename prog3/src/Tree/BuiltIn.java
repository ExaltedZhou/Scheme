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
    	env.define("integer?",new BuiltIn(new Ident("integer?")));
    	env.define("rational?",new BuiltIn(new Ident("rational?")));
    	env.define("zero?",new BuiltIn(new Ident("zero?")));
    	env.define("positive?",new BuiltIn(new Ident("positive?")));
    	env.define("negative?",new BuiltIn(new Ident("negative?")));
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
    	env.define("w",new BuiltIn(new Ident("w")));
    	env.define("display",new BuiltIn(new Ident("display")));
    	env.define("newline",new BuiltIn(new Ident("newline")));
    	env.define("load",new BuiltIn(new Ident("load")));
    	env.define("eval",new BuiltIn(new Ident("eval")));
    	env.define("apply",new BuiltIn(new Ident("apply")));
    	env.define("append",new BuiltIn(new Ident("append")));
    	env.define("quotient",new BuiltIn(new Ident("quotient")));
    	env.define("remainder",new BuiltIn(new Ident("remainder")));
    	env.define("numerator",new BuiltIn(new Ident("numerator")));
    	env.define("denominator",new BuiltIn(new Ident("denominator")));
    	env.define("gcd",new BuiltIn(new Ident("gcd")));
    	env.define("eqv?",new BuiltIn(new Ident("eqv?")));
    	env.define("equal?",new BuiltIn(new Ident("equal?")));

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
    		int sumNum;
    		int sumDen;
    		Node a = args.getCar();
    		Node b = args.getCdr().getCar();
    		sumDen = getLcM(a.getDenominator().getNodeInt(),b.getDenominator().getNodeInt());
    		sumNum = a.getNumerator().getNodeInt()*sumDen/a.getDenominator().getNodeInt()+
    				 b.getNumerator().getNodeInt()*sumDen/b.getDenominator().getNodeInt();
    		while(!args.getCdr().getCdr().isNull()) {
    			args=args.getCdr();
    			sumDen = getLcM(sumDen,args.getCdr().getCar().getDenominator().getNodeInt());
    			sumNum += args.getCdr().getCar().getNumerator().getNodeInt()*sumDen/args.getCdr().getCar().getDenominator().getNodeInt();
    		}
    		if(sumNum==0)
    			return new IntLit(0);
    		else {
    			int rationalGcd=getGcd(sumNum,sumDen);
    			sumNum/=rationalGcd;
    			sumDen/=rationalGcd;
    			if(sumDen==1)
    				return new IntLit(sumNum);
    			else if(sumDen!=1) {
    				Node c1 = new Cons(new IntLit(sumDen),Nil.getInstance());
    				Node c2 = new Cons(new IntLit(sumNum),c1);
    				return new Cons(new Ident("rational"),c2);
    			}
    		}
    		return Nil.getInstance();
    	}
    	else if(symbol.toString().trim().equals("-")) { 		
    		int subNum;
    		int subDen;
    		Node a = args.getCar();
    		if(args.getCdr().isNull()) {
    			if(a.isInteger()) 
    				return new IntLit(-a.getNodeInt());
    			else if(a.isRational()) {
    				Node cb1=new Cons(a.getCdr().getCdr().getCar(),Nil.getInstance());
    				Node cb2=new Cons(new IntLit(-a.getCdr().getCar().getNodeInt()),cb1);
    				return new Cons(new Ident("rational"),cb2);
    			}
    		} 
    		Node b = args.getCdr().getCar();
    		subDen = getLcM(a.getDenominator().getNodeInt(),b.getDenominator().getNodeInt());
    		subNum = a.getNumerator().getNodeInt()*subDen/a.getDenominator().getNodeInt()-
    				 b.getNumerator().getNodeInt()*subDen/b.getDenominator().getNodeInt();
    		while(!args.getCdr().getCdr().isNull()) {
    			args=args.getCdr();
    			subDen = getLcM(subDen,args.getCdr().getCar().getDenominator().getNodeInt());
    			subNum -= args.getCdr().getCar().getNumerator().getNodeInt()*subDen/args.getCdr().getCar().getDenominator().getNodeInt();
    		}
    		if(subNum==0)
    			return new IntLit(0);
    		else {
    			int rationalGcd=getGcd(subNum,subDen);
    			subNum/=rationalGcd;
    			subDen/=rationalGcd;
    			if(subDen==1)
    				return new IntLit(subNum);
    			else if(subDen!=1) {
    				Node cb1 = new Cons(new IntLit(subDen),Nil.getInstance());
    				Node cb2 = new Cons(new IntLit(subNum),cb1);
    				return new Cons(new Ident("rational"),cb2);
    			}
    		}
    		return Nil.getInstance();
    	}
    	else if(symbol.toString().trim().equals("*")) {
    		int mulNum;
    		int mulDen;
    		Node a = args.getCar();
    		Node b = args.getCdr().getCar();
    		mulDen = a.getDenominator().getNodeInt()*b.getDenominator().getNodeInt();
    		mulNum = a.getNumerator().getNodeInt()*b.getNumerator().getNodeInt();
    		while(!args.getCdr().getCdr().isNull()) {
    			args=args.getCdr();
    			mulDen = mulDen*args.getCdr().getCar().getDenominator().getNodeInt();
    			mulNum *= args.getCdr().getCar().getNumerator().getNodeInt();
    		}
    		if(mulNum==0)
    			return new IntLit(0);
    		else {
    			int rationalGcd=getGcd(mulNum,mulDen);
    			mulNum/=rationalGcd;
    			mulDen/=rationalGcd;
    			if(mulDen==1)
    				return new IntLit(mulNum);
    			else if(mulDen!=1) {
    				Node cb1 = new Cons(new IntLit(mulDen),Nil.getInstance());
    				Node cb2 = new Cons(new IntLit(mulNum),cb1);
    				return new Cons(new Ident("rational"),cb2);
    			}
    		}
    		return Nil.getInstance();
    	}
    	else if(symbol.toString().trim().equals("/")) {
    		int divNum;
    		int divDen;
    		Node a = args.getCar();
    		if(args.getCdr().isNull()) {
    			if(a.isInteger()) {
    				Node cb1 = new Cons(new IntLit(a.getNodeInt()),Nil.getInstance());
    				Node cb2 = new Cons(new IntLit(1),cb1);
    				return new Cons(new Ident("rational"),cb2);
    			}
    			else if(a.isRational()) {
    				if(a.getCdr().getCar().getNodeInt()==1) {
    					return new IntLit(a.getCdr().getCdr().getCar().getNodeInt());
    				}
    				else if(a.getCdr().getCar().getNodeInt()!=1) {
    					Node cb1=new Cons(a.getCdr().getCar(),Nil.getInstance());
    					Node cb2=new Cons(new IntLit(a.getCdr().getCdr().getCar().getNodeInt()),cb1);
    					return new Cons(new Ident("rational"),cb2);
    				}
    			}
    		} 
    		Node b = args.getCdr().getCar();
    		divDen = a.getDenominator().getNodeInt()*b.getNumerator().getNodeInt();
    		divNum = a.getNumerator().getNodeInt()*b.getDenominator().getNodeInt();
    		while(!args.getCdr().getCdr().isNull()) {
    			args=args.getCdr();
    			divDen = divDen*args.getCdr().getCar().getNumerator().getNodeInt();
    			divNum *= args.getCdr().getCar().getDenominator().getNodeInt();
    		}
    		if(divNum==0)
    			return new IntLit(0);
    		else {
    			int rationalGcd=getGcd(divNum,divDen);
    			divNum/=rationalGcd;
    			divDen/=rationalGcd;
    			if(divDen==1)
    				return new IntLit(divNum);
    			else if(divDen!=1) {
    				Node cb1 = new Cons(new IntLit(divDen),Nil.getInstance());
    				Node cb2 = new Cons(new IntLit(divNum),cb1);
    				return new Cons(new Ident("rational"),cb2);
    			}
    		}
    		return Nil.getInstance();
    	}
    	else if(symbol.toString().trim().equals("<")) {
    		Node a = args.getCar();
    		Node b = args.getCdr().getCar();
    		int subDen = getLcM(a.getDenominator().getNodeInt(),b.getDenominator().getNodeInt());
    		int subNum = a.getNumerator().getNodeInt()*subDen/a.getDenominator().getNodeInt()-
    				 b.getNumerator().getNodeInt()*subDen/b.getDenominator().getNodeInt();
    		boolean bv = false;
    		if(subNum<0) {
    		   bv = true;
    		}
    		else {
    			return new BooleanLit(false);
    		}
    	if(bv) {
    		while(!args.getCdr().getCdr().isNull()) {
    			args=args.getCdr();
    			subDen=getLcM(args.getCar().getDenominator().getNodeInt(),args.getCdr().getCar().getDenominator().getNodeInt());
    			subNum=args.getCar().getNumerator().getNodeInt()*subDen/args.getCar().getDenominator().getNodeInt()-
    					args.getCdr().getCar().getNumerator().getNodeInt()*subDen/args.getCdr().getCar().getDenominator().getNodeInt();
    			if(subNum<0) {
    				bv = true;
    			}
    			else {
    				bv=false;
    				break;
    			}
    		}
    	}
    	return new BooleanLit(bv);
    	}
    	else if(symbol.toString().trim().equals("=")) {
    		Node a = args.getCar();
    		Node b = args.getCdr().getCar();
    		int subDen = getLcM(a.getDenominator().getNodeInt(),b.getDenominator().getNodeInt());
    		int subNum = a.getNumerator().getNodeInt()*subDen/a.getDenominator().getNodeInt()-
    				 b.getNumerator().getNodeInt()*subDen/b.getDenominator().getNodeInt();
    		boolean bv = false;
    		if(subNum==0) {
    		   bv = true;
    		}
    		else {
    			return new BooleanLit(false);
    		}
    	if(bv) {
    		while(!args.getCdr().getCdr().isNull()) {
    			args=args.getCdr();
    			subDen=getLcM(args.getCar().getDenominator().getNodeInt(),args.getCdr().getCar().getDenominator().getNodeInt());
    			subNum=args.getCar().getNumerator().getNodeInt()*subDen/args.getCar().getDenominator().getNodeInt()-
    					args.getCdr().getCar().getNumerator().getNodeInt()*subDen/args.getCdr().getCar().getDenominator().getNodeInt();
    			if(subNum==0) {
    				bv = true;
    			}
    			else {
    				bv=false;
    				break;
    			}
    		}
    	}
    	return new BooleanLit(bv);
    	}
    	else if(symbol.toString().trim().equals("symbol?")) {
    		if(nar!=1) {
    			System.out.println("1 args required");
    			return Nil.getInstance();
    		}
    		return new BooleanLit(args.isSymbol());
    	}
    	else if(symbol.toString().trim().equals("number?")) {
    		if(args.getCar().isNumber())
    			return new BooleanLit(true);
    		return new BooleanLit(false);
    	}
    	else if(symbol.toString().trim().equals("integer?")) {
    		return new BooleanLit(args.getCar().isInteger());
    	}
    	else if(symbol.toString().trim().equals("rational?")) {
    		return new BooleanLit(args.getCar().isRational());
    	}
    	else if(symbol.toString().trim().equals("zero?")) {
    		return new BooleanLit(args.getCar().isZero());
    	}
    	else if(symbol.toString().trim().equals("positive?")) {
    		return new BooleanLit(args.getCar().isPositive());
    	}
    	else if(symbol.toString().trim().equals("negative?")) {
    		return new BooleanLit(args.getCar().isNegative());
    	}
    	else if(symbol.toString().trim().equals("car")) {
    		if(args.getCar().getCar()==null || !args.isPair()) {
    			//System.out.println("1 pair required");
    			return Nil.getInstance();
    		}
    		return args.getCar().getCar();
    	}
    	else if(symbol.toString().trim().equals("cdr")) {
    		return args.getCar().getCdr();
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
    		if(args.getCar().isSymbol() && args.getCdr().getCar().isSymbol())
    			return new BooleanLit(args.getCar().toString().trim()==args.getCdr().getCar().toString().trim());
    		return new BooleanLit(false);
    	}
    	else if(symbol.toString().trim().equals("eqv?")) {
    		if((args.getCar().isSymbol() && args.getCdr().getCar().isSymbol()) || 
    			args.getCar().isInteger() && args.getCar().isInteger())
    			return new BooleanLit(args.getCar().toString().trim()==args.getCdr().getCar().toString().trim());
    		else if(args.getCar().isRational() && args.getCdr().getCar().isRational()) {
    			Node a = args.getCar();
        		Node b = args.getCdr().getCar();
        		int subDen = getLcM(a.getDenominator().getNodeInt(),b.getDenominator().getNodeInt());
        		int subNum = a.getNumerator().getNodeInt()*subDen/a.getDenominator().getNodeInt()-
        				 b.getNumerator().getNodeInt()*subDen/b.getDenominator().getNodeInt();
        		boolean bv = false;
        		if(subNum==0) {
        		   return new BooleanLit(true);
        		}
        		else {
        			return new BooleanLit(false);
        		}
    		}
    		return new BooleanLit(false);
    	}	
    	else if(symbol.toString().trim().equals("equal?")) {
    		if((args.getCar().isSymbol() && args.getCdr().getCar().isSymbol()) || 
        			(args.getCar().isInteger() && args.getCar().isInteger()))
        			return new BooleanLit(args.getCar().toString().trim()==args.getCdr().getCar().toString().trim());
    		else if(args.getCar().isPair() && args.getCdr().getCar().isPair()) {
        		if(args.getCar().isRational() && args.getCdr().getCar().isRational()) {
        			Node a = args.getCar();
            		Node b = args.getCdr().getCar();
            		int subDen = getLcM(a.getDenominator().getNodeInt(),b.getDenominator().getNodeInt());
            		int subNum = a.getNumerator().getNodeInt()*subDen/a.getDenominator().getNodeInt()-
            				 b.getNumerator().getNodeInt()*subDen/b.getDenominator().getNodeInt();
            		boolean bv = false;
            		if(subNum==0) {
            		   return new BooleanLit(true);
            		}
            		else {
            			return new BooleanLit(false);
            		}
        		}
        		else 
        			return new BooleanLit(args.getCar().toString().trim()==args.getCdr().getCar().toString().trim());
        	}	
    		return new BooleanLit(false);
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
    	
        else if(symbol.toString().trim().equals("w")) {
    		Node a = args.getCar();
    		if(a.isPair()) {
    			if(a.isRational()) {
    				System.out.print("("+a.getCdr().getCar().getNodeInt()+"/"+
    								 a.getCdr().getCdr().getCar().getNodeInt()+")");
    			}
    			else {
    				System.out.print("(");
    				while(!a.isNull()) {
    					if(a.getCar().isRational()) {
    	    				System.out.print(+a.getCar().getCdr().getCar().getNodeInt()+"/"+
   								 a.getCar().getCdr().getCdr().getCar().getNodeInt());
    					}
    					else 
    						System.out.print(a.getCar().toString().trim()+" ");		
    					a=a.getCdr();
    				}
    				System.out.print(")");
    			}
    		}
    		else 
    			System.out.println(a.toString().trim());
    		return new Node();
    	}
        else if(symbol.toString().trim().equals("numerator")) {
        	return args.getCar().getNumerator();
        }
        else if(symbol.toString().trim().equals("denominator")) {
        	return args.getCar().getDenominator();
        }
        else if(symbol.toString().trim().equals("gcd")) {
        	Node int1=args.getCar().getCar();
        	Node int2=args.getCar().getCdr().getCar();
        	int NodeGcd=getGcd(int1.getNodeInt(),int2.getNodeInt());
        	return new IntLit(NodeGcd);
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
    public int getGcd(int a, int b) {
    	a=Math.abs(a);
    	b=Math.abs(b);
    	while(a!=b) {
    		if(a>b) 
    			a=a-b;
    		else 
    			b=b-a;
    	}
		return b;
    }
    public int getLcM(int c, int d) {
    	int e = getGcd(c,d);
    	return Math.abs(c*d/e);
    }
    
}