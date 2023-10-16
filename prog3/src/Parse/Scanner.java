// Scanner -- The lexical analyzer for the Scheme printer and interpreter.

package Parse;


import java.io.IOException;

import java.io.InputStream;
import java.io.PushbackInputStream;

import Tokens.Token;
import Tokens.TokenType;
import Tokens.IdentToken;
import Tokens.IntToken;
import Tokens.StrToken;

public class Scanner {
	
	private PushbackInputStream in;
	// Maximum length of strings and identifers
	private int BUFSIZE = 1000;
	private byte[] buf = new byte[BUFSIZE];

	public Scanner(InputStream i) {
		in = new PushbackInputStream(i);
	}

	public Token getNextToken() {
		int ch;

		try {
			// It would be more efficient if we'd maintain our own
			// input buffer and read characters out of that
			// buffer, but reading individual characters from the
			// input stream is easier.
			// Return null on EOF
			ch = in.read();		
			if (ch == -1)
				return null;			
			else if (ch == ' ' || ch == '\t' || ch == '\n' || ch == '\f'||ch=='\r'){
				return getNextToken();
			}
			else if (ch == ';'){
				while (ch != '\n'){
					ch = in.read();
				}
				return getNextToken();
			}
			// Special characters
			else if (ch == '\'') {
				return new Token(TokenType.QUOTE);}
			else if (ch == '(') {
				return new Token(TokenType.LPAREN);}
			else if (ch == ')') {
				return new Token(TokenType.RPAREN);}
			else if (ch == '.') {
				// We ignore the special identifier `...'.
				return new Token(TokenType.DOT);}

			// Boolean constants
			else if (ch == '#') {
				ch = in.read();
				if (ch == 't')
					return new Token(TokenType.TRUE);
				else if (ch == 'f')
					return new Token(TokenType.FALSE);
				else if (ch == -1) {
					System.err.println("Unexpected EOF following #");
					return null;
				} 
				else {
					System.err.println("Illegal character '" +
									  (char)ch + "' following #");
					return getNextToken();
				}
			}

			// String constants
			else if (ch == '"') {
				byte[] buf=new byte[BUFSIZE];
				ch=in.read();
				int i = 0;
				while (ch != '"'){					
					buf[i] = (byte)ch;
					ch = in.read();
					i++;
				}
				return new StrToken(new String(buf));
			}
			// Integer constants
			else if (ch >= '0' && ch <= '9') {
				int i = ch - '0';
				ch=in.read();
				while (ch >= '0' && ch <= '9'){
					i = i*10 + (ch-'0');
					ch=in.read();
				}
				// Put the character after the integer back into the input
				in.unread(ch);
				return new IntToken(i);
			}
			else if(ch=='-') {
				int j = 0;
				ch = in.read();
				if(ch==' ') {
					in.unread(ch);
					return new IdentToken("-");
				}
				else if (ch >= '0' && ch <= '9') {
					j = '0' - ch;
					ch=in.read();
					while (ch >= '0' && ch <= '9'){
						j = j*10 + ('0'-ch);
						ch=in.read();
					}
					// Put the character after the integer back into the input
					in.unread(ch);
				}
					return new IntToken(j);
			}
			// Identifiers
			else if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || 
					 ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '=' ||
					 ch == '?' || ch == '!' || ch == '<' || ch == '>'||ch=='%'){
				/* or ch is some other valid first character for an identifier */
				byte[] buf=new byte[BUFSIZE];
				int i = 0;
				while ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || 
				(ch >= '0' && ch <= '9') ||ch == '+' || ch == '-' || ch == '*' || 
				ch == '/' || ch == '=' || ch == '?' || ch == '!' || ch == '<' || ch == '>'||ch=='%'){				
					buf[i] = (byte)ch;
					ch = in.read();
					i++;
				}
				// Put the character after the identifier back into the input
				in.unread(ch);				
				return new IdentToken(new String(buf));
			}
			// Illegal character
			else {
				System.err.println("Illegal input character '" + (char)ch + '\'');
				return getNextToken();
			}
		} catch (IOException e) {
			System.err.println("IOException: " + e.getMessage());
			return null;
		}
	}
}
