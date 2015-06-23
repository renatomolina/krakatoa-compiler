package AST;

public class ObjectCreation extends Factor{

    private Id id;

    public ObjectCreation(Id id){
        this.id = id;
    }

    public void genK(PW pw, boolean putParenthesis){
        pw.print("new");
        id.genK(pw, putParenthesis);
        pw.print("(");
        pw.print(")");
    }
}
