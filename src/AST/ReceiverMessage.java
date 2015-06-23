package AST;

public class ReceiverMessage {
    private Id id;
    String str;
    public ReceiverMessage(Id id, String str){
        this.id = id;
        this.str = str;
    }

    public void genK(PW pw, boolean putParenthesis){
        if(id!= null){
            if(str!=null){
                pw.print(str + ".");
            }
                id.genK(pw, putParenthesis);
        }else{
            pw.print(str);
        }

    }


}
