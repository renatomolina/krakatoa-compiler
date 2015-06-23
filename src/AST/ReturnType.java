package AST;

public class ReturnType  extends Type{
    private Type type;
    public ReturnType(Type type){
        super("");
        this.type = type;
    }

    public void genK(PW pw, boolean putParenthesis){
        if(type!=null){
            type.genK(pw, putParenthesis);
        }
        else{
            pw.print("void ");
        }
    }


    public Type getType() {
        if(type == null)
            return null;
        else
        return type.getType();
}
}
