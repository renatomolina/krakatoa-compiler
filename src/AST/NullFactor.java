package AST;

public class NullFactor extends Factor{
    public void genK(PW pw, boolean putParenthesis) {
        pw.print("null");
    }

}
