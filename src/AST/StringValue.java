package AST;

import java.util.ArrayList;

public class StringValue extends BasicValue{
    String value;

    public StringValue(String value){
        super("String");
        this.value = value;
    }

    public void genK(PW pw, boolean putParenthesis){
        pw.print(value);
    }

    public String getKname() {
        return "String";
    }
}
