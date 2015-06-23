package AST;


public class ParenthesisExpr extends Factor{
    Expression expr;
    public ParenthesisExpr(Expression expr){
        this.expr = expr;
    }
    public void genK(PW pw, boolean putParenthesis){
        pw.print("(");
        expr.genK(pw, putParenthesis);
        pw.print(")");
    }
}
