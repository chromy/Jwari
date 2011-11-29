import java.util.ArrayList;

public class OwariTestSuite {
	  public static void main(String[] args) {
	        System.out.println("Running tests...");

	        playerTests();
	        bowlTests();
	        
	        System.out.println("...tests complete.");
	  }
	    
	  public static void playerTests() {
		  ArrayList<String> actions = new ArrayList<String>();
		  Player p1 = new Player();
		  testPlayer(actions, "Made p1", p1, 0);
		  p1.addToScore(5);
		  testPlayer(actions, "Add 5 to p1 score", p1, 5);
		  Player p2 = new Player();
		  testPlayer(actions, "Made p2", p2, 0);
		  p2.addToScore(3);
		  testPlayer(actions, "Add 3 to p2 score", p2, 3);
		  testPlayer(actions, "Did nothing to p1 score", p1, 5);
		  p1.addToScore(2);
		  testPlayer(actions, "Add 2 to p1 score", p1, 7);
		  testPlayer(actions, "Did nothing to p2 score", p2, 3);
	  }
	  
	  public static void test(int expected, int actual, String msg){
		  if (expected != actual) {		  
			  System.out.println(msg 
					  + " Expected: " + expected 
					  + " Got: " + actual);
		  }
	  }
	  
	  public static void bowlTests() {
		  Bowl b1 = new Bowl(4);
		  test(4, b1.getStones(), "01. Bowl(4)");
		  b1.depositStone();
		  test(4, b1.getStones(), "02. Stones = 4; Deposit stone.");
		  b1.depositStone();
		  test(4, b1.getStones(), "03. Stones = 4; Depos = 1; Deposit stone.");
		  test(0, b1.updateAndGetScore(), "04. Stones = 4; Depos = 2; Update.");
		  test(6, b1.getStones(), "05. Stones = 6");
		  b1.depositStone();
		  test(6, b1.takeAllStones(), "06. Stones = 6; Depos = 1; Take all.");
		  test(0, b1.getStones(), "07. Stones = 0; Depos = 1");
		  test(0, b1.updateAndGetScore(), "Stones = 0; Depos = 1; Update.");
		  test(1, b1.getStones(), "08. Stones = 1");
		  b1.depositStone();
		  test(1, b1.getStones(), "09. Stones = 1; Depos = 1; Deposit stone.");
		  test(2, b1.updateAndGetScore(), "10. Stones = 1; Depos = 1; Update.");
		  test(0, b1.getStones(), "11. Stones = 0; Depos = 0;");
		  test(0, b1.updateAndGetScore(), "12. Stones = 0; Depos = 0; Update.");
		  test(0, b1.getStones(), "13. Stones = 0; Depos = 0;");
		  
		  b1.depositStone();
	  }
	  
	  public static void testPlayer(ArrayList<String> actions, String action, 
			  Player p, int expected) {
		  actions.add(action);
		  int actual = p.getScore();
		  if (actual != expected) {
			  System.out.println("-----");
			  for (String act : actions) {
				  System.out.println(act);
			  }
			  System.out.println("I expected: " + expected + 
					  		     " but got: " + actual);
		  }
	  }
	  
}
