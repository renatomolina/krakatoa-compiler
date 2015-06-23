package AST;

import java.util.ArrayList;

public class WriteStat extends Statement{

     ExpressionList expList;

    public WriteStat(ExpressionList expList){
        this.expList = expList;
   }

     public void genK(PW pw, boolean putParenthesis){
         pw.add();
         pw.printIdent("write");
         pw.print("(");
         expList.genK(pw, putParenthesis);
         pw.print(")");
         pw.sub();
    }
}
