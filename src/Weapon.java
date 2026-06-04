public class Weapon extends GameObject {
    PolygonGame game;

    public int damage = 1; 

    public String weaponType;
    public String weaponName;


    public Weapon(PolygonGame game, String weaponType, String weaponName) {
        this.game = game;
        this.weaponType = weaponType;
        this.weaponName = weaponName;
    }

    public void act(){
        
    }

}