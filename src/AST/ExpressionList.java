package AST;

import java.util.ArrayList;

public class ExpressionList {

    Expression expression;
    ArrayList<Expression> listExpr;

    public ExpressionList(Expression expr, ArrayList<Expression> listExpr){
        this.expression = expr;
        this.listExpr = listExpr;
    }

    public void genK(PW pw, boolean putParenthesis){
        expression.genK(pw, putParenthesis);
        for(Expression exp : listExpr){
            pw.print(",");
            exp.genK(pw, putParenthesis);
        }
    }
}
