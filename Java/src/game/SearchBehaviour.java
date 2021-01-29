package game;

import edu.monash.fit2099.engine.*;

import java.util.ArrayList;

/**
 * Superclass with methods for getting nearby locations, type casting
 * */
public abstract class SearchBehaviour implements Behaviour{
    /**
     * Method to get an ArrayList of Locations at a specified distance away
     *
     * @param distance integer representing the distance from the current location
     * @param currentLocation the current Location to get surrounding Locations around
     * @param map GameMap where currentLocation is located
     * @return surroundings An arraylist containing numerous Locations 'distance' squares away
     * */
    protected ArrayList<Location> getSurroundings(int distance, Location currentLocation, GameMap map){
        NumberRange validX=map.getXRange();
        NumberRange validY=map.getYRange();
        int currentX=currentLocation.x();
        int currentY=currentLocation.y();
        ArrayList<Location> surroudings= new ArrayList<>();
        //for every value of x in range
        for (int x=0; x<=distance; x++){
            surroudings.addAll(getYLocation(validX, validY, currentX+x, currentY, distance, map));
            if (x!=0){
                surroudings.addAll(getYLocation(validX, validY, currentX-x, currentY, distance, map));
            }
        }
        //for every value of y in range (excluding edges as corners already covered in previous x loop)
        for (int y=0; y<=distance-1; y++){
            surroudings.addAll(getXLocation(validX, validY, currentX, currentY+y, distance, map));
            if (y!=0){
                surroudings.addAll(getXLocation(validX, validY, currentX, currentY-y, distance, map));
            }
        }
        return surroudings;
    }

    /**
     * For a given x-value, returns a collection of Locations in row-X on the gameMap that are within a certain
     * distance from the current Location
     *
     * @param validX a range of valid X values for the gameMap
     * @param validY a range of valid Y values for the gameMap
     * @param xVal the x-Value for searched locations
     * @param currentY the y-Value of the original Location
     * @param distance distance from the original Location
     * @param map GameMap where original Location is located
     * @return temp ArrayList of Locations
     * */
    private ArrayList<Location> getYLocation(NumberRange validX, NumberRange validY, int xVal, int currentY, int distance, GameMap map){
        ArrayList<Location> temp = new ArrayList<>();
        if (validateXY(validX, validY, xVal, currentY+distance)){
            temp.add(map.at(xVal, currentY+distance));
        }
        if (validateXY(validX, validY, xVal, currentY-distance) && distance!=0){
            temp.add(map.at(xVal, currentY-distance));
        }
        return temp;
    }

    /**
     * For a given x-value, returns a collection of Locations in row-X on the gameMap that are within a certain
     * distance from the current Location
     *
     * @param validX a range of valid X values for the gameMap
     * @param validY a range of valid Y values for the gameMap
     * @param yVal the y-Value for searched locations
     * @param currentX the x-Value of the original Location
     * @param distance distance from the original Location
     * @param map GameMap where original Location is located
     * @return temp ArrayList of Locations
     * */
    private ArrayList<Location> getXLocation(NumberRange validX, NumberRange validY, int currentX, int yVal, int distance, GameMap map){
        ArrayList<Location> temp = new ArrayList<>();
        if (validateXY(validX, validY, currentX+distance, yVal)){
            temp.add(map.at(currentX+distance, yVal));
        }
        if (validateXY(validX, validY, currentX-distance, yVal) && distance!=0){
            temp.add(map.at(currentX-distance, yVal));
        }
        return temp;
    }

    /**
     * Method that checks whether the given x-Value and y-Value point to a valid Location
     *
     * @param validX a collection of valid x-values
     * @param validY a collection of valid y-values
     * @param xVal the x-value to check
     * @param yVal the y-value to check
     * @return true if x and y-value are valid integers
     * */
    private boolean validateXY(NumberRange validX, NumberRange validY, int xVal, int yVal){
        return validX.contains(xVal) && validY.contains(yVal);
    }

    /**
     * Casts an actor as a dinosaur
     *
     * @param actortoDinosaur an Actor to be casted
     * @return the Actor as a Dinosaur or null if actor is not a dinosaur
     * */
    protected Dinosaur asDinosaur(Actor actortoDinosaur) {
        return actortoDinosaur instanceof Dinosaur ? (Dinosaur) actortoDinosaur : null;
    }
}
