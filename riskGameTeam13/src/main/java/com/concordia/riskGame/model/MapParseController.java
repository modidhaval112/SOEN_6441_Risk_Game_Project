package com.concordia.riskGame.model;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JOptionPane;

import com.concordia.riskGame.View.GameDriver;
import com.concordia.riskGame.View.MapEditView;
import com.concordia.riskGame.entity.Continent;
import com.concordia.riskGame.entity.Country;
import com.concordia.riskGame.entity.Player;
import com.concordia.riskGame.exception.InvalidMapFileException;
import com.concordia.riskGame.util.MapValidator;
import com.concordia.riskGame.util.RandomAssignment;
import com.concordia.riskGame.util.ReadConfiguration;

/**
 * This Class has the implementation of reading the .map file and setting
 * countryAndNeighbors and continentAndItsCountries fields in the MapContents
 * class
 * 
 * @author Dheeraj As - Team 13
 * @author Darwin Anirudh G - Team 13
 */
public class MapParseController {

	private File file;
	private String currentLine;
	private String filePath;
	private File fileObject;
	private BufferedReader bufferReaderForFile;
	private MapContents mapContentsObj;
	private String mapAuthorName;
	private int numberOfPlayers;
	private String[] splitUtllityString;
	private List<Continent> contitentList;
	private List<Country> countryList;
	private List<Country> adjCountry;
	private Continent continentObejct;
	private Country countryObject;
	private MapContents mapContentObject;
	private HashMap<Country, List<Country>> countryAndNeighbors;
	private HashMap<Continent, List<Country>> continentAndItsCountries;
	private GameDriver gameDriverObject;
	private Player playerObject;
	private ReadConfiguration readConfigurationObject;
	private List<Player> playerList;
	private int initialArmies;
	private MapEditView mapEditView;
	private MapValidator mapValidator;
	private List<Player> playerListClone;

	/**
	 * This method reads the map file and set the corresponding continents and
	 * countries in their respective entity classes.
	 * 
	 * @param filePath    Path of the map file
	 * @param numberCombo Number of players returned from the drop down menu
	 * 
	 * @return the mapContent Object with map details enriched
	 */

	public MapContents mapParser(String filePath, String numberCombo) {
		try {

			fileObject = new File(filePath);
			numberOfPlayers = Integer.parseInt(numberCombo);
			System.out.println("File Path " + filePath);
			bufferReaderForFile = new BufferedReader(new FileReader(fileObject));
			mapValidator = new MapValidator();
			mapValidator.init(fileObject);

			if (!mapValidator.getValidMapFlag()) {
				throw new InvalidMapFileException("Invalid Map File");
			}

			readMapElements(bufferReaderForFile);

			setcontitentAndCountriesMap();
			mapContentObject = new MapContents();
			mapContentObject.setContinentAndItsCountries(continentAndItsCountries);
			mapContentObject.setCountryAndNeighbors(countryAndNeighbors);
			RandomAssignment randonAssignment = new RandomAssignment();
			playerObject = randonAssignment.randonAssignmentMethod(Integer.parseInt(numberCombo), countryList);
			initialArmies = initialArmyAssignment(Integer.parseInt(numberCombo));
			playerList = new ArrayList();

			for (Player key : playerObject.getPlayerAssign().keySet()) {
				System.out.println("Player Name : " + key.getName());
				System.out.println("     Assigned Of Countries  :" + key.getAssignedCountries().size());
				key.setTotalArmies(initialArmies);
				System.out.println("     Assigned Armies            :" + initialArmies);
				for (int i = 0; i < key.getAssignedCountries().size(); i++) {
					System.out.println("     Assigned Of Countries : "
							+ playerObject.getPlayerAssign().get(key).get(i).getCountryName());

				}

				playerList.add(key);
				System.out.println("");
			}

			playerList=armyAssignment(playerList);
			gameDriverObject = new GameDriver();
			gameDriverObject.gamePhase(playerList, countryAndNeighbors);

		} catch (InvalidMapFileException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapContentObject;

	}

	/**
	 * This method get the number of players and returns initial armies
	 * 
	 * @param intialArmies initial number of army assigned.
	 * 
	 * @return The initial army assignment number
	 */
	public int initialArmyAssignment(int intialArmies) {

		if (intialArmies == 3)
			initialArmies = 35;

		else if (intialArmies == 3)
			initialArmies = 35;

		else if (intialArmies == 4)
			initialArmies = 30;

		else if (intialArmies == 5)
			initialArmies = 25;

		else if (intialArmies == 6)
			initialArmies = 20;

		else
			initialArmies = 15;

		return initialArmies;
	}

	/**
	 * The following method reads the file to be edited and edit the map.
	 * 
	 * @param filePath Path of the map file.
	 */
	public void editMapParsermapParser(String filePath) {

		try {
			fileObject = new File(filePath);
			System.out.println("File Path is  " + filePath);
			bufferReaderForFile = new BufferedReader(new FileReader(fileObject));
			mapValidator = new MapValidator();
			mapValidator.init(fileObject);
			readMapElements(bufferReaderForFile);
			setcontitentAndCountriesMap();
			mapContentObject = new MapContents();
			mapContentObject.setContinentAndItsCountries(continentAndItsCountries);
			mapContentObject.setCountryAndNeighbors(countryAndNeighbors);
			mapEditView = new MapEditView();
			mapEditView.MapDefinition(countryAndNeighbors, continentAndItsCountries);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method reads the selected map's elements
	 * 
	 * @param bufferReader BufferedReader to read text from the file
	 */
	public void readMapElements(BufferedReader bufferReader) {
		try {
			contitentList = new ArrayList<>();
			countryList = new ArrayList<>();
			countryAndNeighbors = new HashMap<>();
			while ((currentLine = bufferReader.readLine()) != null) {
				if (currentLine.contains("[Map]")) {
					while ((currentLine = bufferReader.readLine()) != null && !currentLine.contains("[")) {
						if (currentLine.contains("author")) {
							splitUtllityString = currentLine.split("=");
							System.out.println("splitUtllityString :" + splitUtllityString[1]);
							mapAuthorName = splitUtllityString[1];
						}
					}
					bufferReader.mark(0);
				}

				bufferReader.reset();

				if (currentLine.contains("[Continents]")) {
					while ((currentLine = bufferReader.readLine()) != null && !currentLine.contains("[")) {
						if (!currentLine.isEmpty()) {
							String[] continentValues = currentLine.split("=");
							Continent continentObject = new Continent(continentValues[0],
									Integer.parseInt(continentValues[1]));
							contitentList.add(continentObject);
						}
					}
					bufferReader.mark(0);
				}

				bufferReader.reset();

				if (currentLine.contains("[Territories]")) {
					while ((currentLine = bufferReader.readLine()) != null) {
						if (!currentLine.isEmpty()) {
							String[] territoryValues = currentLine.split(",", 2);
							String[] adjCountries = territoryValues[1].split(",", 2);
							String[] adjCountries2 = adjCountries[1].split(",", 2);
							String[] adjCountries3 = adjCountries2[1].split(",", 2);
							System.out.println("###### The value of split one is ###### :" + territoryValues[0]);
							System.out.println(
									"###### The value of adj split adjCountries3[0] is ###### :" + adjCountries3[0]);
							System.out.println(
									"###### The value of adj split adjCountries3[1] is ###### :" + adjCountries3[1]);
							Country cc = new Country(territoryValues[0], adjCountries3[0]);
							adjCountry = new ArrayList<Country>();
							StringTokenizer st = new StringTokenizer(adjCountries3[1], ", ");
							while (st.hasMoreTokens()) {
								Country adjC = new Country(st.nextToken(","));
								adjCountry.add(adjC);
							}
							cc.setNeighbouringCountries(adjCountry);
							countryList.add(cc);
							countryAndNeighbors.put(cc, adjCountry);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	/**
	 * Method prints out the countries and its neighbours'
	 */
	public void countryAndNeighboursMap() {
		try {
			System.out.println("######  countryAndNeighbors Map size     #############" + countryAndNeighbors.size());
			for (Map.Entry<Country, List<Country>> entry : countryAndNeighbors.entrySet()) {
				System.out.println("##### Key is ##### :" + entry.getKey().getCountryName() + "value size is   "
						+ entry.getValue().size());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method set Continent and its respective countries Map
	 */
	public void setcontitentAndCountriesMap() {
		try {
			System.out.println("##### Reading countryList ######");
			continentAndItsCountries = new HashMap<>();
			Collections.shuffle(contitentList);
			for (Continent continentInstance : contitentList) {
				System.out.println("Continent Name is " + continentInstance.getContinentName());
				Continent contientObj = new Continent(continentInstance.getContinentName());
				List<Country> counList = new ArrayList<Country>();
				for (Country countryInstance : countryList) {
					if (countryInstance.getBelongsToContinent().equals(continentInstance.getContinentName())) {
						counList.add(countryInstance);
					}
				}
				continentAndItsCountries.put(contientObj, counList);
			}

			for (Map.Entry<Continent, List<Country>> entry : continentAndItsCountries.entrySet()) {
				System.out.println("##### Key is ##### :" + entry.getKey().getContinentName() + "value size is   "
						+ entry.getValue().size());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method gets the List of countries for the corresponding continent.
	 *
	 * @param belongsToContinent Continent Name
	 */
	public List<Country> getContinentCountries(String continentName) {

		try {
			List<Country> countriesList = new ArrayList<Country>();
			for (int i = 0; i < countryList.size(); i++) {
				if (continentName.equals(countryList.get(i).getBelongsToContinent())) {
					System.out
							.println("###### Country name is ###########      :" + countryList.get(i).getCountryName());
					countriesList.add(countryList.get(i));

				}
			}
			return countriesList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * public void playerNameAssignment() {
	 * 
	 * readConfigurationObject = new ReadConfiguration(); System.out.println(
	 * "##### Number of Players are                     ###### :" +
	 * readConfigurationObject.getPlayerCount()); playerList = new ArrayList();
	 * 
	 * for (int i = 0; i < numberOfPlayers; i++) { playerObject = new Player();
	 * playerObject.setName("Player" + "-" + i);
	 * 
	 * playerList.add(playerObject);
	 * System.out.println("####### The player name is #########" +
	 * playerObject.getName());
	 * 
	 * }
	 * 
	 * }
	 */
	
	
	public List<Player> armyAssignment(List<Player> listPlayer)
	{
	
		playerListClone  = new ArrayList();
		
		int assignedArmies;
		
		
		for (Player player : listPlayer )
		{
			System.out.println("######### The name of the player is ######### :"+player.getName());
			System.out.println("    ");
			System.out.println("######### The assigned armies are  ######### :"+player.getTotalArmies());
			Player playerObject = new Player();
			playerObject = player;
			assignedArmies = playerObject.getTotalArmies();
			
			for (Country countryObject : playerObject.getAssignedCountries())
			{
				countryObject.setArmies(1);
				assignedArmies = assignedArmies - 1;
			}
			playerObject.setTotalArmies(assignedArmies);
			playerListClone.add(playerObject);
		}
				
		
		
		
		return playerListClone;
	}
	
	

}
