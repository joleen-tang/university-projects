package game;

import edu.monash.fit2099.engine.WeaponItem;

/**
 * Ranged weapon player can buy from vending machine
 * */
public class LaserGun extends WeaponItem {
    /**
     * This makes a new WeaponItem called Laser Gun.
     * The team has decided to give 50 damage to Laser Gun so it has the ability to kill a stegosaur in 2 or less 'zaps'
     *
     */
    public LaserGun() {
        super("LaserGun", 'L', 50, "zaps");
    }


}
