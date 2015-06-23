package AST;

public class MessageSendStatement extends Statement {
    ReceiverMessage rMsg;
    Id id;
    ExpressionList exprList;

    public MessageSendStatement(ReceiverMessage rMsg, Id id, ExpressionList exprList){
        this.rMsg  = rMsg;
        this.id = id;
        this.exprList = exprList;
    }

    public void genK(PW pw, boolean putParenthesis){
        rMsg.genK(pw, putParenthesis);
        pw.print(".");
        id.genK(pw, putParenthesis);
        pw.print("(");
        if(exprList != null){
            exprList.genK(pw, putParenthesis);
        }
        pw.print(")");
        pw.print(";");
    }
}
