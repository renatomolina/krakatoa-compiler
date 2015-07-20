# krakatoa_compiler

This is my own compiler from a small language called [Krakatoa](http://www.cyan-lang.org/jose/courses/06-2/lc/The%20Krakatoa%20Language.pdf). The Krakatoa language is a subset of Java with a few additions/modifications. Krakatoa supports all the basic concepts of object-oriented programming such as classes, inheritance, and polymorphism.

Here is a simple piece of code of krakatoa

```Java
class A {
  private int i;      
  public int get() {
    return this.i;
  }
  
  public void put(int i) {
    this.i=i;
  }
}

class Program{
  public void run() {
    A object;
    int k;
    object = new A();
    object.put(5);         
    k = ();
    write(k);
  }
}
```
