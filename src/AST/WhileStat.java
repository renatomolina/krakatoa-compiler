package AST;

import java.util.ArrayList;

public class WhileStat extends Statement {

     Expression exp;
     Statement st;

    public WhileStat(Expression exp, Statement st){
        this.exp = exp;
        this.st = st;
    }

     public void genK(PW pw, boolean putParenthesis){
         pw.add();
         pw.printIdent("while");
         pw.print("(");
         exp.genK(pw, putParenthesis);
         pw.print(")");
         pw.println("");
         st.genK(pw, putParenthesis);         
         pw.sub();
    }
}
