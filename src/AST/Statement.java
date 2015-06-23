/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package AST;

/**
 *
 * @author 317276
 */
abstract public class Statement {
    abstract public void genK(PW pw, boolean putParenthesis);
}
