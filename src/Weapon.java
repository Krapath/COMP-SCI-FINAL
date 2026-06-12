/**
 * weapon: base class for all weapons.
 */
public class Weapon extends GameObject {
    /** the game instance this weapon belongs to. */
    PolygonGame game;

    /** default damage for weapons. */
    private static final int DEFAULT_WEAPON_DAMAGE = 1;
    static int damage = DEFAULT_WEAPON_DAMAGE;

    /** the type of the weapon. */
    public String weaponType;
    /** the display name of the weapon. */
    public String weaponName;

    /**
     * weapon constructor.
     * game the game instance, weaponType the weapon type, weaponName the weapon name
     */
    public Weapon(PolygonGame game, String weaponType, String weaponName) {
        this.game = game;
        this.weaponType = weaponType;
        this.weaponName = weaponName;
    }

    /**
     * act: update logic for the weapon each frame.
     */
    public void act(){
        
    }

}