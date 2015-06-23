package AST;

import java.io.*;

public class BasicType extends Type {
    int type;

    public BasicType(int type) {
        super("");
        this.type = type;
    }

public void genK(PW pw, boolean putParenthesis){
        if(type == 30)
        pw.print("int ");
        else if(type == 31)
            pw.print("boolean ");
        else if(type == 42)
            pw.print("String ");
}

    private String name;

    public Type getType() {
        return this;
    }
}
