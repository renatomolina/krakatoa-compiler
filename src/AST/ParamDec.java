package AST;

public class ParamDec {
    Type type;
    Id id;

    public ParamDec(Type type, Id id){
        this.type = type;
        this.id = id;
    }

    public void genK(PW pw, boolean putParenthesis){
        type.genK(pw, putParenthesis);
        id.genK(pw, putParenthesis);
    }
}