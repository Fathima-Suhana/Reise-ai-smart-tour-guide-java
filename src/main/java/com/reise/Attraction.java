package com.reise;

import java.util.List;

/** One attraction. open/close are in minutes from midnight, fee in local currency. */
public record Attraction(int id, String name, List<String> tags, int duration, int weight,
                         int open, int close, int crowd, int fee, double lat, double lon,
                         String history, String fact, String fact2) {}
