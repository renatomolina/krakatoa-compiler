package AST;

import java.util.ArrayList;

public class IntValue extends BasicValue{
    String value;
    /*
    Digit digit;
    ArrayList<Digit> listDigit;
    public IntValue(Digit digit,ArrayList<Digit> listDigit ){
        this.digit = digit;
        this.listDigit = listDigit;
    }
    public void genK(PW pw, boolean putParenthesis){
        digit.genK(pw, putParenthesis);
        for(Digit d:listDigit){
            d.genK(pw, putParenthesis);
        }
    }*/
    public IntValue(String value){
        super("int");
        this.value = value;
    }

    public void genK(PW pw, boolean putParenthesis){
        pw.print(value);
    }

    public String getKname() {
        return "int";
    }
}
