package com.reise;

import java.util.*;

/** Same countries, order and ids as the front-end, so id N here is P[N] in the page. */
public final class AttractionData {
    public static final Map<String, List<Attraction>> BY_COUNTRY = new LinkedHashMap<>();

    private static void add(String c, String n, String tags, int d, int w, double o, double z, int cr, int fee, double lat, double lon) {
        List<Attraction> l = BY_COUNTRY.computeIfAbsent(c, k -> new ArrayList<>());
        l.add(new Attraction(l.size(), n, List.of(tags.split(",")), d, w, (int) (o * 60), (int) (z * 60), cr, fee, lat, lon, "", "", ""));
    }

    static {
        add("France", "Eiffel Tower", "landmark,history", 90, 3, 9, 23, 4, 36, 48.8584, 2.2945);
        add("France", "Louvre Museum", "art,history", 150, 3, 9, 18, 4, 22, 48.8606, 2.3376);
        add("France", "Notre-Dame", "history,landmark", 60, 3, 8, 19, 4, 0, 48.8530, 2.3499);
        add("France", "Arc de Triomphe", "landmark,history", 45, 2, 10, 23, 3, 16, 48.8738, 2.2950);
        add("France", "Sacré-Cœur", "history,landmark,nature", 75, 2, 6, 22.5, 3, 0, 48.8867, 2.3431);
        add("France", "Musée d'Orsay", "art,history", 120, 2, 9.5, 18, 3, 16, 48.8600, 2.3266);
        add("France", "Luxembourg Gardens", "nature", 60, 2, 7.5, 21, 2, 0, 48.8462, 2.3372);
        add("France", "Palais Garnier", "art,landmark", 60, 2, 10, 17, 2, 15, 48.8720, 2.3316);
        add("France", "Père Lachaise", "history,nature", 90, 1, 8, 18, 1, 0, 48.8614, 2.3933);
        add("France", "Tuileries Garden", "nature,art", 45, 1, 7, 21, 2, 0, 48.8635, 2.3275);
        add("India", "Red Fort", "history,landmark", 120, 3, 9.5, 16.5, 3, 35, 28.6562, 77.2410);
        add("India", "India Gate", "landmark,history", 45, 3, 0, 24, 4, 0, 28.6129, 77.2295);
        add("India", "Qutub Minar", "history,landmark", 75, 3, 7, 17, 3, 35, 28.5245, 77.1855);
        add("India", "Humayun's Tomb", "history,art", 60, 2, 6, 18, 2, 35, 28.5933, 77.2507);
        add("India", "Lotus Temple", "landmark,art", 45, 2, 9, 17, 3, 0, 28.5535, 77.2588);
        add("India", "Lodhi Garden", "nature,history", 60, 1, 6, 20, 1, 0, 28.5931, 77.2197);
        add("Italy", "Colosseum", "history,landmark", 90, 3, 9, 19, 4, 18, 41.8902, 12.4922);
        add("Italy", "Roman Forum", "history", 75, 3, 9, 19, 3, 18, 41.8925, 12.4853);
        add("Italy", "Pantheon", "history,landmark,art", 40, 3, 9, 19, 4, 5, 41.8986, 12.4769);
        add("Italy", "Trevi Fountain", "art,landmark", 30, 2, 0, 24, 4, 0, 41.9009, 12.4833);
        add("Italy", "Vatican Museums", "art,history", 150, 3, 8, 18, 4, 20, 41.9065, 12.4536);
        add("Italy", "Villa Borghese", "nature,art", 90, 1, 7, 20, 2, 0, 41.9142, 12.4923);
        add("Japan", "Senso-ji", "history,landmark", 75, 3, 6, 17, 4, 0, 35.7148, 139.7967);
        add("Japan", "Meiji Shrine", "history,nature", 75, 3, 5, 18, 3, 0, 35.6764, 139.6993);
        add("Japan", "Tokyo Skytree", "landmark", 60, 2, 10, 21, 3, 2100, 35.7101, 139.8107);
        add("Japan", "Shibuya Crossing", "landmark", 30, 2, 0, 24, 3, 0, 35.6595, 139.7005);
        add("Japan", "Ueno Park", "nature,art", 90, 2, 5, 23, 2, 0, 35.7156, 139.7745);
        add("Japan", "Imperial Palace Gardens", "history,nature", 75, 2, 9, 16.5, 2, 0, 35.6852, 139.7565);
        add("UK", "Tower of London", "history,landmark", 120, 3, 9, 17.5, 4, 35, 51.5081, -0.0759);
        add("UK", "British Museum", "art,history", 150, 3, 10, 17, 3, 0, 51.5194, -0.1270);
        add("UK", "Buckingham Palace", "landmark,history", 45, 3, 9.5, 17, 3, 33, 51.5014, -0.1419);
        add("UK", "Westminster Abbey", "history,landmark", 60, 2, 9.5, 15.5, 3, 30, 51.4993, -0.1273);
        add("UK", "Hyde Park", "nature", 75, 1, 5, 24, 2, 0, 51.5073, -0.1657);
        add("UK", "Tate Modern", "art", 90, 2, 10, 18, 2, 0, 51.5076, -0.0994);
        TouristInfo.apply(BY_COUNTRY);
    }
    private AttractionData() {}
}
