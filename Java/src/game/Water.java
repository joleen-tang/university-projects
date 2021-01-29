package game;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Ground;
import edu.monash.fit2099.engine.Location;
import game.dinosaurs.DinoCapabilities;

/**
 * Class representing a place covered in water
 * */
public class Water extends Ground {
    public Water(){
        super('~');

    }

    /**
     * Actors can only enter Water if they can fly
     * */
    @Override
    public boolean canActorEnter(Actor actor){
        return actor.hasCapability(DinoCapabilities.CanFly);
    }

    /**
     * Adds water items to Location where Water is every turn
     * */
    public void tick(Location location){
        super.tick(location);
        for(int i=0; i<location.getItems().size(); i++){
            if(!(location.getItems().get(i) instanceof WaterItem)){
                location.addItem(new WaterItem());
            }
        }
    }
}
