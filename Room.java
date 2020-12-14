package Room_related;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.HashMap;

public class Room {
    private String description;

    // Trash instance variable:
    private ArrayList<Trash> trashArrayList; // creates an ArrayList
    private HashMap<String, Room> exits; //?
    private Object[][] coordinateSystem;
    private Random random = new Random();

    public Room(String description, Object[][] coordinateSystem)
    {
        this.description = description;
        exits = new HashMap<String, Room>();

        this.coordinateSystem = coordinateSystem;
        trashArrayList = new ArrayList<Trash>(5);

    }

    public void setExit(String direction, Room neighbor)
    {
        exits.put(direction, neighbor);
    }

    public String getShortDescription()
    {
        return description;
    }

    public String getLongDescription()
    {
        return "You are " + description + ".\n" + getExitString();
    }

    private String getExitString()
    {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }

    public Room getExit(String direction)
    {
        return exits.get(direction);
    }

    public Object[][] getCoordinateSystem() {
        return coordinateSystem;
    }

    public void setCoordinateSystem(Object[][] coordinateSystem) {
        this.coordinateSystem = coordinateSystem;
    }

    public void createTrash(){
        // if statement der tjekker om man er på koordinater, hvor spilleren ikke kan gå hen

        // for loop med math.random til at oprette trash objekter tilfældigt i koordinatsystemet for hvert room
        for(int i = 0; i < 5; i++){

            Trash trash;
            String[][] trashArray = {{"can","aluminium foil"},{"battery", "syringe"},{"pizza box", "milk carton"},{"plastic bag", "plastic bottle"}};

            int x = random.nextInt(28);
            int y = random.nextInt(28);

            int type = random.nextInt(4);
            int name = random.nextInt(trashArray[type].length);


            if (type < 4 && trashArray[type].length >= name) {
                TrashType trashType;
                switch (type) {
                    case 0:
                        trashType = TrashType.METAL;
                        break;
                    case 1:
                        trashType = TrashType.HAZARDOUSWASTE;
                        break;
                    case 2:
                        trashType = TrashType.RESIDUALWASTE;
                        break;
                    case 3:
                        trashType = TrashType.PLASTIC;
                        break;

                    default:
                        trashType = TrashType.ERROR;
                }
                trash = new Trash(trashArray[type][name], trashType, x, y);

                System.out.println(x + ", " + y);
                if (coordinateSystem[x][y] == null) {
                    trashArrayList.add(trash);
                }
                else {
                    i--;
                }
            }
        }
    }

    public ArrayList<Trash> getTrashArrayList() {
        return trashArrayList;
    }
}
