package ru.serega6531.steamapitest;

import com.google.gson.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final String APIKEY = "YOURKEYGOESHERE";
    private static final String resolveIdURL = "http://api.steampowered.com/ISteamUser/ResolveVanityURL/v0001/?key=%s&vanityurl=%s";
    private static final String getGamesURL = "http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=%s&steamid=%s&include_appinfo=1";
    private static final String gameUrl = "http://steamcommunity.com/app/%d";

    private static final JsonParser parser = new JsonParser();

    public static void main(String[] args) throws MalformedURLException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите customURL или steamID64 первого игрока");
        String id1 = scanner.nextLine();
        System.out.println("Введите customURL или steamID64 второго игрока");
        String id2 = scanner.nextLine();

        if(id1.length() != 17)
            id1 = customURLToId(id1);

        if(id2.length() != 17)
            id2 = customURLToId(id2);

        if(id1 == null || id2 == null)
            return;

        System.out.println("Ищем обшие игры...");

        List<SteamGame> list1 = getPlayerGames(id1);
        List<SteamGame> list2 = getPlayerGames(id2);
        List<SteamGame> both = new ArrayList<>();

        for(SteamGame game : list1){
            if(list2.contains(game))
                both.add(game);
        }

        System.out.println(both.toString());
    }

    private static String customURLToId(String name){
        try {
            JsonObject res = parser.parse(new InputStreamReader(new URL(String.format(resolveIdURL, APIKEY, name)).openStream())).getAsJsonObject();
            if(res.has("response")){
                res = res.get("response").getAsJsonObject();
                if(res.get("success").getAsInt() == 1){
                    return res.get("steamid").getAsString();
                }
            }

            System.out.println("Ошибка получения steamID64 для игрока: " + res.toString());
            return null;
        } catch (IOException e) {
            System.out.println("Ошибка получения steamID64 для игрока: " + e.getLocalizedMessage());
            return null;
        }
    }

    private static List<SteamGame> getPlayerGames(String id){
        try {
            JsonObject res = parser.parse(new InputStreamReader(new URL(String.format(getGamesURL, APIKEY, id)).openStream())).getAsJsonObject();

            JsonArray jsonArray = res.get("response").getAsJsonObject().get("games").getAsJsonArray();

            List<SteamGame> games = new ArrayList<>();
            for (JsonElement el : jsonArray) {
                JsonObject gameObj = el.getAsJsonObject();

                int appid = gameObj.get("appid").getAsInt();
                String name = gameObj.get("name").getAsString();

                games.add(new SteamGame(appid, name));
            }

            return games;
        } catch (JsonSyntaxException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

}
