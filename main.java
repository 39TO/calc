package tyuukann;

import java.util.ArrayDeque;
import java.util.Deque;

import java.util.ArrayList;
import java.util.List;

import java.util.Arrays;




public class main {
	public static void main(String[] args) {
		
		String formula = new java.util.Scanner(System.in).nextLine();
		
		String[]sequenceList  = formula.split(" ");
		
		//かっこの個数を調べる
		int count1 = 0;
        for (int v = 0; v < sequenceList.length; v++) {
            if (sequenceList[v].equals("(")) {
                count1++;
            }
        }
        
        int count2 = 0;
        
        for (int v = 0; v < sequenceList.length; v++) {
            if (sequenceList[v].equals(")")) {
                count2++;
            }
        }
        
		if(count1 != count2) {
			System.out.println("error:かっこの数が間違っています");
			sequenceList = null;
		}
		
		//（で始まったら）で終わる決まり
		String evaluate = "";
		for (int p = 0; p < sequenceList.length; p++) {
			if(sequenceList[p].equals("(")) {
				for (int p2 = p+1; p2 < sequenceList.length; p2++) {
					if(sequenceList[p2].equals(")")) {
						evaluate = "OK";
						break;
					}
				}
				if(!evaluate.equals("OK")) {
					System.out.println("error:「(」の後に「）」がないです");
					sequenceList = null;
				}
			}
		}
		
		
		//式のミスを調べる
		for (int i=0; i<sequenceList.length; i++){
			switch (sequenceList[i]) {
            //演算子
            case "+": case "-": case "*": case "/":
            	if((sequenceList[i+1].equals("+"))||(sequenceList[i+1].equals("-"))||(sequenceList[i+1].equals("*"))||(sequenceList[i+1].equals("/"))) {
            		System.out.println("error:演算子が連続してます");
            		sequenceList = null;
            	}
            break;            	
            }
		}
		//最後尾の要素が演算子以外
		if((sequenceList[sequenceList.length - 1].equals("+"))||(sequenceList[sequenceList.length - 1].equals("-"))||(sequenceList[sequenceList.length - 1].equals("*"))||(sequenceList[sequenceList.length - 1].equals("/"))) {
					System.out.println("error:式が演算子で終わっています");
					sequenceList = null;
				}
		
		//変換
		StringBuilder resultBuilder = new StringBuilder(sequenceList.length);
		Deque<String> stack = new ArrayDeque<>();
		for(int i=0; i<sequenceList.length; i++){
            switch (sequenceList[i]) {
                case "+":
                case "-":
                    // スタックされた演算子の優先順位より低い場合は、スタックの演算子をバッファへ
                	while (!stack.isEmpty()) {
                        String c = stack.getFirst();
                        if (c.equals("*") || c.equals("/")) {
                        	while (!stack.isEmpty()) {
                        		resultBuilder.append(" ");
                        		resultBuilder.append(stack.removeFirst());
                        	}
                        } else {
                            break;
                        }
                	}
                    stack.addFirst(sequenceList[i]);
                    break;
                case "*":
                case "/":
                case "(":
                	
                    stack.addFirst(sequenceList[i]);
                    break;
                case ")":
                    // 「(」から「)」までの演算子をバッファへ
                    List<Object> list = Arrays.asList(stack.toArray());
                    /*
                    System.out.println("listの中身");
                    System.out.println(list);
                    */
                    int index = list.indexOf("(");
                    
                    Deque<String> workStack = new ArrayDeque<>();
                   
                    for (int i2 = 0; i2 <= index; i2++) {
                        String c = stack.removeFirst();                       
                        if (!c.equals("(")) {
                            workStack.addFirst(c);
                        }
                        /*
                        System.out.println("workstackの中身");
                        System.out.println(workStack);
                        */                        
                    }

                    while (!workStack.isEmpty()) {
                    	resultBuilder.append(" ");
                        resultBuilder.append(workStack.removeLast());
                    }
                    break;
                	
                default:
                    // 数値の場合
                	if(i != 0) {
                		resultBuilder.append(" ");
                	}
                    resultBuilder.append(sequenceList[i]);
                    break;
            }
            /*
            System.out.println("sequenceList[i]の中身");
            System.out.println(sequenceList[i]);
            
            System.out.println("stackの中身");
            System.out.println(stack);
            
            System.out.println("resultBuilderの中身");
            System.out.println(resultBuilder);
            
            System.out.println("-------------------------");
            */
        }
		
		
		while (!stack.isEmpty()) {
			resultBuilder.append(" ");
			resultBuilder.append(stack.removeFirst());
        }
		//変換された式の表示
        String lastFormula = resultBuilder.toString();
        System.out.println(lastFormula);
       
        //計算
        String stringArray[] = lastFormula.split(" ");
        //stackをString型で作成
        Deque<String> que = new ArrayDeque<>();
        
        String a = "";
        String b = "";
        
        for (int i = 0; i < stringArray.length; i++) {
        	switch (stringArray[i]) {
        	//記号が来たときの処理
            	case "+": case "-":
            	case "/":case "*":
            		a = que.pollFirst();
            		b = que.pollFirst();
                //数字かどうか
            		boolean isNumeric1 =  a.matches("[+-]?\\d*(\\.\\d+)?");
            		boolean isNumeric2 =  b.matches("[+-]?\\d*(\\.\\d+)?");
            		
            		
            		
            		if ( (isNumeric1) && (isNumeric2)) {	
            	   //数字の時
            			int result = 0;
                //計算を行うため一度int型にする
            			int aa = Integer.parseInt(a);
            			int bb = Integer.parseInt(b);
            			
            			if (stringArray[i].equals("+")) {
            				result = bb + aa;
            				
            			} else if (stringArray[i].equals("-")) {					
            				result = bb - aa;
            				
            			} else if (stringArray[i].equals("*")) {				
            				result = bb * aa;	
            				
            			} else if (stringArray[i].equals("/")) {				
            				result = bb / aa;			
            			}
            			
            			String finalResult = Integer.toString(result);
        	            que.addFirst(finalResult);
        	            
            		} else {//数字ではない時
            			String result = "";
            			if (stringArray[i].equals("+")) {
            				result = ("("+b+"+"+a+")");					
            			} else if (stringArray[i].equals("-")) {					
            				result = ("("+b+"-"+a+")");					
            			} else if (stringArray[i].equals("*")) {				
            				result = (b+"*"+a);					
            			} else if (stringArray[i].equals("/")) {				
            				result = (b+"/"+a);					
            			}
            			que.addFirst(result);//stackに入れる
            		}
                break;
                
            default:
            	//数字や変数が来た時
            	que.addFirst(stringArray[i]);
            
            }
        }
        System.out.println(que.pop());
       
	 }
 }

        
                
       

    
		

