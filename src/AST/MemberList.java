package AST;

import java.util.ArrayList;

public class MemberList {
    ArrayList<Member> mList;

    public MemberList(ArrayList<Member> mList){
        this.mList = mList;
    }

    public void genK(PW pw, boolean putParenthesis){
        pw.add();
        for( Member m : mList ){
            m.genK(pw, putParenthesis);
        }
        pw.sub();
    }
}
