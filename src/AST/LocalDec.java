package AST;

public class LocalDec extends Statement{
    Type type;
    IdList idList;

    public LocalDec(Type type, IdList idList){
        this.type = type;
        this.idList = idList;
    }

    public void genK(PW pw, boolean putParenthesis){
        type.genK(pw, putParenthesis);
        idList.genK(pw, putParenthesis);
        pw.println(";");
    }
}
