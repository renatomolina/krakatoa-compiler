/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package AST;

import java.util.ArrayList;

/**
 *
 * @author 317276
 */
public class Term {
    private SignalFactor sf;
    private ArrayList<HighOperator> hoList;
    private ArrayList<SignalFactor> sfList;

    public Term(SignalFactor sf, ArrayList<HighOperator> hoList,ArrayList<SignalFactor> sfList){
        this.sf = sf;
        this.hoList = hoList;
        this.sfList = sfList;
    }

public void genK(PW pw, boolean putParenthesis){
    sf.genK(pw, putParenthesis);
    int s = hoList.size();
    for(int i = 0; i <s; i++){
        hoList.get(i).genK(pw, putParenthesis);
        sfList.get(i).genK(pw, putParenthesis);
    }
}

}
