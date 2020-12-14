import command.*;
import Room_related.*;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Map;


public class Game {
    // Der erklæres to variabler
    private Parser parser;
    private Room currentRoom;
    private Player player;
    private boolean wantsToRestart = false;
    private Container metalContainer = new Container("Metal Container", TrashType.METAL);
    private Container hazardousContainer = new Container("Hazardous Container", TrashType.HAZARDOUSWASTE);
    private Container residualContainer = new Container("Residual Container", TrashType.RESIDUALWASTE);
    private Container plasticContainer = new Container("Plastic Container", TrashType.PLASTIC);
    private Room park, beach, street, conSite, playGround, home, reCenter;
    private Trash trash = new Trash("can", TrashType.METAL, 13, 13);

    // constructor - kører metode CreateRooms og laver et nyt objekt
    public Game() {
        parser = new Parser();
    }

    // metode
    private void createRooms() {
        // rum dannes som objekter
        park = new Room("You've reached the park... refreshing.", coordinateSystemPARK);
        beach = new Room("You've reached the beach. Lots of sand... everywhere!", coordinateSystemBEACH);
        street = new Room("You're walking through the street. Ahh the sweet smell of... pollution?", coordinateSystemStreet);
        conSite = new Room("At a construction site. Construction will eventually resume at an unspecified time in the future.", coordinateSystemCONSTRUCTIONSITE);
        playGround = new Room("You've reached the playGround. Everyone's favourite place to be!", coordinateSystemPLAYGROUND);
        home = new Room("You're at home. Smells kinda funny in here.", coordinateSystemHOME);
        reCenter = new RecyclingCenter("You're at the recycling center. The place where you... well... SORT YOUR TRASH!!", coordinateSystemRECYCLECENTER);

        home.createTrash();
        playGround.createTrash();
        conSite.createTrash();
        beach.createTrash();
        street.createTrash();
        park.createTrash();

        currentRoom = street;
    }


    //ny metode
    public void play(String name) {
        player = new Player(name);
        createRooms();
    }

    public String getWelcomeText() {
        String string = "Welcome to the World of TRASH!\n" +
                "World of TRASH is a new, incredibly cool trash sorting game!\n" +
                "Your mission is to walk around the world and collect TRASH!\n" +
                "Yeah i know, it sounds absolutely amazing doesn't it\n" +
                "Oh yeah, and by the way, you get points depending on your sorting skills!\n" +
                "If you need more information about how to play the game, press the \"help\" button\n" +
                "Well, that's all for now. Good luck adventurer, and may the gods of trash watch over you!";
        return string;
    }

    private int processCommand(Command command) {
        int wantToQuit = 0;

        CommandWord commandWord = command.getCommandWord();

        if (commandWord == CommandWord.UNKNOWN) {
            System.out.println("I don't know what you mean...");
            return 0;
        }

        if (commandWord == CommandWord.HELP) {
            //printHelp();
        } else if (commandWord == CommandWord.GO) {
            goRoom(command);
        } else if (commandWord == commandWord.RESTART) {
            //wantToQuit = restart(command);
        } else if (commandWord == CommandWord.QUIT) {
            wantToQuit = quit(command);
        } else if (commandWord == CommandWord.INSPECTITEM) {
            /*player.inspectItem(command);*/
        }
        /*
        else if (commandWord == CommandWord.PICKUP) {
            player.pickUp(command);
        }

         */
        else if (commandWord == CommandWord.OPENINVENTORY) {
            player.openInventory(command);
        }
        /*
        else if (commandWord == CommandWord.SEARCH) {
            player.search();
        }
        else if (commandWord == CommandWord.DROP) {
            //player.dropItem(command);
        }
        else if (commandWord == CommandWord.HANDBOOK) {
            handbook();
        }

         */
        return wantToQuit;
    }

    private void goRoom(Command command) {
        if (!command.hasSecondWord()) {
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        } else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }

    public void restart(TextArea textArea) {
        if (wantsToRestart == true) {
            textArea.setText("Restarting game");
            play("Tobber00");
            wantsToRestart = false;
        } else {
            textArea.setText("Are you sure you want to restart?\n" +
                    "Press the \"restart\" button again if you do\n" +
                    "Press the \"help\" button if you don't");
            wantsToRestart = true;
        }
    }

    private int quit(Command command) {
        if (command.hasSecondWord()) {
            System.out.println("Quit what?");
            return 0;
        } else {
            return 2;
        }
    }


    public Room getCurrentRoom() {
        return currentRoom;
    }

    public Player getPlayer() {
        return player;
    }


    public String getHelp() {
        wantsToRestart = false;
        String string = "Welcome to the world of TRASH! \n" +
                "A trash collecting/sorting game \n" +
                "You can walk around the world using \"W, A, S, D\"\n" +
                "And collect trash using the \"F\" key, and sort it at a container using the \"G\" key \n" +
                "You get a point by sorting the trash correctly, but lose one if you sort it incorrectly \n";
        return string;
    }


    public void loadTrash(ImageView[] trashImages) {
        for (int i = 0; i < currentRoom.getTrashArrayList().size(); i++) {
            trashImages[i].setImage(new Image(currentRoom.getTrashArrayList().get(i).getName() + ".png"));
            trashImages[i].setX(currentRoom.getTrashArrayList().get(i).getX() * 40);
            trashImages[i].setY(currentRoom.getTrashArrayList().get(i).getY() * 40);
        }
        int dif = trashImages.length - currentRoom.getTrashArrayList().size();
        if (dif != 0) {
            for (int i = trashImages.length - 1; i > dif; i--) {
                trashImages[i].setImage(new Image("empty.png"));
                trashImages[i].setX(0);
                trashImages[i].setY(0);
            }
        }

    }

    public void moveRoom(MapObjekt object, ImageView room, ImageView playerImage, ImageView[] trashImages) {
        switch (object) {
            //tjekker om spillers koordinater matcher med et portalObjekt, og tjekker hvilket for at se hvor spiller skal sendes hen
            case STREETWEST:
                playerImage.setX(15*40);
                playerImage.setY(15*40);
                player.setX(15);
                player.setY(15);
                room.setImage(new Image("beach.png"));
                loadTrash(trashImages);
                currentRoom = beach;
                break;
            case BEACHEAST:
                playerImage.setX(15*40);
                playerImage.setY(15*40);
                player.setX(15);
                player.setY(15);
                room.setImage(new Image("street.jpg"));
                loadTrash(trashImages);
                currentRoom = street;
                break;
            case PARKNORTH:
                playerImage.setX(15*40);
                playerImage.setY(15*40);
                player.setX(15);
                player.setY(15);
                room.setImage(new Image("street.jpg"));
                loadTrash(trashImages);
                currentRoom = street;
                break;
            case PLAYGROUNDEAST:
                playerImage.setX(15*40);
                playerImage.setY(15*40);
                player.setX(15);
                player.setY(15);
                room.setImage(new Image("concenter.png"));
                loadTrash(trashImages);
                currentRoom = conSite;
                break;
            case STREETNORTH:
                playerImage.setX(15*40);
                playerImage.setY(15*40);
                player.setX(15);
                player.setY(15);
                room.setImage(new Image("recenter.png"));
                loadTrash(trashImages);
                currentRoom = reCenter;
                break;
            case STREETEAST:
                playerImage.setX(15*40);
                playerImage.setY(15*40);
                player.setX(15);
                player.setY(15);
                room.setImage(new Image("playGround.png"));
                loadTrash(trashImages);
                currentRoom = playGround;
                break;
            case HOMESOUTH:
                playerImage.setX(15*40);
                playerImage.setY(15*40);
                player.setX(15);
                player.setY(15);
                room.setImage(new Image("playGround.png"));
                loadTrash(trashImages);
                currentRoom = playGround;
                break;
            case CONCENTERWEST:
                playerImage.setX(15*40);
                playerImage.setY(15*40);
                player.setX(15);
                player.setY(15);
                room.setImage(new Image("playGround.png"));
                loadTrash(trashImages);
                currentRoom = playGround;
                break;
            case PLAYGROUNDNORTH:
                playerImage.setX(15*40);
                playerImage.setY(15*40);
                player.setX(15);
                player.setY(15);
                room.setImage(new Image("home.jpg"));
                loadTrash(trashImages);
                currentRoom = home;
                break;
            case PLAYGROUNDWEST:
                playerImage.setX(15*40);
                playerImage.setY(15*40);
                player.setX(15);
                player.setY(15);
                room.setImage(new Image("street.jpg"));
                loadTrash(trashImages);
                currentRoom = street;
                break;
            case RECCENTERSOUTH:
                playerImage.setX(15*40);
                playerImage.setY(15*40);
                player.setX(15);
                player.setY(15);
                room.setImage(new Image("street.jpg"));
                loadTrash(trashImages);
                currentRoom = street;
                break;
            case STREETSOUTH:
                playerImage.setX(15*40);
                playerImage.setY(15*40);
                player.setX(15);
                player.setY(15);
                room.setImage(new Image("park.png"));
                loadTrash(trashImages);
                currentRoom = park;
                break;
        }
    }

    Object[][] coordinateSystemHOME =
            {
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.HOMESOUTH, MapObjekt.HOMESOUTH, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE}

            };

    Object[][] coordinateSystemStreet =
            {
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.STREETNORTH, MapObjekt.STREETNORTH, MapObjekt.STREETNORTH, MapObjekt.STREETNORTH, MapObjekt.STREETNORTH, MapObjekt.STREETNORTH, MapObjekt.STREETNORTH, MapObjekt.STREETNORTH, MapObjekt.STREETNORTH, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.STREETWEST, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.STREETEAST},
                    {MapObjekt.STREETWEST, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.STREETEAST},
                    {MapObjekt.STREETWEST, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.STREETEAST},
                    {MapObjekt.STREETWEST, null, null, null, null, null, null, null, null, null, null, null, trash, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.STREETEAST},
                    {MapObjekt.STREETWEST, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.STREETEAST},
                    {MapObjekt.STREETWEST, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.STREETEAST},
                    {MapObjekt.STREETWEST, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.STREETEAST},
                    {MapObjekt.STREETWEST, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.STREETEAST},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.STREETSOUTH, MapObjekt.STREETSOUTH, MapObjekt.STREETSOUTH, MapObjekt.STREETSOUTH, MapObjekt.STREETSOUTH, MapObjekt.STREETSOUTH, MapObjekt.STREETSOUTH, MapObjekt.STREETSOUTH, MapObjekt.STREETSOUTH, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE}
            };
    Object[][] coordinateSystemBEACH =
            {
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.BEACHEAST},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.BEACHEAST},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.BEACHEAST},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            };
    Object[][] coordinateSystemPLAYGROUND =
            {
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.PLAYGROUNDNORTH, MapObjekt.PLAYGROUNDNORTH, MapObjekt.PLAYGROUNDNORTH, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.PLAYGROUNDWEST, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.PLAYGROUNDEAST},
                    {MapObjekt.PLAYGROUNDWEST, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.PLAYGROUNDEAST},
                    {MapObjekt.PLAYGROUNDWEST, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.PLAYGROUNDEAST},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
                    {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE}
            };
    Object[][] coordinateSystemCONSTRUCTIONSITE =
            {
            {MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE,null,null,null,null,null,null,null,null,null,null,null,null,null,null,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE,null,null,null,null,null,null,null,null,null,null,null,null,null,null,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE,null,null,null,null,null,null,null,null,null,null,null,null,null,null,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE,null,null,null,null,null,null,null,null,null,null,null,null,null,null,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE,null,null,null,null,null,null,null,null,null,null,null,null,null,null,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE,null,null,null,null,null,null,null,null,null,null,null,null,null,null,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE,null,null,null,null,null,null,null,null,null,null,null,null,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE,null,null,null,null,null,null,null,null,null,null,null,null,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE,null,null,null,null,null,null,null,null,null,null,null,null,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE,null,null,null,null,null,null,null,null,null,null,null,null,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE,null,null,null,null,null,null,null,null,null,null,null,null,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE,null,null,null,null,null,null,null,null,null,null,null,null,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE},
            {MapObjekt.CONCENTERWEST,null,null,null,null,null,null,null,null,null,null,null,null,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE},
            {MapObjekt.CONCENTERWEST,null,null,null,null,null,null,null,null,null,null,null,null,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE},
            {MapObjekt.CONCENTERWEST,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,MapObjekt.NONWALKABLE},
            {MapObjekt.CONCENTERWEST,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,MapObjekt.NONWALKABLE},
            {MapObjekt.CONCENTERWEST,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,MapObjekt.NONWALKABLE},
            {MapObjekt.CONCENTERWEST,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE,null,null,null,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE,null,null,null,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE,null,null,null,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE,null,null,null,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,null,null,null,null,null,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE,MapObjekt.NONWALKABLE}
            };

    Object[][] coordinateSystemPARK = {
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.PARKNORTH, MapObjekt.PARKNORTH, MapObjekt.PARKNORTH, MapObjekt.PARKNORTH, MapObjekt.PARKNORTH, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.PARKNORTH, MapObjekt.PARKNORTH, MapObjekt.PARKNORTH, MapObjekt.PARKNORTH, MapObjekt.NONWALKABLE, MapObjekt.PARKNORTH, MapObjekt.PARKNORTH, MapObjekt.PARKNORTH, MapObjekt.PARKNORTH, MapObjekt.PARKNORTH, MapObjekt.PARKNORTH, MapObjekt.PARKNORTH, MapObjekt.PARKNORTH, MapObjekt.PARKNORTH, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, null, null, null},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE},
            {null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {null, null, null, null, MapObjekt.NONWALKABLE, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE}
        };
    Object[][] coordinateSystemRECYCLECENTER = {
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, hazardousContainer, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, plasticContainer, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE },
            {MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, metalContainer, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, residualContainer, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, null, null, null, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE},
            {MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.RECCENTERSOUTH, MapObjekt.RECCENTERSOUTH, MapObjekt.RECCENTERSOUTH, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE, MapObjekt.NONWALKABLE}

    };
}
