package AST;

public class IfStat extends Statement{

    private Expression expr;
    private Statement statement;
    private Statement statementelse;

    public IfStat(Expression expr, Statement st, Statement elseSt){
        this.expr = expr;
        this.statement = st;
        this.statementelse = elseSt;
    }

    public void genK(PW pw, boolean putParenthesis){
        pw.add();
        pw.printIdent("if ");
        pw.print("( ");
        expr.genK(pw, putParenthesis);
        if(statement.getClass().getName().equals("AST.CompStatement"))
            pw.print(" ) ");
        else
            pw.println(" ) ");
        statement.genK(pw, putParenthesis);        
        if(statementelse!= null){
            pw.printlnIdent("else");
            statementelse.genK(pw, putParenthesis);            
        }
        pw.sub();
    }
}
