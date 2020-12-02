package interpreter.expressions;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;


public class ShuntingYard {
	
    public static Double calculateExpression(List<String> expression) {
        return evaluatePostfix(calculatePostfix(expression));
    }
    
    
	private static String[] calculatePostfix(List<String> expressionList) {
		String[] expression = new String[expressionList.size()];
		expression = expressionList.toArray(expression);
    	Queue<String> queue = new LinkedList<String>();
		Stack<String> stack = new Stack<String>();
    	for (String s : expression){
			if (isANumber(s)){
				queue.add(s);
			}
			else{
				switch(s) {
			    case "/":
			    case "*":
			    case "(":
			        stack.push(s);
			        break;
			    case "+":
			    case "-":
			    	while (!stack.empty() && (!stack.peek().equals("("))){
			    		queue.add(stack.pop());
			    	}
			        stack.push(s);
			        break;
			    case ")":
			    	while (!stack.peek().equals("(")){
			    		queue.add(stack.pop());
			    	}
			    	stack.pop();
			        break;
				}
			}
		}
    	while(!stack.isEmpty()){
			queue.add(stack.pop());
    	}
    	String[] retArr=new String[queue.size()];
    	for (int i = 0; i < retArr.length; i++) {
			retArr[i]=queue.poll();
		}
    	return retArr;
    }
	
	
	private static Double evaluatePostfix(String[] exp) {
        //create a stack
        Stack<Expression> stack = new Stack<>();

        // Scan all characters one by one
        for (String s : exp) {
            // If the scanned character is an operand (number here),
            // push it to the stack.
            if (isANumber(s))
                stack.push(new Number(Double.parseDouble(s)));

                //  If the scanned character is an operator, pop two
                //  elements from stack apply the operator
            else {
                double val1 = stack.pop().calculate();
                double val2 = stack.pop().calculate();

                switch (s) {
                    case "+":
                        stack.push(new Plus(new Number(val2), new Number(val1)));
                        break;

                    case "-":
                        stack.push(new Minus(new Number(val2), new Number(val1)));
                        break;

                    case "/":
                        stack.push(new Divide(new Number(val2), new Number(val1)));
                        break;

                    case "*":
                        stack.push(new Multiply(new Number(val2), new Number(val1)));
                        break;
                }
            }
        }
        return stack.pop().calculate();
    }
	
	
	private static boolean isANumber(String word) {
        try {
            Double.parseDouble(word);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
