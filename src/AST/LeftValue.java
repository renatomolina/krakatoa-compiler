package AST;

public class LeftValue {

    Id id;
    Id id2;
    boolean hasThis;

    public LeftValue(Id id, Id id2, boolean hasThis){
        this.id = id;
        this.id2 = id2;
        this.hasThis = hasThis;
    }

    public void genK(PW pw, boolean putParenthesis){
        if(hasThis){
        pw.printIdent("this.");
        }else{
            if(id2!=null)
                id2.genK(pw, putParenthesis);
        }
        id.genK(pw, putParenthesis);
    }

}
