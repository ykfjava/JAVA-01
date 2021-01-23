package main.java;
public class XSSDemo {

  public static void main(String[] args) {

    int index = 0;
    while (index++ < 10000) {
    	int c = index;
    	new Thread(()->{
    		System.out.println(c + " -> " + Runtime.getRuntime().totalMemory() +"MB "
							 			  + Runtime.getRuntime().maxMemory()   +"MB "
						 	 			  + Runtime.getRuntime().freeMemory()  +"MB ");
    		try{Thread.sleep(6000);}
    		catch(Exception e){}
    	}).start();
    }
  }

}
