package AST;

import java.io.*;

public class BooleanValue extends BasicValue {
    int value;
   public BooleanValue(int value)
   {
       super("bool");
       this.value = value;
   }

   public String getKname() {
      return "boolean";
   }
    public void genK(PW pw, boolean putParenthesis) {
        pw.add();
        if(value == 7)
            pw.print("true");
        else
            pw.print("false");
        pw.sub();
    }

}
