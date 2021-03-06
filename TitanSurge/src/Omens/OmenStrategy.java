package Omens;
import java.util.Scanner;

import Common.*;

//Class that checks all cards that can be played in the hand, used on the field, 
//cards to buy
public class OmenStrategy {
	CardObserver observer;
	public OmensGame player;
	int choice = -1;
	String use;
	Scanner read = new Scanner(System.in);
	
	//links teh observer the player to teh class 
	public OmenStrategy(CardObserver obs, OmensGame person){
		this.observer = obs;
		this.player = person;}
	
	private void pressEnterToContinue()
	 { 
	        System.out.println("Press 'enter' to continue...");
	        try
	        {
	            System.in.read();
	        }  
	        catch(Exception e)
	        {}  
	 }
	
	private void printupdate(){
		System.out.println("Type 'print' to see your board, 'skip' to move on");

		use = read.nextLine();
		
		while(true){
			
			if(use.equalsIgnoreCase("print")){
				print();
				break;}
			
			else if(use.equalsIgnoreCase("skip")){
				System.out.println();
				break;}
			
			else{

				use=read.nextLine();
				}
		}
	}
	
	private void print(){
		player.printresources();
		player.printhand();
		player.printbank();
		player.printfield();
	}
	
	//used to play cards from the hand
	public void handcards(){
		printupdate();
		System.out.println("Cheking to see if there are cards in hand to play...");
		pressEnterToContinue();
		
		System.out.println("####################################################################");
		if(player.gethandlength() > 0){
			System.out.println("\nYou " +player.getName()+ " can play cards from the hand\nEnter -1 to stop playing cards from hand");
			player.printhand();
		}
		
		while(player.gethandlength() > 0){
			
			choice = read.nextInt();
			if(choice >= 0 && choice <= player.gethandlength()){
				player.gethandcard(choice -1).onplay();
				System.out.println();
				System.out.println("enter -1 to stop playing cards from hand");
				player.printhand();
				
			}
			else if(choice > player.gethandlength())
				{
				System.out.println("That isn't a card in hand");
				System.out.println("enter -1 to stop to playing cards from hand");
				System.out.println();
				player.printhand();
				}
			else if(choice == -1)
				break;
		}
		
		System.out.println("\n" + player.getName()+" Gold: " + player.getGoldAmount());
	}
	
	//used to buy cards from the bank and place them in the hand
	public void buycards(){
		printupdate();
		System.out.println("Checking to see if you buy cards from the bank...");
		pressEnterToContinue();
		System.out.println("####################################################################");
		
		choice = -1;
		
		while(player.getGoldAmount() >= player.cheapestcard()){
			System.out.println("\nThere is enough gold to buy something from the bank\nEnter -1 to stop buying cards from bank");
			System.out.println("Gold: " +player.getGoldAmount());
			player.printbank();
			System.out.println();
			choice = read.nextInt();
			
			if(choice >=0 && choice <= player.getBanklength()){
				
				if(player.getGoldAmount() >= player.getbankcard(choice -1 ).getCost()){
					player.getbankcard(choice -1).buy();
					System.out.println(player.getName() + " Gold: " + player.getGoldAmount() );
				}
				else{
					System.out.println("There isn't enough gold for that card.");
					player.printbank();
					System.out.println(player.getName() + " Gold: " + player.getGoldAmount() );
				}
				
			}
			
			else if(choice > player.getBanklength())
			{
				System.out.println("That isn't a card in the bank");
				System.out.println();
				player.printbank();
				}
			
			else if(choice == -1)
				break;
			
		}
		if(player.gethandlength() > 0)
			handcards();
	}
	
	private void playApples(){
		while(player.getApplesAmount() > 0){
			System.out.println("\ntype 'minion' : uses apples on minion\ntype 'self' : use apples on your health\n"
					+ "Type 'skip' to move on");
			
			player.printresources();
			use = read.next();
			
			/**FIRST if-else */
			
			if(use.equalsIgnoreCase("self")){
				player.eatApples();
				player.printresources();
				System.out.println();
			}
			else if(use.equalsIgnoreCase("minion")){
				
				/**SECOND if-else */
				
				if(player.getfieldlength() > 0){
					
					System.out.println("\nEnter -1 to stop feeding minions");
					
					//LOOP that allows the feeding of minions until 0 apples
					while(player.getApplesAmount() > 0){
						
						System.out.println(player.getName() + " Field:");
						player.printfield();
						System.out.println("which minion to feed?");
						choice = read.nextInt();
						
						/**THIRD if-else */
						
						if(choice > 0 && choice <= player.getfieldlength()){
							
							player.getfieldcard((choice -1)).consumeApples();
							player.printresources();
							System.out.println();}
						
						else if(choice == -1)
							break;
						
						else{
							
							while((choice > 0 && choice <= player.getfieldlength()) == false){
								
								System.out.println("That card is not on the field, enter a valid card number");
								player.printfield();}
							}	
					}}
				
				else
					System.out.println("There are no minions on the field");}
			
			else if(use.equalsIgnoreCase("skip"))
				break;
		}
	}
	
	private void playSkulls(){
		while(player.getSkullAmount() > 0){
			System.out.println("\ntype 'minion' : attacks enemy minion with skulls, if any\n" 
					+ "type 'enemy' : attacks enemy player with a skull");
			use = read.next();
			
			if(player.enemy.getfieldlength() > 0){
				System.out.println("\n" + player.enemy.getName()+ " field");
				player.enemy.printfield();
			}
			else
				System.out.println(player.enemy.getName() + " does not have a field");
			
			player.printresources();
			use = read.next();
			
			/**FIRST if-else chain*/
			
			if(use.equalsIgnoreCase("minion")){
				
				/**SECOND if-else chain*/
					if(player.enemy.getfieldlength() > 0){
						player.loseSkulls(1);
						player.enemy.lowestMinion();
						System.out.println("\n" +player.enemy.getName() + " field After attack");
						player.enemy.printfield();
						System.out.print("Enter 'minion' or 'enemy' for next attack, enter 'skip' to move on");
					}
					else
						System.out.println("There are no cards on the enemy field");
				}
			
			else if(use.equalsIgnoreCase("enemy")){
				player.loseSkulls(1);
				player.enemy.defend();
				System.out.println(player.getName() + " Health: " + player.enemy.gethealth());
			}
			else if(use.equalsIgnoreCase("skip"))
				break;
		}
	}
	
	private void playMagic(){
		while(player.getMagicAmount() > 0){
			System.out.println("\ntype 'minion' : uses magic on minion\ntype 'self' : use magic on your health\n"
					+ "type 'enemy' to attack enemy player \nType 'skip' to move on");
			
			player.printresources();
			use = read.next();
			
			/**FIRST if/else chain */
			
			if(use.equalsIgnoreCase("self")){
				
				player.eatMagic();
				player.printresources();
				System.out.println();}
			
			else if(use.equalsIgnoreCase("enemy")){
				player.loseMagic(1);
				player.enemy.defend();
				System.out.println("\n" + player.enemy.getName() + " Health: ");
			}
			else if(use.equalsIgnoreCase("minion")){
				
				/**SECOND if/else chain*/
				
				if(player.getfieldlength() > 0){
					
					System.out.println("\nEnter -1 to stop selecting minions");
					
					while(player.getMagicAmount() > 0){
					
						System.out.println("Player Field:");
						player.printfield();
						System.out.println("which minion to feed?");
						choice = read.nextInt();
						
						/**THIRD if/else chain*/
						
						if(choice > 0 && choice <= player.getfieldlength()){
							
							player.getfieldcard((choice -1)).consumeMagic();
							player.printresources();
							System.out.println();}
						
						else if(choice == -1)
							break;
						else{
							
							while((choice > 0 && choice <= player.getfieldlength()) == false){
								
								System.out.println("That card is not on the field, enter a valid card number");
								player.printfield();}
							}	
					}}
				
				else
					System.out.println("There are no minions on the field");
			}
			
			else if(use.equalsIgnoreCase("skip"))
				break;
		}
	}
	//called to check all resources and how to used them 
	public void useresources(){
		printupdate();
		System.out.println("Checking to see if you have play any resources...");
		pressEnterToContinue();
		System.out.println("####################################################################");
		
		if(player.getApplesAmount() > 0){
			System.out.println("You have some apples to use. Wanna use them? 'yes' or 'no'");
			use = read.nextLine();
			
			while(true){
				
				if(use.equalsIgnoreCase("yes")){
					playApples();
					break;
				}
				
				else if(use.equalsIgnoreCase("no"))
					break;
				
				else
					use = read.nextLine();
			}
		}
		
		if(player.getSkullAmount() > 0){
			System.out.println("You have some skulls to use. Wanna use them? 'yes' or 'no'");
			use = read.nextLine();
			
			while(true){
				
				if(use.equalsIgnoreCase("yes")){
					playSkulls();
					break;
				}
				
				else if(use.equalsIgnoreCase("no"))
					break;
				
				else
					use = read.nextLine();
			}
		}
		
		if(player.getMagicAmount() > 0){
			System.out.println("You have some magic to use. Wanna use them? 'yes' or 'no'");
			use = read.nextLine();
			
			while(true){
				
				if(use.equalsIgnoreCase("yes")){
					playMagic();
					break;
				}
				
				else if(use.equalsIgnoreCase("no"))
					break;
				
				else
					use = read.nextLine();
			}}
	}

	//used for the AI class to play its card
	public void Attackstrategy(){
		
		/*Focuses on getting cards that can just do damage and go offensive
		 * Plays entire hand, then buys from bank, then attacks */
		
		while(player.gethandlength() > 0){
			player.gethandcard(0).onplay();
		}
		
		for(int i = 0; i < player.getBanklength(); i+=1){
			if(player.getbankcard(i).getName().equalsIgnoreCase("Militia"))
				player.getbankcard(i).buy();
			else if(player.getbankcard(i).getName().equalsIgnoreCase("Mercernary"))
				player.getbankcard(i).buy();
			else if(player.getbankcard(i).getName().equalsIgnoreCase("Courtesan"))
				player.getbankcard(i).buy();
		}
		
		if(player.getApplesAmount() > 0){
			for(int i = 0; i < player.getfieldlength(); i+=1){
				
				if(player.getfieldcard(i).getName().equalsIgnoreCase("Courtesan")){
					while(player.getApplesAmount() > 0){
						System.out.println(player.getName() + " Fed Courtesan apple, gained skull");
						player.getfieldcard(i).consumeApples();}
					}
				else if(player.getfieldcard(i).getName().equalsIgnoreCase("Mercernary")){
					while(player.getApplesAmount() > 0){
						System.out.println(player.getName() + " Fed Mercernary apple for an attack");
						player.getfieldcard(i).consumeApples();}
					}
				else if(player.getfieldcard(i).getName().equalsIgnoreCase("Militia")){
					while(player.getApplesAmount() > 0){
						System.out.println(player.getName() + " Fed Militia apple for an attack");
						player.getfieldcard(i).consumeApples();}
					}
				}
		}
		
		if(player.getMagicAmount() > 0){
			for(int i = 0; i < player.getfieldlength(); i+=1){
				
				if(player.getfieldcard(i).getName().equalsIgnoreCase("Courtesan")){
					while(player.getApplesAmount() > 0){
						System.out.println(player.getName() + " Fed Courtesan magic for a skull");
						player.getfieldcard(i).consumeMagic();}
					}
				else if(player.getfieldcard(i).getName().equalsIgnoreCase("Mercernary")){
					while(player.getApplesAmount() > 0){
						System.out.println(player.getName() + " Fed Mercernary magic for an attack");
						player.getfieldcard(i).consumeMagic();}
					}
				else if(player.getfieldcard(i).getName().equalsIgnoreCase("Militia")){
					while(player.getApplesAmount() > 0){
						System.out.println(player.getName() + " Fed Militia magic for an attack");
						player.getfieldcard(i).consumeMagic();}
					}
				}
		}
		
		if(player.getSkullAmount() > 0){
			System.out.println(player.getName() + " Uses skull to attack");
			player.loseSkulls(1);
			player.enemy.defend();
		}
	}
	
	public void campaignTactic(){
		
		while(player.gethandlength() > 0)
			player.gethandcard(0).onplay();
		
		if(player.getGoldAmount() >= player.cheapestcard()){
			for(int i =0; i < player.getBanklength(); i++){
				if(player.getbankcard(i).getCost() == player.cheapestcard())
					player.getbankcard(i).buy();
			}
		}
		
		else if(player.getGoldAmount() >= 8){
			
			while(player.getGoldAmount() >= player.cheapestcard()){
				
				for(int i =0; i < player.getBanklength(); i++){
					if(player.getbankcard(i).getCost() == player.cheapestcard())
						player.getbankcard(i).buy();
				}
			}
		}
		
		while(player.gethandlength() > 0)
			player.gethandcard(0).onplay();
		
		if (player.getApplesAmount() > 0){
			
			for (int i = 0; i < player.getfieldlength(); i++){
				
				if (player.getfieldcard(i).canconsume() == true){
					
					while(player.getApplesAmount() > 0)
						player.getfieldcard(i).consumeApples();
				}
			}
		}
		
		if (player.getMagicAmount() > 0){
			
			for (int i = 0; i < player.getfieldlength(); i++){
				
				if (player.getfieldcard(i).canconsume() == true){
					
					while(player.getMagicAmount() > 0)
						player.getfieldcard(i).consumeMagic();;
				}
			}
		}
		
		if (player.getSkullAmount() > 0){
			
			if(player.enemy.getfieldlength() > 0){
				while(player.enemy.getfieldlength() > 0 && player.getSkullAmount() > 0){
					player.loseSkulls(1);
					player.enemy.defend();
				}
				
				if(player.enemy.gethealth() <= player.getSkullAmount()){
					player.loseSkulls(player.enemy.gethealth());
					while(player.enemy.gethealth() > 0 && player.getSkullAmount() > 0)
						player.enemy.defend();
				}
			}
		}
	}
}
