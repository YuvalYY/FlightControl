package interpreter.lexer;

import java.util.LinkedList;
import java.util.List;

public class Lexer {
	
	private boolean inTheMiddleOfAWhileLoop;
    private StringBuilder whileBlock;

    public Lexer() {}
    
	public String[][] lexer(String line)
	{
		return lexer(new String[] {line});
	}
	
	public String[][] lexer(String[] lines)
	{
		List<String[]> tokens=new LinkedList<String[]>();
		for (int i = 0; i < lines.length; i++) {
			String[] pr=processLine(preprocessLine(lines[i]));
			if(pr!=null) {
				tokens.add(pr);
			}
		}
		String[][] returnArr = new String[tokens.size()][];
		returnArr = tokens.toArray(returnArr);
		return returnArr;
	}

	 public String preprocessLine(String line){
		 String[] splitStrings=line.split("\"");
		 StringBuilder res=new StringBuilder();
		 for(int i=0;i<splitStrings.length;i++) {
			 if(i%2==0) {
				    splitStrings[i] = splitStrings[i].replaceAll("\t", "");
				    splitStrings[i] = splitStrings[i].replaceAll("\\(", " ( ");
				    splitStrings[i] = splitStrings[i].replaceAll("\\)", " ) ");
				    splitStrings[i] = splitStrings[i].replaceAll("\\{", " {");
				    splitStrings[i] = splitStrings[i].replaceAll("\\}", " }");
			    	splitStrings[i] = splitStrings[i].replaceAll("\\+", " + ");
			    	splitStrings[i] = splitStrings[i].replaceAll("\\-", " - ");
			    	splitStrings[i] = splitStrings[i].replaceAll("\\/", " / ");
			    	splitStrings[i] = splitStrings[i].replaceAll("\\*", " * ");
			    	splitStrings[i] = splitStrings[i].replaceAll("\\=", " = ");
			    	
			    	splitStrings[i]=splitStrings[i].replaceAll(">\\s+=", " >= ");
			    	splitStrings[i]=splitStrings[i].replaceAll("<\\s+=", " <= ");
			    	splitStrings[i]=splitStrings[i].replaceAll("!\\s+=", " != ");
			    	splitStrings[i]=splitStrings[i].replaceAll("=\\s+=", " == ");
			    	
			 }
			 else {
				 splitStrings[i]="\""+splitStrings[i]+"\"";
			 }
			 res.append(splitStrings[i]);
		 }
	    	
	    	return res.toString();
	    }
	 
	 public String[] processLine(String line)
	    {
	    	if (inTheMiddleOfAWhileLoop) {
	            // we are in a while loop block
	            whileBlock.append(line).append(" \n ");

	            if (line.endsWith("}")){
	                // end of a while loop
	                inTheMiddleOfAWhileLoop = false;
	                String[] whileBlockAsStringArray = whileBlockAsStringArray();

	                // allocate a new
	                whileBlock = new StringBuilder();
	                return whileBlockAsStringArray;
	            }
	        } else if (line.startsWith("while")) {
	            // start of a new while block
	        	whileBlock=new StringBuilder("");
	            inTheMiddleOfAWhileLoop = true;
	            whileBlock.append(line).append(" \n ");
	        }

	        if(inTheMiddleOfAWhileLoop){
	        	return null; //don't add anything since we are in the middle of the loop
	        }
	        else {
	        	return line.split("[ \t]+");
			}
	    }
	    
	    private String[] whileBlockAsStringArray() {
	        return whileBlock.toString().split("[ \t]+");
	    }
}
