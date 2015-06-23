package AST;

import java.io.*;

abstract public class BasicValue extends Factor {
    public BasicValue(String name){
        this.name = name;
    }
    abstract public String getKname();
    String name;
}
