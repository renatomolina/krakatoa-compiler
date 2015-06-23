/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package AST;

/**
 *
 * @author 317276
 */
public class ExpressionFactor extends Factor{
    private Expression expr;
    public ExpressionFactor(Expression expr){
        this.expr = expr;
    }

    public void genK(PW pw, boolean putParenthesis) {
        pw.print("(");
        if(expr!=null)
            expr.genK(pw, putParenthesis);
        pw.print(")");
    }

}
