package AST;

public class InstVarDec extends Member{

    boolean staticFlag;
    Type type;
    IdList idList;


    public InstVarDec(boolean staticFlag, Type type, IdList idList){
        this.staticFlag = staticFlag;
        this.type = type;
        this.idList = idList;
    }

    public void genK(PW pw, boolean putParenthesis){
        if(staticFlag){
            pw.print("static");
        }
        pw.print("private");
        type.genK(pw, putParenthesis);
        idList.genK(pw, putParenthesis);
        pw.println(";");
        
    }

    public StatementList getList() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
