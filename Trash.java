package Room_related;

public class Trash {
    // Attributter:
    private String name;
    private TrashType trashType;
    private int x;
    private int y;

    // Constructor: objects (trash) are made here
    public Trash (String name, TrashType trashType, int x, int y){
        this.name = name;
        this.trashType = trashType;
        this.x = x;
        this.y = y;
    }

    public String getName()
    {
        return name;
    }

    public TrashType getTrashType(){
        return trashType;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
