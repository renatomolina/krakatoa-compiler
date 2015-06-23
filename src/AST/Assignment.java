/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package AST;

/**
 *
 * @author 317276
 */
public class Assignment extends Statement{
    public Assignment(LeftValue lf, Expression expr, boolean hasSemicolon){
        this.lf = lf;
        this.expr = expr;
        this.hasSemicolon = hasSemicolon;
    }
    public void genK(PW pw, boolean putParenthesis){
        lf.genK(pw, false);
        pw.print("=");
        expr.genK(pw, putParenthesis);
        if(hasSemicolon)
            pw.println(";");
    }

    private LeftValue lf;
    private Expression expr;
    private boolean hasSemicolon;
}
