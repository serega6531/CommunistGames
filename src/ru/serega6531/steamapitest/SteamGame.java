package ru.serega6531.steamapitest;

public class SteamGame {

    private int id;
    private String name;

    public SteamGame(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;

        if(!(o instanceof SteamGame))
            return false;

        SteamGame game = ((SteamGame) o);
        return id == game.id && name.equals(game.name);
    }

    @Override
    public String toString() {
        return name;
    }
}
