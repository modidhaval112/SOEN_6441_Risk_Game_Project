package com.concordian.riskGame.Controller;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import com.concordia.riskGame.entity.Continent;
import com.concordia.riskGame.entity.Country;
import com.concordian.riskGame.Model.MapContents;




/**
 * This Class has the implementation of reading the .map file and  setting countryAndNeighbors
 * and continentAndItsCountries fields in the MapContents class
 * 
 * @author Dheeraj As  - Team 13
 * @author Darwin Anirudh G  - Team 13
 */
public class MapParseController {

	private File file;
	private String currentLine;
	private String filePath;
	private File fileObject;
	private BufferedReader bufferReaderForFile;
	private MapContents mapContentsObj;
	private String mapAuthorName;
	private String[] splitUtllityString;
	private List<Continent> contitentList;
	private List<Country> countryList;
	private List<Country> adjCountry;
	private Continent continentObejct;
	private Country countryObject;
	private MapContents mapContentObject;
	private HashMap<Country, List<Country>> countryAndNeighbors;
	private HashMap<Continent, List<Country>> continentAndItsCountries;

	public void MapParser(String filePath) {
		try {

			fileObject = new File(filePath);

			bufferReaderForFile = new BufferedReader(new FileReader(fileObject));

			readMapElements(bufferReaderForFile);

			displayContinent();

			displayCountry();
			
			countryAndNeighboursMap();
			
			contitentAndCountriesMap();
			
		/*	displayContinentCountryMap();*/
			
			

		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void readMapElements(BufferedReader bufferReader) {
		try {
			contitentList = new ArrayList<Continent>();
			countryList = new ArrayList<Country>();
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
						System.out.println(currentLine);
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
							System.out.println(currentLine);
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
							/*mapContentObject.setCountryAndNeighbors(countryAndNeighbors);*/
							
							System.out.println("The country List size is "+countryList.size());

						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	public void countryAndNeighboursMap()
	{
		try
		{
			System.out.println("######  countryAndNeighbors Map size     #############"+countryAndNeighbors.size());
			
			for (Map.Entry<Country, List<Country>> entry : countryAndNeighbors.entrySet())
			{
				
				System.out.println("##### Key is ##### :"+entry.getKey().getCountryName() + "value size is   "+entry.getValue().size() );
				
			}
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	
	public void contitentAndCountriesMap()
	{
		try
		{
			System.out.println("##### Reading countryList ######");
			continentAndItsCountries = new HashMap<>();
			
			
			for(Continent continentInstance : contitentList)
			{
				System.out.println("Continent Name is "+continentInstance.getContinentName());
				Continent contientObj = new Continent(continentInstance.getContinentName());
				List<Country> counList = new ArrayList<Country>();
				
				for (Country countryInstance : countryList)
				{
					if(countryInstance.getBelongsToContinent().equals(continentInstance.getContinentName()))
					{
						counList.add(countryInstance);
					}
				}
				
				continentAndItsCountries.put(contientObj, counList);
			}
			
			for (Map.Entry<Continent, List<Country>> entry : continentAndItsCountries.entrySet())
			{
				System.out.println("##### Key is ##### :"+entry.getKey().getContinentName() + "value size is   "+entry.getValue().size() );
			}
			
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	public List<Country> getContinentCountries(String belongsToContinent) {
		try
		{
			List<Country> couList = new ArrayList<Country>(); 
			
			for (int i =0 ;i <countryList.size();i++ )
			{
				if (belongsToContinent.equals(countryList.get(i).getBelongsToContinent()))
				{
					
					System.out.println("###### Country name is ###########      :"+countryList.get(i).getCountryName());
					couList.add(countryList.get(i));
					
				}
				
			}
			
			return couList;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	
	
	
	
	public void displayContinentCountryMap()
	{
		try
		{
			for (Map.Entry<Continent, List<Country>> entry : continentAndItsCountries.entrySet())
			{
				System.out.println("##### Key is ##### :"+entry.getKey().getContinentName() + "value size is   "+entry.getValue().size() );
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}

	public void displayContinent() {
		try {

			for (Continent continentObejct : contitentList) {
				System.out.println(
						"###### The Continent Name is            ######### :" + continentObejct.getContinentName());
				System.out.println("###### The Continent Control value is ######### :"
						+ continentObejct.getContinentControlValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void displayCountry() {
		try {
			for (Country countryObject : countryList) {
				System.out.println(
						"###### The Country Name is            ######### :" + countryObject.getCountryName());
				System.out.println(
						"###### The Country belongs value is ######### :" + countryObject.getBelongsToContinent());
				System.out.println("###### The size of the adjacent country #######  :"
						+ countryObject.getNeighbouringCountries().size());
				
				  for (int i =0;i <countryObject.getNeighbouringCountries().size();i++ ) {
				  System.out.println("##### The neighbouring country of "+countryObject.
				  getCountryName() +"is #### :"
				  +countryObject.getNeighbouringCountries().get(i).getCountryName()); }
				 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}