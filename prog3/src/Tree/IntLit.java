// IntLit -- Parse tree node class for representing integer literals

package Tree;

public class IntLit extends Node {
    private int intVal;

    public IntLit(int i) {
        intVal = i;
    }

    public void print(int n) {
        for (int i = 0; i < n; i++)
            System.out.print(" ");
        System.out.println(intVal);
    }
    public boolean isNumber(){
        return true;
    }
    public int getNodeInt(){
        return intVal;
    }

    public Node eval(Environment env) {
    	return this;
    }
    public String toString() {
    	return String.valueOf(intVal);
    }
    public boolean isInteger() {
    	return true;
    }
    public Node getNumerator() {
    	return this;
    }
    public Node getDenominator() {
    	return new IntLit(1);
    }
    public boolean isZero() {
    	return intVal==0;
    }
    public boolean isPositive() {
    	return intVal>0;
    }
    public boolean isNegative() {
    	return intVal<0;
    }
}