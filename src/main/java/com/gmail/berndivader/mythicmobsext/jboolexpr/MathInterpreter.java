package com.gmail.berndivader.mythicmobsext.jboolexpr;

import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleUnaryOperator;

public class MathInterpreter {
	private static final Map<String,DoubleUnaryOperator>functions=new HashMap<>();
	Map<String,Object>variables=new HashMap<>();
	
	static {
	    functions.put("sqrt",x->Math.sqrt(x));
	    functions.put("sin",x->Math.sin(x));
	    functions.put("cos",x->Math.cos(x));
	    functions.put("tan",x->Math.tan(x));
	    functions.put("rnd",x->Math.random()*x);
	    functions.put("floor",x->Math.floor(x));
	    functions.put("int",x->(int)x);
	}

	public static Expression parse(String str,Map<String,Double>variables) {
		return new Object() {
			int pos=-1,ch;
			void nextChar() {
				ch=(++pos<str.length())?str.charAt(pos):-1;
			}
			boolean eat(int charToEat) {
				while(ch==' ')nextChar();
				if(ch==charToEat){
					nextChar();
					return true;
				}return false;
			}


			Expression parse() {
				nextChar();
				Expression x=parseExpression();
				if(pos<str.length())throw new RuntimeException("Unexpected: "+(char)ch);
				return x;
			}

			Expression parseExpression() {
				Expression x=parseTerm();
				for(;;){
					if(eat('+')){
						Expression a=x,b=parseTerm();                      
						x=(()->a.eval()+b.eval());
					}else if(eat('-')){
						Expression a=x,b=parseTerm();
						x=(()->a.eval()-b.eval());
					}else return x;
				}
			}

			Expression parseTerm() {
				Expression x=parseFactor();
				for(;;){
					if(eat('*')){
						Expression a=x,b=parseFactor();
						x=(()->a.eval()*b.eval());
					}else if(eat('/')){
						Expression a=x,b=parseFactor();
						x=(()->a.eval()/b.eval());
					}else if(eat('%')){
						Expression a=x,b=parseFactor();
						x=(()->a.eval()%b.eval());
					}else return x;
				}
			}

			Expression parseFactor() {
				if(eat('+'))return parseFactor();
				if(eat('-')){
					Expression b=parseFactor();
					return(()->-1*b.eval());
				}

				Expression x;
				int startPos=this.pos;
				if(eat('(')){ 
					x=parseExpression();
					eat(')');
				}else if((ch>='0'&&ch<='9')||ch=='.'){
					while((ch>='0'&&ch<='9')||ch=='.') nextChar();
					double xx=Double.parseDouble(str.substring(startPos,this.pos));
					x=()->xx;
			    }else if(ch>='a'&&ch<='z'){
			        while(ch>='a'&&ch<='z')nextChar();
			        String name=str.substring(startPos,this.pos);
			        if(functions.containsKey(name)){
			            DoubleUnaryOperator func=functions.get(name);
			            Expression arg=parseFactor();
			            x=()->func.applyAsDouble(arg.eval());
			        }else x=()->variables.get(name);
			    }else throw new RuntimeException("Unexpected: "+(char)ch);
				return x;
			}
		}.parse();
	}
}