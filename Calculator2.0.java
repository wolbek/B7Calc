import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.math.*;

class node
{
    String val;
    node nxt;
}

class List 
{
    node start,top;static String answer="";
    List()
    {
        start=top=null;
    }

    void push(String n)
    {
        node newnode=new node();
        newnode.val=n;
        if(top==null)
        {                      
            start=top=newnode; 
            top.nxt=null;
        }        
        else
        {
            newnode.nxt=top;
            top=newnode;            
        }         
    }

    String pop()
    {
        if(!isEmpty())
        {
            String n=top.val;           
            top=top.nxt;
            return n;
        }                
        else        
            return "Underflow";
    }

    String peek()
    {
        return top.val;
    }

    boolean isEmpty()
    {
        if(top==null)
            return true;
        else
            return false;
    }

    String change(String exp)
    {
        exp=exp.replace("(-","(0-");
        exp=exp.replace("(+","(0+");
        exp=exp+"q";
        exp=exp.replace("+","q+");
        exp=exp.replace("-","q-");
        exp=exp.replace("*","q*");
        exp=exp.replace("/","q/");
        exp=exp.replace("^","q^");
        exp=exp.replace(")","q)");
        exp=exp.replace(")q",")");   
        exp=exp.replace("(q","(0");        
        exp=exp.replace("]","q]");
        exp=exp.replace("]q","]");   
        exp=exp.replace("[q","[0");        
        exp=exp.replace(",","q,");        
        exp=exp.replace("\u2202q","\u2202");
        return exp;
    }   
    
  int Prec(char ch) 
    { 
        switch (ch) 
        { 
            case '+': 
            case '-': 
            return 1; 

            case '*': 
            case '/': 
            return 2; 

            case '^': 
            return 3; 
            
            case '!': 
            return 4; 
        } 
        return -1; 
    } 

    int endBP(String exp)//gives corresponding ending endBP position, E.g if exp=(2+3)*4 then j=4
    {
    	int i=exp.indexOf('(');
        char c;int j=0,inc=0;
        for(;i<exp.length();i++)
        {
            c=exp.charAt(i);
            if(c=='(')
                inc++;
            else if (c==')')
                inc--;
            if(inc==0)
            {
                j=i;
                break;
            }
        }
        return j;    
    }

    int sL()//sL: returns number of elements in stack
    {
        int len=0;
        node t=top;
        while(t!=null)
        {
            t=t.nxt;
            len++;
        }
        return len;
    }
   
    BigDecimal exp(BigDecimal x,int fix)
    {
        BigDecimal sum=new BigDecimal("1");
        BigDecimal presum=new BigDecimal("1");
        BigDecimal pow=new BigDecimal("1");
        BigDecimal fact=new BigDecimal("1");
        for(long i=1;;i++)
        {            
            presum=sum;
            pow=pow.multiply(x);
            fact=fact.multiply(BigDecimal.valueOf(i));
            sum=sum.add(pow.divide(fact,fix,RoundingMode.HALF_EVEN));
            if(sum.compareTo(presum)==0)
                break;
        }
        return sum.setScale(fix,RoundingMode.HALF_UP);        
    }
   
    BigDecimal decPow(BigDecimal b,BigDecimal power,int fix)
    {           
        String p=power.toString();
        BigDecimal integer=new BigDecimal(p.substring(0,p.indexOf('.')));
        BigDecimal decimal=power.subtract(integer);
        return intPow(b,integer,fix).multiply(exp(ln(b,fix).multiply(decimal),fix)).setScale(fix,RoundingMode.HALF_UP);
    }    

    BigDecimal intPow(BigDecimal b,BigDecimal p,int fix)
    {      
    	BigDecimal ans=new BigDecimal(1);
    	if(p.signum()==-1)
    	{   
    		p=p.negate();
            for(long i=1;p.compareTo(BigDecimal.valueOf(i))>=0;i++)
                ans=ans.multiply(b);
            return BigDecimal.valueOf(1).divide(ans,fix,RoundingMode.HALF_UP);
    	}
    	else
    	{
        for(long i=1;p.compareTo(BigDecimal.valueOf(i))>=0;i++)
            ans=ans.multiply(b);
        return ans.setScale(fix,RoundingMode.HALF_UP);
    	}
    }    

    BigDecimal ln(BigDecimal x,int fix)
    {                  
        BigDecimal limit=BigDecimal.valueOf(1).movePointLeft(fix);
        BigDecimal eToX;
        BigDecimal term;
        BigDecimal n=x;
        while(true)
        {   
            eToX=exp(x,fix);
            term=eToX.subtract(n).divide(eToX,fix,RoundingMode.HALF_EVEN);
            x=x.subtract(term);           
            if(term.compareTo(limit)<=0)
                break;               
        }
        return x.setScale(fix,RoundingMode.HALF_UP); 
    }   

    BigDecimal sin(BigDecimal x,int fix)
    {
	    BigDecimal lastVal=x.add(BigDecimal.ONE);
	    BigDecimal currentValue=x;
	    BigDecimal xSquared=x.multiply(x);
	    BigDecimal nr=x;
	    BigDecimal dr=BigDecimal.ONE;
	    int i=0;
	    while (lastVal.compareTo(currentValue) != 0) 
	    {
	        lastVal = currentValue;
	        int z=2*i+3;
	        dr=dr.multiply(BigDecimal.valueOf(z));
	        dr=dr.multiply(BigDecimal.valueOf(z - 1));
	        nr=nr.multiply(xSquared);
	        BigDecimal term=nr.divide(dr, fix, RoundingMode.HALF_UP);
	        if (i%2==0) 
	            currentValue=currentValue.subtract(term);
	         else 
	            currentValue=currentValue.add(term);
	        i++;
	    }
	    return currentValue;
	}
    
    BigDecimal cos(BigDecimal x,int fix)
    {
    	BigDecimal currentValue=BigDecimal.ONE;
        BigDecimal lastVal=currentValue.add(BigDecimal.ONE);
        BigDecimal xSquared=x.multiply(x);
        BigDecimal nr=BigDecimal.ONE;
        BigDecimal dr=BigDecimal.ONE;
        int i=0;
        while (lastVal.compareTo(currentValue) != 0)
        {
            lastVal = currentValue;
            int z = 2 * i + 2;
            dr = dr.multiply(BigDecimal.valueOf(z));
            dr = dr.multiply(BigDecimal.valueOf(z - 1));
            nr   = nr.multiply(xSquared);
            BigDecimal term = nr.divide(dr, fix, RoundingMode.HALF_UP);
            if (i % 2 == 0) 
                currentValue = currentValue.subtract(term);
            else 
                currentValue = currentValue.add(term);            
            i++;
        }
        return currentValue;
    }
    
    BigDecimal tan(BigDecimal x,int fix)
    {
    	BigDecimal sum=sin(x,fix).divide(cos(x,fix),fix,RoundingMode.HALF_UP);
    	return sum;
    }
    BigDecimal sec(BigDecimal x,int fix)
    {
    	BigDecimal sum=BigDecimal.valueOf(1).divide(cos(x,fix),fix,RoundingMode.HALF_UP);
    	return sum;
    }
    BigDecimal csc(BigDecimal x,int fix)
    {
    	BigDecimal sum=BigDecimal.valueOf(1).divide(sin(x,fix),fix,RoundingMode.HALF_UP);
    	return sum;
    }
    BigDecimal cot(BigDecimal x,int fix)
    {
    	BigDecimal sum=BigDecimal.valueOf(1).divide(sin(x,fix).divide(cos(x,fix),fix,RoundingMode.HALF_UP),fix,RoundingMode.HALF_UP);
    	return sum;
    }
   
    BigDecimal asin(BigDecimal x,int fix)
    {       	  	
        BigDecimal sum=x;
        BigDecimal presum;
        BigDecimal term;
        int j=3;//Rather than the for loop jumps two times making the precision less, take j outside loop.
        for(int i=1;i<=fix;i++)   
        {
            presum=sum;            
            term=fact1(j-2).multiply(intPow(x,new BigDecimal(j),fix)).divide(fact2(j-1).multiply(BigDecimal.valueOf(j)),fix,RoundingMode.HALF_UP);
            sum=sum.add(term);   
            if(sum.compareTo(presum)==0)
                break;
            j=j+2;
        }
        return sum.setScale(fix,RoundingMode.HALF_UP);
    }
    BigDecimal acos(BigDecimal x,int fix)
    {
    	BigDecimal sum=BigDecimal.valueOf(Math.pow(1-(x.multiply(x)).doubleValue(),0.5));
    	return sum;
    }
    BigDecimal atan(BigDecimal x,int fix)
    {
    	BigDecimal sum=BigDecimal.valueOf(x.doubleValue()/Math.pow(1+(x.multiply(x)).doubleValue(),0.5));
    	return sum;
    }
    BigDecimal asec(BigDecimal x,int fix)
    {
    	BigDecimal sum=BigDecimal.valueOf(1/Math.pow(1-(x.multiply(x)).doubleValue(),0.5));
    	return sum;
    }
    BigDecimal acsc(BigDecimal x,int fix)
    {
    	BigDecimal sum=BigDecimal.valueOf(1).divide(x);
    	return sum;
    }
    BigDecimal acot(BigDecimal x,int fix)
    {
    	BigDecimal sum=BigDecimal.valueOf(1/(x.doubleValue()/Math.pow(1+(x.multiply(x)).doubleValue(),0.5)));
    	return sum;
    }
    BigDecimal fact(long num)
    {
        BigDecimal ans=new BigDecimal("1");
        for(long i=1;i<=num;i++)
            ans=ans.multiply(BigDecimal.valueOf(i));
        return ans;
    }    
    BigDecimal fact1(int num)//for asin
    {
        BigDecimal ans=new BigDecimal("1");
        for(int i=1;i<=num;i+=2)        
            ans=ans.multiply(BigDecimal.valueOf(i));        
        return ans;
    }

    BigDecimal fact2(int num)//for asin
    {
        BigDecimal ans=new BigDecimal("1");
        for(int i=2;i<=num;i+=2)        
            ans=ans.multiply(BigDecimal.valueOf(i));        
        return ans;
    }   
    
    String iTP(String exp,int slen) //Takes operators in stack
    {   	
        String str ="";int pos=0;//pos stores starting index of a term, First i gets changed then pos gets changed          
        for (int i = 0; i<exp.length(); ++i) 
        { 
            char c = exp.charAt(i);        
                        
            if(c=='s'||c=='c'||c=='t'||c=='l')
            {         	
                int eB=endBP(exp.substring(i));   
                i=i+eB;   
                str=str+exp.substring(pos,i+1);                               
            }
            else if(c=='\u2202'||c=='\u222B')
            {            	
            	i=exp.indexOf(']',i);     	
            	str=str+exp.substring(pos,i+1);        	          	
            }        
            else if (Character.isLetterOrDigit(c)||c=='.'||(c+"").equals("\u207B") || (c+"").equals("\u00B9"))
                str += c;    
            else if (c=='(') 
                push(c+""); 
            else if (c==')') 
            {                          	
                while (!isEmpty() && !(peek().equals("("))) 
                    str += pop();       
                pop();//endBP (which is not of sin log) does not get included in iTP string               
            } 
            else 
            { 
            	pos=i+1;
                while (!isEmpty() && Prec(c)<=Prec(peek().charAt(0)))              
                    str += pop();               
                push(c+""); 
            } 
        }         
        while (!isEmpty() && sL()>slen)           
            str += pop();           
        return str;     	
    }     

    String eP(String exp,int fix,boolean rad) //Takes numbers in stack
    {         
    	try {
        int pos=0;//stores the starting index of a digit   
        fix=fix+15;//15 is perfect, gives accurate answers.
       
        for(int i=0;i<exp.length();i++) 
        { 
            char c=exp.charAt(i);            
            
            if(c=='s'||c=='c'||c=='t')
            {            	
            	 double x=1,y=1;
                 if(rad==false)
                 {
                     x=Math.PI/180;                      
                     y=180/Math.PI;
                 }
                String str=exp.substring(i,i+3);
                int sB=exp.indexOf('(',i);                
                int eB=endBP(exp.substring(i));
                String bracExp=exp.substring(sB,i+eB+1);
                
                BigDecimal num=new BigDecimal(eP(iTP(bracExp,sL()),fix,rad));                  
                
                if((exp.charAt(i+3)+"").equals("\u207B"))
                {                           	
                    if(str.equals("sin"))                     
                        push(BigDecimal.valueOf(y).multiply(asin(num,fix)).setScale(fix,RoundingMode.HALF_UP).toString());                   
                    else if(str.equals("cos"))                         
                        push(BigDecimal.valueOf(y).multiply(acos(num,fix)).setScale(fix,RoundingMode.HALF_UP).toString());                    
                    else if(str.equals("tan"))                       
                        push(BigDecimal.valueOf(y).multiply(atan(num,fix)).setScale(fix,RoundingMode.HALF_UP).toString());                    
                    else if(str.equals("sec"))                       
                        push(BigDecimal.valueOf(y).multiply(asec(num,fix)).setScale(fix,RoundingMode.HALF_UP).toString());                    
                    else if(str.equals("csc"))                        
                        push(BigDecimal.valueOf(y).multiply(acsc(num,fix)).setScale(fix,RoundingMode.HALF_UP).toString());                    
                    else if(str.equals("cot"))                        
                        push(BigDecimal.valueOf(y).multiply(acot(num,fix)).setScale(fix,RoundingMode.HALF_UP).toString());                    
                    i=i+eB;
                }
                else
                {                    
                    num=BigDecimal.valueOf(x).multiply(num);
                    
                    if(str.equals("sin"))                              
                        push(sin(num,fix).toString());                   
                    else if(str.equals("cos"))
                        push(cos(num,fix).toString());                    
                    else if(str.equals("tan"))
                        push(tan(num,fix).toString());                   
                    else if(str.equals("sec"))
                        push(sec(num,fix).toString());                  
                    else if(str.equals("csc"))
                        push(csc(num,fix).toString());                    
                    else if(str.equals("cot"))
                        push(cot(num,fix).toString());                   
                    i=i+eB;
                }
                pos=i+1;
            }      
            else if(c=='\u2202')
            {
            	int sB=exp.indexOf('(',i);                
                int eB=endBP(exp.substring(i));               
                String bracExp=exp.substring(sB,i+eB+1);            	
            	if(!bracExp.contains("\u207B"))//if inverse trigo is present,differentiation will not be performed
            	{           		
            		BigDecimal h=new BigDecimal("0.0000000000000001");//More zeros more time for evaluation		
                	String val =exp.substring(exp.indexOf('=',i)+1,exp.indexOf(']',i));
                	BigDecimal val1=new BigDecimal(eP(iTP(val,sL()),fix,rad));
               
    				BigDecimal term1=new BigDecimal(eP(iTP(bracExp.replace("x",val1.add(h)+""),sL()),fix,rad));
    				BigDecimal term2=new BigDecimal(eP(iTP(bracExp.replace("x",val1+""),sL()),fix,rad));
    				
    				push((term1.subtract(term2).divide(h,fix,RoundingMode.HALF_UP)).toString());
    				i=exp.indexOf(']',i+eB+1);//i+eB+1 rather than i because bracExp can also contain d/dx()[] and it'll give inner ']' index which will cause a problem.
    				pos=i+1;
            	}            	
            	
            }
            else if(c=='\u222B')
            {  	                        	
            	int sB=exp.indexOf('(',i);                
                int eB=endBP(exp.substring(i));                
                String bracExp=exp.substring(sB,i+eB+1);            	
            	int comma=exp.indexOf(',',i);        	
            	
            	String u1=exp.substring(exp.indexOf('=',i)+1,exp.indexOf(',',i));
            	String l1=exp.substring(exp.indexOf('=',comma)+1,exp.indexOf(']',comma));
            	
            	BigDecimal u=new BigDecimal(eP(iTP(u1,sL()),fix,rad));            	
            	BigDecimal l=new BigDecimal(eP(iTP(l1,sL()),fix,rad));      	
            	
            	String fa1=bracExp.replace("x",u+"");
                String fb1=bracExp.replace("x",l+"");
                String fc1=bracExp.replace("x",(u.add(l)).divide(BigDecimal.valueOf(2),fix,RoundingMode.HALF_UP)+"");
                BigDecimal fa=new BigDecimal(eP(iTP(fa1,sL()),fix,rad));            
                BigDecimal fb=new BigDecimal(eP(iTP(fb1,sL()),fix,rad));               
                BigDecimal fc=new BigDecimal(eP(iTP(fc1,sL()),fix,rad));                
                BigDecimal sum=(u.subtract(l)).multiply(fa.add(fb.add(fc.multiply(BigDecimal.valueOf(4))))).divide(BigDecimal.valueOf(6),fix,RoundingMode.HALF_UP);               
         
    		   	sum=sum.setScale(fix,RoundingMode.HALF_UP);
    		   	push(sum.toString());    		   	
    		   	i=exp.indexOf(']',i+eB+1);//i+eB+1 rather than i because bracExp can also contain integration().dx[u=,l=] and it'll give inner ']' index which will cause a problem.
				pos=i;
            }
            
            else if(c=='l')
            {
            	int sB=exp.indexOf('(',i);                
                int eB=endBP(exp.substring(i));               
                String bracExp=exp.substring(sB,i+eB+1);
                BigDecimal num=new BigDecimal(eP(iTP(bracExp,sL()),fix,rad));
                if(exp.charAt(i+1)=='n')
                {                  
                    push(ln(num,fix).toString());
                    i=i+eB;
                }
                else
                {                   
                    push(ln(num,fix).divide(ln(new BigDecimal(10),fix),fix,RoundingMode.HALF_UP).toString());
                    i=i+eB;
                }
                pos=i+1;
            }

            else if(c=='q') 
            {            	
                push(exp.substring(pos,i)); 
                pos=i+1;                
            }

            else if(c=='+' || c=='-'|| c=='/'||c=='*')
            { 
                pos++;
                BigDecimal val2=new BigDecimal(pop());               
                BigDecimal val1=new BigDecimal(pop());
                switch(c) 
                { 
                    case '+': 
                    push((val1.add(val2)).toString()); 
                    break; 

                    case '-': 
                    push((val1.subtract(val2)).toString()); 
                    break; 

                    case '/':                    
                    push((val1.divide(val2,fix,RoundingMode.HALF_UP)).toString());
                    break; 

                    case '*': 
                    push((val1.multiply(val2)).toString()); 
                    break; 
                } 
            } 
            else if(c=='^')
            {                	
                pos++;
                BigDecimal val2=new BigDecimal(pop());
                BigDecimal val1=new BigDecimal(pop()); 
                if(val2.toString().contains("."))                 	
                    push(decPow(val1,val2,fix).toString());          
                else                          	
                    push(intPow(val1,val2,fix).toString());                 
            }
          
            else if(c=='!')
            {
                pos++;                
                int val2=Integer.parseInt(pop());
                BigDecimal fact=new BigDecimal("1");
                for(int j=1;j<=val2;j++)                             	
                    fact=fact.multiply(BigDecimal.valueOf(j));                 
                push(fact.toString());
            }
            else if(c=='e')
            {                               
                push(exp(new BigDecimal(1),fix).toString());
                i=exp.indexOf('q',i);
                pos=i+1;
            }
            else if(c=='\u03C0')
            {                
                push(Double.toString(Math.PI));
                i=exp.indexOf('q',i);
                pos=i+1;
            }
        } 
        answer=top.val;
        return pop();   
    	}
        catch(Exception e)
        {
        	return "Invalid Expression";
        }
    } 
    String retAns(int fix)
    {   
    	try {
    	BigDecimal numAnswer=new BigDecimal(answer);// When invalid expression, the answer stored "" and if we convert it to BigDecimal we get an error.
    	return numAnswer.setScale(fix,RoundingMode.HALF_UP).toString();
    	}
    	catch(Exception e)
    	{
    		return "Invalid Expression";
    	}
    }    
}

public class Calculator implements ActionListener{	
	
	int fix=2;
	boolean rad=false;
	private JFrame frame;
	private JTextArea textArea;	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Calculator window = new Calculator();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
   
	public void actionPerformed(ActionEvent e) {
	}

	public Calculator() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(SystemColor.textText);
		frame.setBounds(100, 100, 455, 569);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);		
		
		JButton btn7 = new JButton("7");
		btn7.setFocusPainted(false);
		btn7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.insert(btn7.getText(),textArea.getCaretPosition());	
				textArea.requestFocus(); 
			}
		});
		btn7.setForeground(Color.WHITE);
		btn7.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btn7.setBorderPainted(false);
		btn7.setBackground(SystemColor.windowBorder);
		btn7.setBounds(1, 291, 69, 57);
		frame.getContentPane().add(btn7);
		
		JButton btn8 = new JButton("8");
		btn8.setFocusPainted(false);
		btn8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.insert(btn8.getText(),textArea.getCaretPosition());
				textArea.requestFocus(); 
			}
		});
		btn8.setForeground(Color.WHITE);
		btn8.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btn8.setBorderPainted(false);
		btn8.setBackground(SystemColor.windowBorder);
		btn8.setBounds(73, 291, 69, 57);
		frame.getContentPane().add(btn8);
		
		JButton btn9 = new JButton("9");
		btn9.setFocusPainted(false);
		btn9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.insert(btn9.getText(),textArea.getCaretPosition());	
				textArea.requestFocus(); 
			}
		});
		btn9.setForeground(Color.WHITE);
		btn9.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btn9.setBorderPainted(false);
		btn9.setBackground(SystemColor.windowBorder);
		btn9.setBounds(145, 291, 69, 57);
		frame.getContentPane().add(btn9);
		
		JButton btnDiv = new JButton("/");
		btnDiv.setFocusPainted(false);
		btnDiv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.insert(btnDiv.getText(),textArea.getCaretPosition());	
				textArea.requestFocus(); 
			}
		});
		btnDiv.setForeground(Color.WHITE);
		btnDiv.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnDiv.setBorderPainted(false);
		btnDiv.setBackground(SystemColor.windowBorder);
		btnDiv.setBounds(217, 291, 69, 57);
		frame.getContentPane().add(btnDiv);
		
		JButton btnPI = new JButton("\u03C0");
		btnPI.setFocusPainted(false);
		btnPI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.insert(btnPI.getText(),textArea.getCaretPosition());	
				textArea.requestFocus(); 
			}
		});
		btnPI.setForeground(Color.WHITE);
		btnPI.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		btnPI.setBorderPainted(false);
		btnPI.setBackground(Color.DARK_GRAY);
		btnPI.setBounds(217, 171, 69, 57);
		frame.getContentPane().add(btnPI);
		
		JButton btnXPY = new JButton("^");
		btnXPY.setFocusPainted(false);
		btnXPY.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				textArea.insert("^",textArea.getCaretPosition());
				textArea.requestFocus(); 
			}
		});
		btnXPY.setForeground(Color.WHITE);
		btnXPY.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		btnXPY.setBorderPainted(false);
		btnXPY.setBackground(Color.DARK_GRAY);
		btnXPY.setBounds(145, 231, 69, 57);
		frame.getContentPane().add(btnXPY);
		
		JButton btnXF = new JButton("!");
		btnXF.setFocusPainted(false);
		btnXF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				textArea.insert("!",textArea.getCaretPosition());	
				textArea.requestFocus(); 
			}
		});
		btnXF.setForeground(Color.WHITE);
		btnXF.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		btnXF.setBorderPainted(false);
		btnXF.setBackground(Color.DARK_GRAY);
		btnXF.setBounds(1, 231, 69, 57);
		frame.getContentPane().add(btnXF);
		
		JButton btne = new JButton("e");
		btne.setFocusPainted(false);
		btne.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.insert(btne.getText(),textArea.getCaretPosition());
				textArea.requestFocus(); 
			}
		});
		btne.setForeground(Color.WHITE);
		btne.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		btne.setBorderPainted(false);
		btne.setBackground(Color.DARK_GRAY);
		btne.setBounds(217, 231, 69, 57);
		frame.getContentPane().add(btne);
		
		JButton btn4 = new JButton("4");
		btn4.setFocusPainted(false);
		btn4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.insert(btn4.getText(),textArea.getCaretPosition());
				textArea.requestFocus(); 
			}
		});
		btn4.setForeground(Color.WHITE);
		btn4.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btn4.setBorderPainted(false);
		btn4.setBackground(SystemColor.windowBorder);
		btn4.setBounds(1, 351, 69, 57);
		frame.getContentPane().add(btn4);
		
		JButton btn5 = new JButton("5");
		btn5.setFocusPainted(false);
		btn5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.insert(btn5.getText(),textArea.getCaretPosition());
				textArea.requestFocus(); 
			}
		});
		btn5.setForeground(Color.WHITE);
		btn5.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btn5.setBorderPainted(false);
		btn5.setBackground(SystemColor.windowBorder);
		btn5.setBounds(73, 351, 69, 57);
		frame.getContentPane().add(btn5);
		
		JButton btn6 = new JButton("6");
		btn6.setFocusPainted(false);
		btn6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.insert(btn6.getText(),textArea.getCaretPosition());	
				textArea.requestFocus(); 
			}
		});
		btn6.setForeground(Color.WHITE);
		btn6.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btn6.setBorderPainted(false);
		btn6.setBackground(SystemColor.windowBorder);
		btn6.setBounds(145, 351, 69, 57);
		frame.getContentPane().add(btn6);
		
		JButton btnMul = new JButton("*");
		btnMul.setFocusPainted(false);
		btnMul.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.insert(btnMul.getText(),textArea.getCaretPosition());
				textArea.requestFocus(); 
			}
		});
		btnMul.setForeground(Color.WHITE);
		btnMul.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnMul.setBorderPainted(false);
		btnMul.setBackground(SystemColor.windowBorder);
		btnMul.setBounds(217, 351, 69, 57);
		frame.getContentPane().add(btnMul);
		
		JButton btn1 = new JButton("1");
		btn1.setFocusPainted(false);
		btn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.insert(btn1.getText(),textArea.getCaretPosition());
				textArea.requestFocus(); 
				
			}
		});
		btn1.setForeground(Color.WHITE);
		btn1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btn1.setBorderPainted(false);
		btn1.setBackground(SystemColor.windowBorder);
		btn1.setBounds(1, 411, 69, 57);
		frame.getContentPane().add(btn1);
		
		JButton btn2 = new JButton("2");
		btn2.setFocusPainted(false);
		btn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.insert(btn2.getText(),textArea.getCaretPosition());
				textArea.requestFocus(); 
			}
		});
		btn2.setForeground(Color.WHITE);
		btn2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btn2.setBorderPainted(false);
		btn2.setBackground(SystemColor.windowBorder);
		btn2.setBounds(73, 411, 69, 57);
		frame.getContentPane().add(btn2);
		
		JButton btn3 = new JButton("3");
		btn3.setFocusPainted(false);
		btn3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.insert(btn3.getText(),textArea.getCaretPosition());
				textArea.requestFocus(); 
			}
		});
		btn3.setForeground(Color.WHITE);
		btn3.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btn3.setBorderPainted(false);
		btn3.setBackground(SystemColor.windowBorder);
		btn3.setBounds(145, 411, 69, 57);
		frame.getContentPane().add(btn3);
		
		JButton btnSub = new JButton("-");
		btnSub.setFocusPainted(false);
		btnSub.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.insert(btnSub.getText(),textArea.getCaretPosition());
				textArea.requestFocus(); 
			}
		});
		btnSub.setForeground(Color.WHITE);
		btnSub.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnSub.setBorderPainted(false);
		btnSub.setBackground(SystemColor.windowBorder);
		btnSub.setBounds(217, 411, 69, 57);
		frame.getContentPane().add(btnSub);
		
		JButton btnDot = new JButton(".");
		btnDot.setFocusPainted(false);
		btnDot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.insert(btnDot.getText(),textArea.getCaretPosition());
				textArea.requestFocus(); 
			}
		});
		btnDot.setForeground(Color.WHITE);
		btnDot.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnDot.setBorderPainted(false);
		btnDot.setBackground(SystemColor.windowBorder);
		btnDot.setBounds(1, 471, 69, 57);
		frame.getContentPane().add(btnDot);
		
		JButton btn0 = new JButton("0");
		btn0.setFocusPainted(false);
		btn0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {								   
			   textArea.insert(btn0.getText(),textArea.getCaretPosition());
			   textArea.requestFocus(); 
			}
		});
		btn0.setForeground(Color.WHITE);
		btn0.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btn0.setBorderPainted(false);
		btn0.setBackground(SystemColor.windowBorder);
		btn0.setBounds(73, 471, 69, 57);
		frame.getContentPane().add(btn0);
		
		JButton btnEqual = new JButton("=");
		btnEqual.setFocusPainted(false);
		btnEqual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {			
		        List ob=new List();			        
		        String exp = textArea.getText(); 
		        exp=ob.change(exp);		   		      
		        String postfix=ob.iTP(exp,-1);		  	
		        String ans1=ob.eP(postfix,fix,rad);
		        if(ans1.equals("Invalid Expression"))
		        	textArea.setText(ans1);
		        else
		        {		        	
			        BigDecimal ans2=new BigDecimal(ans1).setScale(fix,RoundingMode.HALF_UP);
			        ans1=ans2.toString();
			        textArea.setText(ans1); 
		        }		        
			}
		});
		btnEqual.setForeground(Color.WHITE);
		btnEqual.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnEqual.setBorderPainted(false);
		btnEqual.setBackground(SystemColor.windowBorder);
		btnEqual.setBounds(145, 471, 69, 57);
		frame.getContentPane().add(btnEqual);
		
		JButton btnAdd = new JButton("+");
		btnAdd.setFocusPainted(false);
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.insert(btnAdd.getText(),textArea.getCaretPosition());	
				textArea.requestFocus(); 
			}
		});
		btnAdd.setForeground(Color.WHITE);
		btnAdd.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnAdd.setBorderPainted(false);
		btnAdd.setBackground(SystemColor.windowBorder);
		btnAdd.setBounds(217, 471, 69, 57);
		frame.getContentPane().add(btnAdd);
		
		JButton btnAC = new JButton("AC");
		btnAC.setFocusPainted(false);
		btnAC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText(null);
				textArea.requestFocus(); 
			}
		});
		btnAC.setForeground(Color.WHITE);
		btnAC.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnAC.setBorderPainted(false);
		btnAC.setBackground(Color.DARK_GRAY);
		btnAC.setBounds(289, 111, 74, 57);
		frame.getContentPane().add(btnAC);
		
		JButton btnLB = new JButton("(");
		btnLB.setFocusPainted(false);
		btnLB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.insert(btnLB.getText(),textArea.getCaretPosition());	
				textArea.requestFocus(); 
			}
		});
		btnLB.setForeground(Color.WHITE);
		btnLB.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnLB.setBorderPainted(false);
		btnLB.setBackground(Color.DARK_GRAY);
		btnLB.setBounds(289, 171, 74, 57);
		frame.getContentPane().add(btnLB);
		
		JButton btnLog = new JButton("log");
		btnLog.setFocusPainted(false);
		btnLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.insert(btnLog.getText()+"()",textArea.getCaretPosition());					
				textArea.setCaretPosition(textArea.getCaretPosition()-1);		
				textArea.requestFocus(); 				
			}
		});
		btnLog.setForeground(Color.WHITE);
		btnLog.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnLog.setBorderPainted(false);
		btnLog.setBackground(Color.DARK_GRAY);
		btnLog.setBounds(289, 231, 74, 57);
		frame.getContentPane().add(btnLog);
		
		JButton btnSin = new JButton("sin");
		btnSin.setFocusPainted(false);
		btnSin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {						
				textArea.insert(btnSin.getText()+"()",textArea.getCaretPosition());					
				textArea.setCaretPosition(textArea.getCaretPosition()-1);	
				textArea.requestFocus(); 					
			}
		});
		btnSin.setForeground(Color.WHITE);
		btnSin.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnSin.setBorderPainted(false);
		btnSin.setBackground(Color.GRAY);
		btnSin.setBounds(289, 351, 74, 57);
		frame.getContentPane().add(btnSin);		
		
		JButton btnCos = new JButton("cos");
		btnCos.setFocusPainted(false);
		btnCos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.insert(btnCos.getText()+"()",textArea.getCaretPosition());					
				textArea.setCaretPosition(textArea.getCaretPosition()-1);
				textArea.requestFocus(); 
			}
		});
		btnCos.setForeground(Color.WHITE);
		btnCos.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnCos.setBorderPainted(false);
		btnCos.setBackground(Color.GRAY);
		btnCos.setBounds(289, 411, 74, 57);
		frame.getContentPane().add(btnCos);
		
		JButton btnTan = new JButton("tan");
		btnTan.setFocusPainted(false);
		btnTan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.insert(btnTan.getText()+"()",textArea.getCaretPosition());					
				textArea.setCaretPosition(textArea.getCaretPosition()-1);		
				textArea.requestFocus(); 
			}
		});
		btnTan.setForeground(Color.WHITE);
		btnTan.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnTan.setBorderPainted(false);
		btnTan.setBackground(Color.GRAY);
		btnTan.setBounds(289, 471, 74, 57);
		frame.getContentPane().add(btnTan);
		
		JButton btnDeg = new JButton("DEG");
		btnDeg.setFocusPainted(false);
		btnDeg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(btnDeg.getText().equals("DEG"))
				{
					btnDeg.setText("RAD");
					rad=true;
				}
				else
				{
					btnDeg.setText("DEG");
					rad=false;
				}				
			}
		});
		btnDeg.setForeground(Color.WHITE);
		btnDeg.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnDeg.setBorderPainted(false);
		btnDeg.setBackground(Color.GRAY);
		btnDeg.setBounds(289, 291, 74, 57);
		frame.getContentPane().add(btnDeg);
		
		JButton btnBack = new JButton("\u232B");
		btnBack.setFocusPainted(false);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String backSpace=null;						
				int pos=textArea.getCaretPosition();
				if(textArea.getCaretPosition()>0)
				{
					StringBuilder str=new StringBuilder(textArea.getText());
					str.deleteCharAt(pos-1);				
					backSpace=str.toString();					
					textArea.setText(backSpace);				
					textArea.setCaretPosition(pos-1);
				}
				textArea.requestFocus();
			}
		});
		btnBack.setForeground(Color.WHITE);	
		btnBack.setBorderPainted(false);
		btnBack.setBackground(Color.DARK_GRAY);
		btnBack.setBounds(366, 111, 73, 57);
		frame.getContentPane().add(btnBack);
		
		JButton btnRB = new JButton(")");
		btnRB.setFocusPainted(false);
		btnRB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.insert(btnRB.getText(),textArea.getCaretPosition());
				textArea.requestFocus(); 
			}
		});
		btnRB.setForeground(Color.WHITE);
		btnRB.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnRB.setBorderPainted(false);
		btnRB.setBackground(Color.DARK_GRAY);
		btnRB.setBounds(366, 171, 73, 57);
		frame.getContentPane().add(btnRB);
		
		JButton btnLn = new JButton("ln");
		btnLn.setFocusPainted(false);
		btnLn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.insert(btnLn.getText()+"()",textArea.getCaretPosition());					
				textArea.setCaretPosition(textArea.getCaretPosition()-1);
				textArea.requestFocus(); 
			}
		});
		btnLn.setForeground(Color.WHITE);
		btnLn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnLn.setBorderPainted(false);
		btnLn.setBackground(Color.DARK_GRAY);
		btnLn.setBounds(366, 231, 73, 57);
		frame.getContentPane().add(btnLn);
		
		JButton btnCsc = new JButton("csc");
		btnCsc.setFocusPainted(false);
		btnCsc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.insert(btnCsc.getText()+"()",textArea.getCaretPosition());					
				textArea.setCaretPosition(textArea.getCaretPosition()-1);
				textArea.requestFocus(); 
			}
		});
		btnCsc.setForeground(Color.WHITE);
		btnCsc.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnCsc.setBorderPainted(false);
		btnCsc.setBackground(Color.GRAY);
		btnCsc.setBounds(366, 351, 73, 57);
		frame.getContentPane().add(btnCsc);
		
		JButton btnSec = new JButton("sec");
		btnSec.setFocusPainted(false);
		btnSec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.insert(btnSec.getText()+"()",textArea.getCaretPosition());					
				textArea.setCaretPosition(textArea.getCaretPosition()-1);	
				textArea.requestFocus(); 
			}
		});
		btnSec.setForeground(Color.WHITE);
		btnSec.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnSec.setBorderPainted(false);
		btnSec.setBackground(Color.GRAY);
		btnSec.setBounds(366, 411, 73, 57);
		frame.getContentPane().add(btnSec);
		
		JButton btnCot = new JButton("cot");
		btnCot.setFocusPainted(false);
		btnCot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.insert(btnCot.getText()+"()",textArea.getCaretPosition());					
				textArea.setCaretPosition(textArea.getCaretPosition()-1);		
				textArea.requestFocus(); 
			}
		});
		btnCot.setForeground(Color.WHITE);
		btnCot.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnCot.setBorderPainted(false);
		btnCot.setBackground(Color.GRAY);
		btnCot.setBounds(366, 471, 73, 57);
		frame.getContentPane().add(btnCot);
		
		JButton btnInv = new JButton("INV");
		btnInv.setFocusPainted(false);		
		
		btnInv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(btnSin.getText().equals("sin"))
					btnSin.setText("sin\u207B\u00B9");
					if(btnCos.getText().equals("cos"))
						btnCos.setText("cos\u207B\u00B9");
					if(btnTan.getText().equals("tan"))
						btnTan.setText("tan\u207B\u00B9");
					if(btnSec.getText().equals("sec"))
						btnSec.setText("sec\u207B\u00B9");
					if(btnCsc.getText().equals("csc"))
						btnCsc.setText("csc\u207B\u00B9");
					if(btnCot.getText().equals("cot"))
						btnCot.setText("cot\u207B\u00B9");
				else
				{
					btnSin.setText("sin");
					btnCos.setText("cos");
					btnTan.setText("tan");
					btnSec.setText("sec");
					btnCsc.setText("csc");
					btnCot.setText("cot");
				}				
			}
		});
		btnInv.setForeground(Color.WHITE);
		btnInv.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnInv.setBorderPainted(false);
		btnInv.setBackground(Color.GRAY);
		btnInv.setBounds(366, 291, 73, 57);
		frame.getContentPane().add(btnInv);
		
		JButton btnFix = new JButton("Fix");
		btnFix.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {			
				fix =Integer.parseInt(JOptionPane.showInputDialog("Fix"));		
			}
		});
		btnFix.setForeground(Color.WHITE);
		btnFix.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnFix.setFocusPainted(false);
		btnFix.setBorderPainted(false);
		btnFix.setBackground(Color.DARK_GRAY);
		btnFix.setBounds(145, 111, 141, 57);
		frame.getContentPane().add(btnFix);
	
		JButton btnitg = new JButton("\u222B");
		btnitg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
		        textArea.insert(btnitg.getText()+"().dx[u=,l=]",textArea.getCaretPosition());
				textArea.setCaretPosition(textArea.getCaretPosition()-11);	
				textArea.requestFocus(); 
			}
		});
		btnitg.setForeground(Color.WHITE);
		btnitg.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnitg.setFocusPainted(false);
		btnitg.setBorderPainted(false);
		btnitg.setBackground(Color.DARK_GRAY);
		btnitg.setBounds(1, 111, 69, 117);
		frame.getContentPane().add(btnitg);
		
		JButton btnDdx = new JButton("\u2202/\u2202x");
		btnDdx.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {						
				textArea.insert(btnDdx.getText()+"()[x=]",textArea.getCaretPosition());
				textArea.setCaretPosition(textArea.getCaretPosition()-5);	
				textArea.requestFocus(); 								
			}
		});
		btnDdx.setForeground(Color.WHITE);
		btnDdx.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnDdx.setFocusPainted(false);
		btnDdx.setBorderPainted(false);
		btnDdx.setBackground(Color.DARK_GRAY);
		btnDdx.setBounds(73, 111, 69, 117);
		frame.getContentPane().add(btnDdx);
		
		
		JButton btnAns = new JButton("ANS");
		btnAns.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List ob=new List();
				String ans1=ob.retAns(fix);
				if(!ans1.equals("Invalid Expression"))//If the answer is not an invalid expression then only it'll print				
				textArea.insert(ans1,textArea.getCaretPosition());				
			}
		});
		btnAns.setForeground(Color.WHITE);
		btnAns.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnAns.setFocusPainted(false);
		btnAns.setBorderPainted(false);
		btnAns.setBackground(Color.DARK_GRAY);
		btnAns.setBounds(145, 171, 69, 57);
		frame.getContentPane().add(btnAns);
		
		JButton btnX = new JButton("x");
		btnX.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.insert(btnX.getText(),textArea.getCaretPosition());
			}
		});
		btnX.setForeground(Color.WHITE);
		btnX.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnX.setFocusPainted(false);
		btnX.setBorderPainted(false);
		btnX.setBackground(Color.DARK_GRAY);
		btnX.setBounds(73, 231, 69, 57);
		frame.getContentPane().add(btnX);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(1, 0, 438, 108);
		frame.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setLineWrap(true);
		textArea.setFont(new Font("Segoe UI", Font.PLAIN, 24));				
		
	}
}
