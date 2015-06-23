package AST;

import java.util.ArrayList;

public class StatementList {
    private ArrayList<Statement> sList;
    private boolean retorno;
    public StatementList(ArrayList<Statement> sList, boolean retorno){
        this.sList = sList;
        this.retorno = retorno;
    }
    public ArrayList<Statement> getList(){
        return sList;
    }
    public boolean getRetorno(){
        return retorno;
    }

    public void genK(PW pw, boolean putParenthesis){
        for(Statement s: sList){
            s.genK(pw, putParenthesis);
        }
    }

}
