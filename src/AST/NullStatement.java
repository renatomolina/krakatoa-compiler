package AST;

public class NullStatement extends Statement {
    public void genK(PW pw, boolean putParenthesis){
        pw.println(";");
    }
}
