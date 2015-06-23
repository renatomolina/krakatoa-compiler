package AST;

public class LowOperator {
    int lOp;

    public LowOperator(int lOp){
        this.lOp = lOp;
    }

    public void genK(PW pw, boolean putParenthesis){
        if(lOp == 4)
        pw.print("-");
        else if(lOp == 3)
            pw.print("+");
        else
            pw.print("||");
    }
}
