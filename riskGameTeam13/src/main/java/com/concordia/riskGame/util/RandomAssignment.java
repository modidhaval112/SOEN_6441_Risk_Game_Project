package com.concordia.riskGame.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.concordia.riskGame.model.Continent.Continent;
import com.concordia.riskGame.model.Country.Country;
import com.concordia.riskGame.model.Player.Player;


/**
 * This class assigns countries randomly to the different players
 * @author Dhaval
 *
 */
public class RandomAssignment {

	/**
	 * This method sets countries randomly assign to the players
	 * @param noOfPlayers number of players
	 * @param countryList List of Countries
	 * @return player Player object
	 */
	public Player randonAssignmentMethod(int noOfPlayers, List<Country> countryList) {
		int noOfCountries = countryList.size();
		
		System.out.println("Players : " + noOfPlayers + " Countries : " + noOfCountries);
		
		List<Player> playersList = new ArrayList<>();
		for (int i = 0; i < noOfPlayers; i++) {
			Player playerObject = new Player("p"+(i+1));
			playersList.add(playerObject);
		}
		
		RandomAssignment inputObject = new RandomAssignment();
		int[] dividedValuesList = inputObject.divider(noOfCountries, noOfPlayers);
		
		Map<Continent, List<Country>> continentAssign = new HashMap<>();
		List<Country> newCountryList = new ArrayList<>(countryList);

		System.out.println("\n");
		for( Continent key : continentAssign.keySet() ) {
		    System.out.println("Continent Name : " +  key.getContinentName() );
		    for (int i = 0; i < key.getCountries().size(); i++) {
				System.out.println("     Assigned Countries : " + continentAssign.get(key).get(i).getCountryName());
			}
		    System.out.println("");
		}
				
		dividedValuesList = inputObject.divider(noOfCountries, noOfPlayers);
		Map<Player, List<Country>> playerAssign = new HashMap<>();
		newCountryList = new ArrayList<>(countryList);
		Player player = new Player();
		
		int k = 0;
		for (int i = 0; i < noOfPlayers; i++) {
			List<Country> countryList1 = new ArrayList<>();
			for (int j = 0; j < dividedValuesList[i]; j++) {		
				int rando = (int) ((Math.random() * newCountryList.size()));
				countryList1.add(newCountryList.get(rando));
				newCountryList.remove(rando);
			}
			playersList.get(i).setAssignedCountries(countryList1);
			playerAssign.put(playersList.get(i), countryList1);
		}

		player.setPlayerAssign(playerAssign);
			
		return player;
		
	}
	
	/**
	 * This method divides a number into smaller numbers
	 * @param number number of countries
	 * @param parts number of players
	 * @return randoms array of divided numbers
	 */
	int[] divider(int number, int parts)
    {
		int[] randoms = new int[parts];
	    Arrays.fill(randoms, (number/parts)-1); // At least one
	    int remainder = number - ((number/parts)-1)*parts;
	    Random random = new Random();
	    for (int i = 0; i < parts - 1 && remainder > 0; ++i) {
	        int diff = random.nextInt(remainder);
	        randoms[i] += diff;
	        remainder -= diff;
	   }
	   randoms[parts - 1] += remainder;
	   Arrays.sort(randoms);

	   // Reverse (for getting a descending array):
	   for (int i = 0, j = parts - 1; i < j; ++i, --j) {
	       int temp = randoms[i];
	       randoms[i] = randoms[j];
	       randoms[j] = temp;
	   }
        return randoms;
    }
}
