package Comp;

import AST.ReturnStat;
import AST.*;
import Lexer.*;

import java.io.*;
import java.util.*;


public class Compiler {

      // compile must receive an input with an character less than
      // p_input.lenght
   public Program compile( char []input, PrintWriter outError ) {

       error = new CompilerError( lexer, new PrintWriter(outError) );
       symbolTable = new SymbolTable();
       lexer = new Lexer(input, error);
       error.setLexer(lexer);


       Program p = null;
       try {
          lexer.nextToken();
          if ( lexer.token == Symbol.EOF )
             error.show("Unexpected EOF");
          p = program(); 
                    if ( lexer.token != Symbol.EOF ) {
             p = null;
             error.show("EOF expected");
          }
       } catch ( Exception e ) {
              // the below statement prints the stack of called methods.
              // of course, it should be removed if the compiler were
              // a production compiler.

           //   e.printStackTrace();
                 
          p = null;
       }

       return p;
   }


   private Program program() throws ClassNotFoundException {
     // Program ::=  ClassDec { ClassDec }
      ClassDec classDec = classDec();
      symbolTable.putInGlobal(classDec.getName(), classDec);      
      ArrayList<ClassDec> cdList = new ArrayList<ClassDec>();
      while ( lexer.token == Symbol.CLASS ){
          ClassDec cD = classDec();
         cdList.add(cD);
         symbolTable.putInGlobal(cD.getName(), cD);
      }
      return new Program(classDec, cdList);
   }

   private ClassDec classDec() throws ClassNotFoundException {

      /* ClassDec ::=   ``class'' Id [ ``extends''  Id ]
                        "{"   MemberList "}"
         MemberList ::= { Member }
         Member ::= InstVarDec | MethodDec
         InstVarDec ::= [ "static"  ] "private"  Type  IdList  ";"
         MethodDec ::= Qualifier ReturnType Id "("[ FormalParamDec ]  ")"
                       "{"  StatementList "}"
         Qualifier} ::=  [ "static"  ] ( "private" |  "public" )
      */
       MemberList mList = null;
       ArrayList<Member> mArray = new ArrayList<Member>();
       ClassDec classDec = null;
      if ( lexer.token != Symbol.CLASS )
         error.show("'class' expected");
      lexer.nextToken();
      if ( lexer.token != Symbol.IDENT) {
         error.show(CompilerError.identifier_expected);
      }
      Id className = id(lexer.getStringValue());

      lexer.nextToken();
      Id superClassName = null;
      if ( lexer.token == Symbol.EXTENDS ) {
         lexer.nextToken();
         if ( lexer.token != Symbol.IDENT ){
            error.show(CompilerError.identifier_expected);
         }
         superClassName = id(lexer.getStringValue());

         lexer.nextToken();
      }
      classDec = new ClassDec(className,superClassName);
      symbolTable.putInGlobal(className.getName(),classDec);
      if ( lexer.token != Symbol.LEFTCURBRACKET )
         error.show("{ expected", true);
      lexer.nextToken();
      symbolTable.putInLocal(className.getName(), className);

      while ( lexer.token == Symbol.PRIVATE ||
              lexer.token == Symbol.PUBLIC || lexer.token == Symbol.STATIC  ) {
         boolean isStatic = false;

         if (lexer.token == Symbol.STATIC){
            isStatic = true;
            lexer.nextToken();
         }
         Qualifier qualifier;

         switch( lexer.token ) {
            case Symbol.PRIVATE:
               lexer.nextToken();
               qualifier = new Qualifier(isStatic,Symbol.PRIVATE);
               break;
            case Symbol.PUBLIC:
               lexer.nextToken();
               qualifier = new Qualifier(isStatic, Symbol.PUBLIC);
               break;
            default :
               error.show("private, or public expected");
               qualifier = new Qualifier(isStatic, Symbol.PUBLIC);
         }
         Type t = type();
         if ( lexer.token != Symbol.IDENT ){
            error.show("Identifier expected");
         }
         Id id = id(lexer.getStringValue());
         lexer.nextToken();
         Member m = null;
         if ( lexer.token == Symbol.LEFTPAR ){
            m = methodDec(t, id, qualifier);
             ArrayList<Statement> aux = m.getList().getList();             
      if(t.getType() !=null){
          for(Statement s:aux){
              System.out.println(s.getClass().getName());
            if(s.getClass().getName().equals("AST.ReturnStat"))
                break;
          error.show("return expected");
          }}
      lexer.nextToken();
         }
         else if ( qualifier.getProtection() != Symbol.PRIVATE )
            error.show("Attempt to declare a public instance variable");
         else{
            m = instanceVarDec(t, id, isStatic);
         //   lexer.nextToken();
         }
         mArray.add(m);
      }

      if ( lexer.token != Symbol.RIGHTCURBRACKET )
         error.show("public/private or \"}\" expected");
      lexer.nextToken();       
       classDec.setMemberList(new MemberList(mArray));
      return classDec;
   }


   private InstVarDec instanceVarDec( Type type, Id id, boolean isStatic ) {
      //   InstVarDec ::= [ "static"  ] "private"  Type  IdList  ";"      
      IdList idL;
      ArrayList<Id> ids = new ArrayList<Id>();      
      if(type.getType() == null)
          error.show("type expected");
      while ( lexer.token == Symbol.COMMA ) {
         lexer.nextToken();
         if ( lexer.token != Symbol.IDENT ){
            error.show("Identifier expected");
         }
         ids.add(new Id(lexer.getStringValue()));
         lexer.nextToken();
      }
      idL = new IdList(id, ids);
      if ( lexer.token != Symbol.SEMICOLON )
         error.show(CompilerError.semicolon_expected);
      lexer.nextToken();
      return new InstVarDec(isStatic, type, idL );
   }



   private MethodDec methodDec(Type type, Id id, Qualifier qualifier ) throws ClassNotFoundException {
      /*   MethodDec ::= Qualifier ReturnType Id "("[ FormalParamDec ]  ")"
                       "{"  StatementList "}"
      */

      ReturnType rt = new ReturnType(type);
      FormalParamDec fParamDec = null;
      if ( lexer.token != Symbol.LEFTPAR ){
          error.show("( expected");          
      }
           lexer.nextToken();
         fParamDec = formalParamDec();
      if ( lexer.token != Symbol.RIGHTPAR )
         error.show(") expected");
      lexer.nextToken();     
      if ( lexer.token != Symbol.LEFTCURBRACKET )
         error.show("{ expected");

      lexer.nextToken();
      StatementList sList = statementList();
      if ( lexer.token != Symbol.RIGHTCURBRACKET )
         error.show("} expected");
      return new MethodDec(qualifier, rt, id,fParamDec,sList) ;
   }




   private LocalDec localDec( Type type ) {
      // LocalDec ::= Type IdList ";"
      if ( lexer.token != Symbol.IDENT ){
         error.show("Identifier expected");
      }
      IdList idList = idList();
      if(lexer.token != Symbol.SEMICOLON)
          error.show("; expected");
      lexer.nextToken();
      return new LocalDec(type, idList);
   }

   private FormalParamDec formalParamDec() {
      //  FormalParamDec ::= ParamDec { "," ParamDec }                
                if(lexer.token == Symbol.RIGHTPAR)
                    return new FormalParamDec(null,null);
      ParamDec param = paramDec();
      ArrayList<ParamDec> paramList = new ArrayList<ParamDec>();
      while ( lexer.token == Symbol.COMMA ) {
         lexer.nextToken();
         paramList.add(paramDec());
      }
      return new FormalParamDec(param,paramList);
   }

    private ParamDec paramDec() {
      // ParamDec ::= Type Id
      Type type = type();
      if ( lexer.token != Symbol.IDENT ){
          error.show("Identifier expected");
      }
      String name = lexer.getStringValue();
      Id id = id(name);
      lexer.nextToken();
      return new ParamDec(type, id);
   }


    private Type type() {
        // Type ::= BasicType | Id
        Type result=null;

        switch ( lexer.token ) {
            case Symbol.INT :
               result = new BasicType(Symbol.INT);
               break;
            case Symbol.BOOLEAN :
               result = new BasicType(Symbol.BOOLEAN);
               break;
            case Symbol.STRING :
               result = new BasicType(Symbol.STRING);
               break;
            case Symbol.IDENT :
               //# corrija: faça uma busca na TS para buscar a classe
               // IDENT deve ser uma classe.                
               if(symbolTable.getInGlobal(lexer.getStringValue()) == null &&
                       symbolTable.getInLocal(lexer.getStringValue()) == null)
                   error.show("Cannot find type");
               result = id(lexer.getStringValue());
               break;
            case Symbol.VOID :
                Type t = null;
                result = new ReturnType(t);
                break;
            default :
              error.show("Type expected");
        }
        lexer.nextToken();
        return result;
    }




    private CompStatement compositeStatement() {
        if ( lexer.token != Symbol.LEFTCURBRACKET )
          error.show("{ expected");
        lexer.nextToken();
        StatementList sL = statementList();
        if ( lexer.token != Symbol.RIGHTCURBRACKET )
          error.show("} expected");
        else
          lexer.nextToken();
        return new CompStatement(sL);
    }



   private StatementList statementList() {
       // CompStatement ::= "{" { Statement } "}"
       int tk;
       boolean retorno = false;
       ArrayList<Statement> sList = new ArrayList<Statement>();
          // statements always begin with an identifier, if, read, write, ...
       while ( (tk = lexer.token) == Symbol.IDENT|| tk == Symbol.INT ||
                tk == Symbol.BOOLEAN|| tk == Symbol.TRUE || tk == Symbol.FALSE||
                tk == Symbol.STRING ||tk == Symbol.RETURN ||
                tk == Symbol.READ || tk == Symbol.WRITE ||
                tk == Symbol.THIS||tk == Symbol.SUPER ||
                tk == Symbol.IF ||tk == Symbol.BREAK ||
                tk == Symbol.WHILE ||tk == Symbol.SEMICOLON || tk == Symbol.LEFTCURBRACKET ){
           if(tk == Symbol.RETURN)
            retorno = true;
          sList.add(statement());
       }
       return new StatementList(sList,retorno);
   }



   private Statement statement() {
     /*
        Statement ::= Assignment ``;'' | IfStat |WhileStat
            |  MessageSend ``;''  | ReturnStat ``;''
            |  ReadStat ``;'' | WriteStat ``;'' | ``break'' ``;''
            | ``;'' | CompStatement | LocalDec
     */
     Statement st = null;
     Type t;
     Id id2 = null;
     Id id = null;
      switch (lexer.token) {
         case Symbol.THIS :
             lexer.nextToken();
             if(lexer.token != Symbol.DOT)
                error.show(". expected");
             lexer.nextToken();
             if(lexer.token != Symbol.IDENT)
                error.show("ident expected");
             id = id(lexer.getStringValue());
             lexer.nextToken();
             if(lexer.token == Symbol.ASSIGN){
                 id2 = id(lexer.getStringValue());                 
                 st = assignment(new LeftValue(id, id2, true));
             }
             else st = messageSendStatement("this", id);
             break;
          case Symbol.IDENT :
             if(symbolTable.getInGlobal(lexer.getStringValue()) != null
                     ||symbolTable.getInLocal(lexer.getStringValue()) !=null ){
                 t = id(lexer.getStringValue());
                 lexer.nextToken();
                 if(lexer.token == Symbol.IDENT)
                    st = localDec(t);
                 else
                     st = messageSendStatement(null,id);
                 break;
             }else{
             id = id(lexer.getStringValue());
             lexer.nextToken();
             if(lexer.token == Symbol.ASSIGN){
                 st = assignment(new LeftValue(id,id2,false));

             }
             else{
                 st = messageSendStatement(null,id);
                 lexer.nextToken();
              }
             }
             break;
         case Symbol.SUPER :
            lexer.nextToken(); 
            /*if(lexer.token != Symbol.DOT)
                error.show(". expected");            
            lexer.nextToken();
            if(lexer.token != Symbol.IDENT)
                error.show("ident expected");
             */
            id = null;
            st = messageSendStatement("super",id);
            break;
         case Symbol.INT :
             lexer.nextToken();
             t = new BasicType(Symbol.INT);
             st = localDec(t);
             break;
          case Symbol.BOOLEAN :
             lexer.nextToken();
             t = new BasicType(Symbol.BOOLEAN);
             st = localDec(t);
             break;
          case Symbol.STRING:
             lexer.nextToken();
             t = new BasicType(Symbol.STRING);
             st = localDec(t);
             break;
         case Symbol.RETURN :
            lexer.nextToken();
            Expression expr = expr();
            st = new ReturnStat(expr);
            break;
         case Symbol.READ :
            st = readStatement();
            break;
         case Symbol.WRITE :
            st = writeStatement();
            break;
         case Symbol.IF :
            st = ifStatement();
            break;
         case Symbol.BREAK :
            st = breakStatement();
            break;
         case Symbol.WHILE :
            st = whileStatement();
            break;
         case Symbol.SEMICOLON :
            st = nullStatement();
            break;
         case Symbol.LEFTCURBRACKET :
            st = compositeStatement();
            break;
         default :
            error.show("Statement expected");
      }
      return st;
   }

   private IdList idList(){
        Id id = id(lexer.getStringValue());
        lexer.nextToken();
        ArrayList<Id> idList = new ArrayList<Id>();        
        while(lexer.token == Symbol.COMMA){
            lexer.nextToken();
            idList.add(id(lexer.getStringValue()));
            lexer.nextToken();
        }
        return new IdList(id,idList);
   }
   private Assignment assignment(LeftValue lf) {
     /*
       Assignment ::= LeftValue "=" Expression
       LeftValue} ::= [ "this" "." ] Id
       MessageSend ::= ReceiverMessage "." Id "("  [ ExpressionList ] ")"
       ReceiverMessage ::=  "super" | Id | "this" | "this" "."  Id
       LocalDec ::= Type IdList ";"
     */
      boolean hasThis = false;
      boolean hasSemicolon = false;
      if(lexer.token != Symbol.ASSIGN){
        error.show("= expected");
      }
      lexer.nextToken();
      Expression expr = expr();
      if(lexer.token == Symbol.SEMICOLON){
        hasSemicolon = true;
        lexer.nextToken();
      }
      return new Assignment(lf, expr,hasSemicolon);

   }

   private MessageSend messageSend(String str, Id id){
            ReceiverMessage RM = new ReceiverMessage(id,str);
            ExpressionList exprList = null;
           // if(str.equals("this")||str.equals("super")){
               if(lexer.token == Symbol.LEFTPAR){
                lexer.nextToken();
                if(lexer.token == Symbol.PLUS || lexer.token == Symbol.MINUS||
                lexer.token == Symbol.TRUE ||lexer.token == Symbol.FALSE ||
                lexer.token == Symbol.NUMBER || lexer.token == Symbol.LITERALSTRING ||
                lexer.token == Symbol.THIS||lexer.token == Symbol.SUPER ||
                lexer.token == Symbol.LEFTPAR ||lexer.token == Symbol.NOT ||
                lexer.token == Symbol.NULL ||lexer.token == Symbol.NEW ||
                lexer.token == Symbol.IDENT){
                   exprList = exprList();
                }
            if(lexer.token != Symbol.RIGHTPAR)
                error.show(") expected");
            lexer.nextToken();
            return new MessageSend(RM,id,exprList);
               }
    //}
            if(lexer.token != Symbol.DOT)
                error.show(". expected");            
            lexer.nextToken();
            if(lexer.token != Symbol.IDENT)
                error.show("ident expected");
            id = id(lexer.getStringValue());
            lexer.nextToken();
            if(lexer.token != Symbol.LEFTPAR)
                error.show("( expected");
            lexer.nextToken();
                while(lexer.token == Symbol.PLUS || lexer.token == Symbol.MINUS||
                lexer.token == Symbol.TRUE ||lexer.token == Symbol.FALSE ||
                lexer.token == Symbol.NUMBER || lexer.token == Symbol.LITERALSTRING ||
                lexer.token == Symbol.THIS||lexer.token == Symbol.SUPER ||
                lexer.token == Symbol.LEFTPAR ||lexer.token == Symbol.NOT ||
                lexer.token == Symbol.NULL ||lexer.token == Symbol.NEW){
                    exprList = exprList();
                }
            if(lexer.token != Symbol.RIGHTPAR){
                error.show(") expected");
            }
            lexer.nextToken();
            return new MessageSend(RM,id,exprList);

   }

   private MessageSendStatement messageSendStatement(String str, Id id){
            ExpressionList exprList = null;
            ReceiverMessage RM = new ReceiverMessage(id,str);
            if(str.equals("this")){
               if(lexer.token == Symbol.LEFTPAR){
                lexer.nextToken();
                if(lexer.token == Symbol.PLUS || lexer.token == Symbol.MINUS||
                lexer.token == Symbol.TRUE ||lexer.token == Symbol.FALSE ||
                lexer.token == Symbol.NUMBER || lexer.token == Symbol.LITERALSTRING ||
                lexer.token == Symbol.THIS||lexer.token == Symbol.SUPER ||
                lexer.token == Symbol.LEFTPAR ||lexer.token == Symbol.NOT ||
                lexer.token == Symbol.NULL ||lexer.token == Symbol.NEW ||
                lexer.token == Symbol.IDENT){
                   exprList = exprList();
                }
            if(lexer.token != Symbol.RIGHTPAR)
                error.show(") expected");
            lexer.nextToken();
            return new MessageSendStatement(RM,id,exprList);
               }
            }
            //lexer.nextToken();            
            if(lexer.token != Symbol.DOT)
                error.show(". expected");            
            lexer.nextToken();
            if(lexer.token != Symbol.IDENT)
                error.show("ident expected");
            id = id(lexer.getStringValue());
            lexer.nextToken();
            if(lexer.token != Symbol.LEFTPAR)
                error.show("( expected");
            lexer.nextToken();
                if(lexer.token == Symbol.PLUS || lexer.token == Symbol.MINUS||
                lexer.token == Symbol.TRUE ||lexer.token == Symbol.FALSE ||
                lexer.token == Symbol.NUMBER || lexer.token == Symbol.LITERALSTRING ||
                lexer.token == Symbol.THIS||lexer.token == Symbol.SUPER ||
                lexer.token == Symbol.LEFTPAR ||lexer.token == Symbol.NOT ||
                lexer.token == Symbol.NULL ||lexer.token == Symbol.NEW ||
                lexer.token == Symbol.IDENT){
                   exprList = exprList();
                }
            if(lexer.token != Symbol.RIGHTPAR)
                error.show(") expected");
            lexer.nextToken();
            return new MessageSendStatement(RM,id,exprList);

   }



   private WhileStat whileStatement() {
      lexer.nextToken();
      if ( lexer.token != Symbol.LEFTPAR )
         error.show("( expected");
      lexer.nextToken();
      Expression expr = expr();
      if ( lexer.token != Symbol.RIGHTPAR )
         error.show(") expected");
      lexer.nextToken();
      Statement st = statement();
      return new WhileStat(expr,st);
   }

   private IfStat ifStatement() {
      lexer.nextToken();
      if ( lexer.token != Symbol.LEFTPAR )
         error.show("( expected");
      lexer.nextToken();
      Expression expr = expr();
      if ( lexer.token != Symbol.RIGHTPAR )
         error.show(") expected");
      lexer.nextToken();
      Statement st = statement();
      Statement elseSt = null;
      if ( lexer.token == Symbol.ELSE ) {
         lexer.nextToken();
         elseSt = statement();
      }
      return new IfStat(expr, st,elseSt);
   }



   private ReadStat readStatement() {
      lexer.nextToken();
      if ( lexer.token != Symbol.LEFTPAR )
         error.show("( expected");
      lexer.nextToken();       
      LeftValue lf = leftValue();       
      ArrayList<LeftValue> lfList = new ArrayList<LeftValue>();
      while ( lexer.token == Symbol.COMMA ) {
          lexer.nextToken();
          lfList.add(leftValue());          
      }

      if ( lexer.token != Symbol.RIGHTPAR )
         error.show(") expected");
      lexer.nextToken();
      return new ReadStat(lf, lfList);
   }

   private LeftValue leftValue(){
       boolean hasThis = false;
       Id id2 = null;
       if(lexer.token == Symbol.THIS){
           hasThis = true;
           lexer.nextToken();
           if(lexer.token != Symbol.DOT)
               error.show(". expected");
           lexer.nextToken();
       }

       if(lexer.token != Symbol.IDENT){            
          error.show("ident expected");           
       }
       Id id = id(lexer.getStringValue());
       lexer.nextToken();
       if(lexer.token == Symbol.IDENT){
           id2 = id(lexer.getStringValue());
           lexer.nextToken();
       }
       return new LeftValue(id, id2, hasThis);
   }

   private WriteStat writeStatement() {
      lexer.nextToken();
      if ( lexer.token != Symbol.LEFTPAR )
         error.show("( expected");
      lexer.nextToken();
      ExpressionList exprList = exprList();
      if ( lexer.token != Symbol.RIGHTPAR ){
          error.show(") expected");
      }
      lexer.nextToken();
      return new WriteStat(exprList);
   }


   private BreakStat breakStatement() {
      lexer.nextToken();
      if ( lexer.token != Symbol.SEMICOLON )
         error.show(CompilerError.semicolon_expected);
      lexer.nextToken();
      return new BreakStat();
   }


   private NullStatement nullStatement() {
      lexer.nextToken();
      return new NullStatement();
   }


   private ExpressionList exprList() {
      // ExpressionList ::= Expression { "," Expression }
      Expression expr = expr();      
      ArrayList<Expression> exprList = new ArrayList<Expression>();
      while ( lexer.token == Symbol.COMMA ) {
         lexer.nextToken();
         exprList.add(expr());
      }
      return new ExpressionList(expr,exprList);
   }



   private Expression expr() {

      SimpleExpression left = simpleExpr();
      SimpleExpression right = null;
      Relation relation = null;
      int op = lexer.token;
      if ( op == Symbol.EQ || op == Symbol.NEQ ||
           op == Symbol.LE || op == Symbol.LT ||
           op == Symbol.GE || op == Symbol.GT  ) {
         lexer.nextToken();

         relation = new Relation(op);
         right = simpleExpr();
      }
      return new Expression(left,relation,right);
   }

   private SimpleExpression simpleExpr() {
      int op;

      Term left = term();
      ArrayList<LowOperator> lowOpList = new ArrayList<LowOperator>();
      ArrayList<Term> termList = new ArrayList<Term>();
      while ( (op = lexer.token) == Symbol.MINUS || op == Symbol.PLUS ||
              op == Symbol.OR ) {

          lowOpList.add(new LowOperator(op));
         lexer.nextToken();
         Term right = term();
         termList.add(right);
      }
      return new SimpleExpression(left, lowOpList, termList);
   }


   private Term term() {
      int op;
      SignalFactor left = signalFactor();
      ArrayList<HighOperator> hoList = new ArrayList<HighOperator>();
      ArrayList<SignalFactor> sfList = new ArrayList<SignalFactor>();

      while ( (op = lexer.token) == Symbol.DIV || op == Symbol.MULT ||
              op == Symbol.AND ) {
         lexer.nextToken();

         hoList.add(new HighOperator(op));
         sfList.add(signalFactor());
         
      }
      return new Term(left,hoList,sfList);
   }


   private SignalFactor signalFactor() {

      int op = -1;
      if ( (op = lexer.token) == Symbol.PLUS || op == Symbol.MINUS ) {

         lexer.nextToken();       
      }
      Factor f = factor();
        return new SignalFactor(op, f);
   }



    private Factor factor() {
      /*
        Factor ::= BasicValue | RightValue | MessageSend  | "(" Expression ")"
           | "!" Factor | "null" | ObjectCreation
        BasicValue ::= IntValue | BooleanValue | StringValue
        BooleanValue ::= "true" | "false"
        RightValue ::= "this" [ "." Id ] | Id
        MessageSend ::= ReceiverMessage "." Id "("  [ ExpressionList ] ")"
        ReceiverMessage ::=  "super" | Id | "this" | "this" "."  Id
        ObjectCreation ::= ``new" Id ``("  ``)"
      */
      Factor e;
      Id id = null;     
      //MethodDec aMethod;
      switch ( lexer.token ) {
         case Symbol.TRUE :
           lexer.nextToken();
           return new BooleanValue(Symbol.TRUE);
         case Symbol.FALSE :
           lexer.nextToken();
           return new BooleanValue(Symbol.FALSE);
         case Symbol.NUMBER :
             e = new IntValue(String.valueOf(lexer.getNumberValue()));
             lexer.nextToken();
             return e;
         case Symbol.LITERALSTRING :
             e = new StringValue("\""+lexer.getLiteralStringValue()+"\"");
             lexer.nextToken();
           return e;
          case Symbol.THIS :
              lexer.nextToken();
              if(lexer.token == Symbol.DOT){
                  lexer.nextToken();
                  if(lexer.token == Symbol.IDENT){                       
                      id = id(lexer.getStringValue());
                      lexer.nextToken();
                      if(lexer.token == Symbol.DOT || lexer.token == Symbol.LEFTPAR){
                        return messageSend("this",id);
                      }else
                          return new RightValue(null,id);
                  
                  }
              }
             // lexer.nextToken();
           return new RightValue(null,null);
         case Symbol.IDENT :
            Id id2 = null;
            String str;
            str = lexer.getStringValue();
            
            lexer.nextToken();
            
            if(lexer.token == Symbol.DOT){
                lexer.nextToken();
                if(lexer.token != Symbol.IDENT)
                    error.show("identifier expected");
                id2 = id(lexer.getStringValue());
                lexer.nextToken();
                if(lexer.token == Symbol.LEFTPAR)
                    return messageSend(str, id2);
                else
                    return new RightValue(id(lexer.getStringValue()),id2);
            }
 else{
                id = id(str);
                
                 return new RightValue(id,id2);
             }

                /*
                  lexer.nextToken();
                  if(lexer.token == Symbol.IDENT){
                      id2 = id(lexer.getStringValue());
                      lexer.nextToken();
                      if(lexer.token == Symbol.LEFTPAR){

                        lexer.nextToken();
                            Expression expr;
                        if(lexer.token != Symbol.RIGHTPAR )
                            expr = expr();
                        else{                                
                            expr = null;
                            lexer.nextToken();
                          }
                            System.out.println("aqeq: "+lexer.token);
                          return new ExpressionFactor(expr);
                      }
                }
              }
            return new RightValue(id,id2);*/
         case Symbol.SUPER :
             lexer.nextToken();
             if(lexer.token != Symbol.DOT)
                 error.show(". expected");
             lexer.nextToken();
             if(lexer.token != Symbol.IDENT)
                 error.show("ident expected");
             id = id(lexer.getStringValue());
             lexer.nextToken();
            return messageSend("super",id);
         case Symbol.LEFTPAR :
            lexer.nextToken();
            Expression expr = expr();
            if ( lexer.token != Symbol.RIGHTPAR )
               error.show(") expected");
            lexer.nextToken();
            return new ParenthesisExpr(expr);
          case Symbol.NOT :
              Factor f = factor();
              lexer.nextToken();
              return new NotFactor(f);
         case Symbol.NULL :
            lexer.nextToken();
            return new NullFactor();
         case Symbol.NEW :
            lexer.nextToken();
            if ( lexer.token != Symbol.IDENT ){
               error.show("Identifier expected");
            }
            id = id(lexer.getStringValue());
            ClassDec aClass = symbolTable.getInGlobal(id.getName());

            if ( aClass == null){
                error.show("Cannot find symbol ");
            }
            lexer.nextToken();
            if ( lexer.token != Symbol.LEFTPAR )
               error.show("( expected");
            lexer.nextToken();
            if ( lexer.token != Symbol.RIGHTPAR )
               error.show(") expected");
            lexer.nextToken();
            /* return an object representing the creation of an object
              something as
            return new Cria_um_objeto(aClass);
            é importante não utilizar className, uma string e sim aClass, um objeto.
            */
            return new ObjectCreation(id);
   
               default :
                  error.show(CompilerError.identifier_expected);
               }
               return null;
         }

   





   private boolean startExpr( int aToken ) {

      return
             lexer.token == Symbol.FALSE ||
             lexer.token == Symbol.TRUE ||
             lexer.token == Symbol.NOT ||
             lexer.token == Symbol.THIS ||
             lexer.token == Symbol.NUMBER ||
             lexer.token == Symbol.SUPER ||
             lexer.token == Symbol.LEFTPAR ||
             lexer.token == Symbol.NULL ||
             lexer.token == Symbol.IDENT ||
             lexer.token == Symbol.LITERALSTRING;

   }


   private SymbolTable symbolTable;
   private Lexer lexer;
   private CompilerError error;

    private Id id(String name) {
        if(!Character.isLetter(name.charAt(0))){
            if(name.charAt(0) == '_')
                error.show("_ cannot start an indentifier");//arrumar o erro
        }
        for(int i = 0; i<name.length(); i++){
            if(!Character.isLetterOrDigit(name.charAt(i)) && name.charAt(i)!='_'){
                error.show("Wrong type");//arrumar o erro
            }
        }
        //symbolTable.putInGlobal(name, null);
        return new Id(name);
    }

}
