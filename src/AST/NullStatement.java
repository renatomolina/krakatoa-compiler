/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package AST;

/**
 *
 * @author 317276
 */
public class NullStatement extends Statement {
    public void genK(PW pw, boolean putParenthesis){
        pw.println(";");
    }
}
