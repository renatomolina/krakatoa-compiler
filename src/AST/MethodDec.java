package AST;

public class MethodDec extends Member{
    Qualifier qualifier;
    ReturnType returnType;
    Id id;
    FormalParamDec fParamDec;
    StatementList sList;

    public MethodDec(Qualifier qualifier, ReturnType returnType, Id id, FormalParamDec fParamDec, StatementList sList){
        this.qualifier = qualifier;
        this.returnType = returnType;
        this.id = id;
        this.fParamDec = fParamDec;
        this.sList = sList;
    }
    public StatementList getList(){
        return sList;
    }

    public void genK(PW pw, boolean putParenthesis){
        pw.add();
        qualifier.genK(pw, putParenthesis);
        returnType.genK(pw, putParenthesis);
        id.genK(pw, putParenthesis);
        pw.print("(");
        if(fParamDec != null)
            fParamDec.genK(pw, putParenthesis);
        pw.print(") ");
        pw.println("{");        
        sList.genK(pw, putParenthesis);        
        pw.printlnIdent("}");
        pw.sub();
    }
}
