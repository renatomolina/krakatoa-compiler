package AST;

import java.util.ArrayList;

public class IdList {
    private Id id;
    private ArrayList<Id> listId;

    public IdList(Id id, ArrayList<Id> listId){
        this.id = id;
        this.listId = listId;
    }

    public void genK(PW pw, boolean putParenthesis){
          id.genK(pw, putParenthesis);
          
          for( Id i : listId ){
            pw.print(",");
            i.genK(pw, putParenthesis);
          }
    }
}
