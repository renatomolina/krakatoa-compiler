package AST;

public class ReturnStat extends Statement{
    Expression expr;

    public ReturnStat(Expression expr){
        this.expr = expr;
    }

    public void genK(PW pw, boolean putParenthesis){
        pw.add();
        pw.printIdent("return ");
        expr.genK(pw, putParenthesis);
        pw.sub();
    }
}
