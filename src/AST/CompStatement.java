package AST;

import java.util.ArrayList;

public class CompStatement extends Statement {

    StatementList stList;

    public CompStatement(StatementList stList){
        this.stList = stList;
    }

    public void genK(PW pw, boolean putParenthesis){
        pw.println("{");
        for (Statement st: stList.getList()){
            st.genK(pw, putParenthesis);
        }
        pw.printlnIdent("}");
    }
}
