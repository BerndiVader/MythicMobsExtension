package com.gmail.berndivader.mythicmobsext.jboolexpr;
import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleUnaryOperator;

@FunctionalInterface
interface Expression {
	double eval();
}

public class MathInterpreter {
	private static final Map<String,DoubleUnaryOperator> functions = new HashMap<>();
	Map<String,Double> variables=new HashMap<>();
	
	static {
	    functions.put("sqrt", x -> Math.sqrt(x));
	    functions.put("sin", x -> Math.sin(Math.toRadians(x)));
	    functions.put("cos", x -> Math.cos(Math.toRadians(x)));
	    functions.put("tan", x -> Math.tan(Math.toRadians(x)));
	}	

	static {
	}

	public static Expression parse(String str, Map<String,Double> variables) {
		return new Object() {
			int pos = -1, ch;
			//if check pos+1 is smaller than string length ch is char at new pos
			void nextChar() {
				ch = (++pos < str.length()) ? str.charAt(pos) : -1;
			}

			//skips 'spaces' and if current char is what was searched, if true move to next char return true
			//else return false
			boolean eat(int charToEat) {
				while (ch == ' ') nextChar();
				if (ch == charToEat) {
					nextChar();
					return true;
				}
				return false;
			}


			Expression parse() {
				nextChar();
				Expression x = parseExpression();
				if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
				return x;
			}

			// Grammar:
			// expression = term | expression `+` term | expression `-` term
			// term = factor | term `*` factor | term `/` factor
			// factor = `+` factor | `-` factor | `(` expression `)`
			//        | number | functionName factor | factor `^` factor

			Expression parseExpression() {
				Expression x = parseTerm();
				for (;;) {
					if (eat('+')) { // addition
						Expression a = x, b = parseTerm();                      
						x = (() -> a.eval() + b.eval());
					} else if (eat('-')) { // subtraction
						Expression a = x, b = parseTerm();
						x = (() -> a.eval() - b.eval());
					} else {
						return x;
					}
				}
			}

			Expression parseTerm() {
				Expression x = parseFactor();
				for (;;) {
					if (eat('*')){
						Expression a = x, b = parseFactor(); // multiplication
						x = (() -> a.eval() * b.eval());
					}
					else if(eat('/')){
						Expression a = x, b = parseFactor(); // division
						x = (() -> a.eval() / b.eval());
					}
					else return x;
				}
			}

			Expression parseFactor() {
				if (eat('+')) return parseFactor(); // unary plus
				if (eat('-')){
					Expression b = parseFactor(); // unary minus
					return (() -> -1 * b.eval());
				}

				Expression x;
				int startPos = this.pos;
				if (eat('(')) { // parentheses
					x = parseExpression();
					eat(')');
				} else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
					while ((ch >= '0' && ch <= '9') || ch == '.'){                     
						nextChar();
					}
					double xx = Double.parseDouble(str.substring(startPos, this.pos));
					x = () -> xx;
			    } else if (ch >= 'a' && ch <= 'z') { // functions and variables
			        while (ch >= 'a' && ch <= 'z') nextChar();
			        String name = str.substring(startPos, this.pos);
			        if (functions.containsKey(name)) {
			            DoubleUnaryOperator func = functions.get(name);
			            Expression arg = parseFactor();
			            x = () -> func.applyAsDouble(arg.eval());
			        } else {
			            x = () -> variables.get(name);
			        }
			    } else {					throw new RuntimeException("Unexpected: " + (char)ch);
				}

				if (eat('^')){ 
					x = () -> {
						double d =  parseFactor().eval();
						return Math.pow(d,d); // exponentiation
					};
				}
				return  x;
			}
		}.parse();
	}
}