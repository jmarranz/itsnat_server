package inexp.groovyex;

/**
 *
 * @author jmarranz
 */
class FalseDB 
{
    def cities

    FalseDB(String name)
    {
        cities = new LinkedList<City>();
        cities.add(new City("Madrid"));
        cities.add(new City("Barcelona"));
        cities.add(new City("Bilbao"));
    }

    def getCityList() { return cities; }
}
