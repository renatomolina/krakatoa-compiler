package AST;

public class HighOperator {

    int hOp;

    public HighOperator(int hOp){
        this.hOp = hOp;
    }

    public void genK(PW pw, boolean putParenthesis){
        if(hOp == 6)
            pw.print("/");
        else if(hOp == 5)
            pw.print("*");
        else
            pw.print("&&");
    }

}
