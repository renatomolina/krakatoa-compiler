package Lexer;
import java.util.*;
import Comp.*;

public class Lexer {

    public Lexer( char []input, CompilerError error ) {
        this.input = input;
          // add an end-of-file label to make it easy to do the lexer
        input[input.length - 1] = '\0';
          // number of the current line
        lineNumber = 1;
        tokenPos = 0;
        lastTokenPos = 0;
        beforeLastTokenPos = 0;
        this.error = error;
    }


    private static final int MaxValueInteger = 32767;
      // contains the keywords
    static private Hashtable keywordsTable;

     // this code will be executed only once for each program execution
	static {
		keywordsTable = new Hashtable();

		keywordsTable.put( "true", new Integer(Symbol.TRUE) );
		keywordsTable.put( "false", new Integer(Symbol.FALSE) );
		keywordsTable.put( "final", new Integer(Symbol.FINAL) );
		keywordsTable.put( "void", new Integer(Symbol.VOID) );
		keywordsTable.put( "null", new Integer(Symbol.NULL) );
		keywordsTable.put( "if", new Integer(Symbol.IF) );
		keywordsTable.put( "else", new Integer(Symbol.ELSE) );
		keywordsTable.put( "while", new Integer(Symbol.WHILE) );
		keywordsTable.put( "read", new Integer(Symbol.READ) );
		keywordsTable.put( "write", new Integer(Symbol.WRITE) );
		keywordsTable.put( "break", new Integer(Symbol.BREAK) );
		keywordsTable.put( "int", new Integer(Symbol.INT) );
		keywordsTable.put( "boolean", new Integer(Symbol.BOOLEAN) );
		keywordsTable.put( "return", new Integer(Symbol.RETURN) );
		keywordsTable.put( "class", new Integer(Symbol.CLASS) );
		keywordsTable.put( "super", new Integer(Symbol.SUPER) );
		keywordsTable.put( "this", new Integer(Symbol.THIS) );
		keywordsTable.put( "new", new Integer(Symbol.NEW) );
		keywordsTable.put( "public", new Integer(Symbol.PUBLIC) );
		keywordsTable.put( "private", new Integer(Symbol.PRIVATE) );
		keywordsTable.put( "String", new Integer(Symbol.STRING) );
		keywordsTable.put( "extends", new Integer(Symbol.EXTENDS) );
                keywordsTable.put( "static", new Integer(Symbol.STATIC) );

	}




    public void nextToken() {
        char ch;

        lastTokenPos = tokenPos;
        while (  (ch = input[tokenPos]) == ' ' || ch == '\r' ||
                 ch == '\t' || ch == '\n')  {
            // count the number of lines
          if ( ch == '\n')
            lineNumber++;
          tokenPos++;
          }
        if ( ch == '\0')
          token = Symbol.EOF;
        else
          if ( input[tokenPos] == '/' && input[tokenPos + 1] == '/' ) {
                // comment found
               while ( input[tokenPos] != '\0'&& input[tokenPos] != '\n' )
                 tokenPos++;
               nextToken();
          }
          else if ( input[tokenPos] == '/' && input[tokenPos + 1] == '*' ) {
             int posStartComment = tokenPos;
             int lineNumberStartComment = lineNumber;
             tokenPos += 2;
             while ( (ch = input[tokenPos]) != '\0' &&
                 (ch != '*' || input[tokenPos + 1] != '/') ) {
                if ( ch == '\n' )
                   lineNumber++;
                tokenPos++;
             }
             if ( ch == '\0' )
                error.show( "Comment opened and not closed",
                      getLine(posStartComment), lineNumberStartComment);
             else
                tokenPos += 2;
             nextToken();
          }
          else {
            if ( Character.isLetter( ch ) ) {
                // get an identifier or keyword
                StringBuffer ident = new StringBuffer();
                while ( Character.isLetter( ch = input[tokenPos] ) ||
                        Character.isDigit(ch) ||
                        ch == '_' ) {
                    ident.append(input[tokenPos]);
                    tokenPos++;
                }
                stringValue = ident.toString();
                  // if identStr is in the list of keywords, it is a keyword !
                Object value = keywordsTable.get(stringValue);
                if ( value == null )
                  token = Symbol.IDENT;
                else
                  token = ((Integer ) value).intValue();
            }
            else if ( Character.isDigit( ch ) ) {
                // get a number
                StringBuffer number = new StringBuffer();
                while ( Character.isDigit( input[tokenPos] ) ) {
                    number.append(input[tokenPos]);
                    tokenPos++;
                }
                token = Symbol.NUMBER;
                try {
                   numberValue = Integer.valueOf(number.toString()).intValue();
                } catch ( NumberFormatException e ) {
                   error.show("Number out of limits");
                }
                if ( numberValue > MaxValueInteger )
                   error.show("Number out of limits");
            }
            else {
                tokenPos++;
                switch ( ch ) {
                    case '+' :
                      token = Symbol.PLUS;
                      break;
                    case '-' :
                      token = Symbol.MINUS;
                      break;
                    case '*' :
                      token = Symbol.MULT;
                      break;
                    case '/' :
                      token = Symbol.DIV;
                      break;
                    case '<' :
                      if ( input[tokenPos] == '=' ) {
                        tokenPos++;
                        token = Symbol.LE;
                      }
                      else
                        token = Symbol.LT;
                      break;
                    case '>' :
                      if ( input[tokenPos] == '=' ) {
                        tokenPos++;
                        token = Symbol.GE;
                      }
                      else
                        token = Symbol.GT;
                      break;
                    case '=' :
                      if ( input[tokenPos] == '=' ) {
                        stringValue = "=";
                        tokenPos++;
                        token = Symbol.EQ;
                      }
                      else
                        token = Symbol.ASSIGN;
                      break;
                    case '!' :
                      if ( input[tokenPos] == '=' ) {
                         tokenPos++;
                         token = Symbol.NEQ;
                      }
                      else
                         token = Symbol.NOT;
                      break;
                    case '(' :
                      token = Symbol.LEFTPAR;
                      break;
                    case ')' :
                      token = Symbol.RIGHTPAR;
                      break;
                    case ',' :
                      token = Symbol.COMMA;
                      break;
                    case ';' :
                      token = Symbol.SEMICOLON;
                      break;
                    case '.' :
                      token = Symbol.DOT;
                      break;
                    case '&' :
                      if ( input[tokenPos] == '&' ) {
                         tokenPos++;
                         token = Symbol.AND;
                      }
                      else
                        error.show("& expected");
                      break;
                    case '|' :
                      if ( input[tokenPos] == '|' ) {
                         tokenPos++;
                         token = Symbol.OR;
                      }
                      else
                        error.show("| expected");
                      break;
                    case '{' :
                      token = Symbol.LEFTCURBRACKET;
                      break;
                    case '}' :
                      token = Symbol.RIGHTCURBRACKET;
                      break;
                    case '_' :
                      error.show("_ cannot start an indentifier");
                      break;
                    case '"' :
                       StringBuffer s = new StringBuffer();
                       while ( input[tokenPos] != '\0' && input[tokenPos] != '\n' )
                          if ( input[tokenPos] == '"' )
                             break;
                          else
                             if ( input[tokenPos] == '\\' ) {
                                if ( input[tokenPos+1] != '\n' && input[tokenPos+1] != '\0' ) {
                                   s.append(input[tokenPos]);
                                   tokenPos++;
                                   s.append(input[tokenPos]);
                                   tokenPos++;
                                }
                                else {
                                   s.append(input[tokenPos]);
                                   tokenPos++;
                                }
                             }
                             else {
                                s.append(input[tokenPos]);
                                tokenPos++;
                             }

                       if ( input[tokenPos] == '\0' || input[tokenPos] == '\n' ) {
                          error.show("Nonterminated string");
                          literalStringValue = "";
                       }
                       else {
                          tokenPos++;
                          literalStringValue = s.toString();
                       }
                       token = Symbol.LITERALSTRING;
                       break;
                    default :
                      error.show("Invalid Character: '" + ch + "'", false);
                }
            }
          }
        beforeLastTokenPos = lastTokenPos;
    }

      // return the line number of the last token got with getToken()
    public int getLineNumber() {
        return lineNumber;
    }

    public int getLineNumberBeforeLastToken() {
        return getLineNumber( lastTokenPos );
    }

    private int getLineNumber( int index ) {
        // return the line number in which the character input[index] is
        int i, n, size;
        n = 1;
        i = 0;
        size = input.length;
        while ( i < size && i < index ) {
          if ( input[i] == '\n' )
            n++;
          i++;
        }
        return n;
    }


    public String getCurrentLine() {
        //return getLine(lastTokenPos);
        return getLine(tokenPos);
    }

    public String getLineBeforeLastToken() {
        return getLine(beforeLastTokenPos);
    }

    private String getLine( int index ) {
        // get the line that contains input[index]. Assume input[index] is at a token, not
        // a white space or newline

        int i;
        if ( input.length <= 1 )
           return "";
        i = index;
        if ( i <= 0 )
          i = 1;
        else
          if ( i >= input.length )
            i = input.length;

        while ( input[i] == '\n' || input[i] == '\r' )
           i--;

        StringBuffer line = new StringBuffer();
          // go to the beginning of the line
        while ( i >= 1 && input[i] != '\n' )
          i--;
        if ( input[i] == '\n' )
          i++;
          // go to the end of the line putting it in variable line
        while ( input[i] != '\0' && input[i] != '\n' && input[i] != '\r' ) {
            line.append( input[i] );
            i++;
        }
        return line.toString();
    }

    public String getStringValue() {
       return stringValue;
    }

    public int getNumberValue() {
       return numberValue;
    }

    public String getLiteralStringValue() {
       return literalStringValue;
    }

          // current token
    public int token;
    private String stringValue, literalStringValue;
    private int numberValue;

    private int  tokenPos;
      //  input[lastTokenPos] is the last character of the last token found
    private int lastTokenPos;
      //  input[beforeLastTokenPos] is the last character of the token before the last
      // token found
    private int beforeLastTokenPos;

    private char []input;

    // number of current line. Starts with 1
    private int lineNumber;

    private CompilerError error;
}
