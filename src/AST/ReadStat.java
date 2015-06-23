package AST;

import java.util.ArrayList;

public class ReadStat extends Statement{

     LeftValue lf;
     ArrayList<LeftValue> lfList;

     public ReadStat(LeftValue lf,ArrayList<LeftValue> lfList ){
        this.lf = lf;
        this.lfList = lfList;
     }

     public void genK(PW pw, boolean putParenthesis){
         pw.add();
         pw.printIdent("read");
         pw.print("(");         
         lf.genK(pw, putParenthesis);      
         for(LeftValue lv : lfList){
             pw.print(",");
             lv.genK(pw, putParenthesis);
         }
         pw.print(")");
         pw.sub();
    }
}
