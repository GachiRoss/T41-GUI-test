package command;

public enum CommandWord {
    GO("go"), QUIT("quit"), HELP("help"), UNKNOWN("?"), PICKUP("pickUp"), DROP("drop"), RESTART("restart"), OPENINVENTORY("openInventory"), INSPECTITEM("inspectItem"), SEARCH("search"), HANDBOOK("handbook");

    private String commandString;

    CommandWord(String commandString)
    {
        this.commandString = commandString;
    }

    public String toString()
    {
        return commandString;
    }
}
