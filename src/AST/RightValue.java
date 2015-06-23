
package AST;

public class RightValue extends Factor{
private Id id;
private Id id2;

public RightValue(Id id, Id id2){
    this.id = id;
    this.id2 = id2;
}
public void genK(PW pw, boolean putParenthesis){
    if(id==null){
        pw.print("this");
    }
    else{
        id.genK(pw, putParenthesis);
    }
        if(id2 != null){
                        pw.print(".");
            id2.genK(pw, putParenthesis);
        }
}
}
