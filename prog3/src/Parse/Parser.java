// Parser -- the parser for the Scheme printer and interpreter
//
// Defines
//
//   class Parser;
//
// Parses the language
//
//   exp  ->  ( rest
//         |  #f
//         |  #t
//         |  ' exp
//         |  integer_constant
//         |  string_constant
//         |  identifier
//    rest -> )
//         |  exp+ [. exp] )
//
// and builds a parse tree.  Lists of the form (rest) are further
// `parsed' into regular lists and special forms in the constructor
// for the parse tree node class Cons.  See Cons.parseList() for
// more information.
//
// The parser is implemented as an LL(0) recursive descent parser.
// I.e., parseExp() expects that the first token of an exp has not
// been read yet.  If parseRest() reads the first token of an exp
// before calling parseExp(), that token must be put back so that
// it can be reread by parseExp() or an alternative version of
// parseExp() must be called.
//
// If EOF is reached (i.e., if the scanner returns a NULL) token,
// the parser returns a NULL tree.  In case of a parse error, the
// parser discards the offending token (which probably was a DOT
// or an RPAREN) and attempts to continue parsing with the next token.

package Parse;

import Tree.BooleanLit;
import Tree.IntLit;
import Tree.Cons;
import Tree.Ident;
import Tree.Nil;
import Tree.StrLit;
import Tree.Node;
import Tokens.Token;
import Tokens.TokenType;
public class Parser {
    private Scanner scanner;
    Token lookAhead;
    Node expNd;
    boolean isQuote;
    
    public Parser(Scanner s) {
        scanner = s;
    }
    public Node parseExp() {
    	return parseExp(scanner.getNextToken());
    }

    public Node parseExp(Token T) { 
        if(T==null)   return null;   
        else if (T.getType() == TokenType.TRUE){
           expNd= new BooleanLit(true);
        }
        else if (T.getType() == TokenType.FALSE){
            expNd= new BooleanLit(false);
        }
        else if (T.getType() == TokenType.LPAREN){
           lookAhead = scanner.getNextToken();
      
           expNd= parseRest();
        }
        else if (T.getType() == TokenType.IDENT){           
        	 expNd= new Ident(T.getName());          
        }
        else if (T.getType() == TokenType.QUOTE){
        	expNd= parseExp();  
        	expNd.isQuote=true;
        }
        else if(T.getType()==TokenType.INT){          
        	 expNd= new IntLit(T.getIntVal());
        }
        else if(T.getType()==TokenType.STRING){            
        	 expNd= new StrLit(T.getStrVal());
        }       
        else {
            System.out.println("invalid input exp");
            lookAhead=scanner.getNextToken();
        }  
        return expNd;
    }

    protected Node parseRest() {      	
        if (lookAhead ==null) return null;   
        if(lookAhead.getType()==TokenType.RPAREN){
            return new Nil();
        }       
        else if(lookAhead.getType()!=TokenType.RPAREN){       	
        		Node exp=parseExp(lookAhead);
        		lookAhead=scanner.getNextToken();
        		expNd=parseRest();  
        		expNd=new Cons(exp,expNd);
        }
        else{
            System.out.println("Invalid Input rest");
            lookAhead = scanner.getNextToken();
        }        
              return expNd;
    }
}