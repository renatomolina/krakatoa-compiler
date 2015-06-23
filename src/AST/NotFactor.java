package AST;

public class NotFactor extends Factor{
    private Factor factor;
    public NotFactor(Factor factor){
        this.factor = factor;
    }

    public void genK(PW pw, boolean putParenthesis) {
        pw.print("!");

    }

}
