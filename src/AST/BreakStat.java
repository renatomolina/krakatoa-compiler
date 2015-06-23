
package AST;


public class BreakStat extends Statement{
    public void genK(PW pw, boolean putParenthesis){
        pw.add();
        pw.printlnIdent("break;");
        pw.sub();
    }
}
