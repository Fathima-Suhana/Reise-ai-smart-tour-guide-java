package com.reise;

import java.util.*;

/** Greedy planner: best interest-score per minute that fits opening hours and the time budget. */
public final class Planner {
    public record Step(int id, String name, int travel, int at, int waitMin) {}
    public record Summary(int used, int left, int cost, int fin) {}
    public record Plan(List<Step> stops, Summary summary) {}

    static final double WALK_M_PER_MIN = 80;

    static double meters(Attraction a, Attraction b) {
        double r = Math.PI / 180, dLat = (b.lat() - a.lat()) * r, dLon = (b.lon() - a.lon()) * r;
        double h = Math.pow(Math.sin(dLat / 2), 2) + Math.cos(a.lat() * r) * Math.cos(b.lat() * r) * Math.pow(Math.sin(dLon / 2), 2);
        return 12742000 * Math.asin(Math.sqrt(h));
    }

    private static double score(Attraction p, Set<String> tags) {
        long m = p.tags().stream().filter(tags::contains).count();
        double crowdPenalty = tags.contains("quiet") ? (p.crowd() - 1) * 1.5 : 0;
        return Math.max(0.5, m * 3 + p.weight() - crowdPenalty);
    }

    public static Plan plan(List<Attraction> all, int startId, double hours, int startHour, Set<String> tags, Set<Integer> skip) {
        int t0 = startHour * 60, end = t0 + (int) Math.round(hours * 60), now = t0, cost = 0;
        Attraction cur = all.get(startId);
        List<Step> out = new ArrayList<>();
        Set<Integer> used = new HashSet<>();
        while (true) {
            Attraction best = null; double bestV = 0; int bestStart = 0, bestTravel = 0;
            for (Attraction p : all) {
                if (used.contains(p.id()) || skip.contains(p.id())) continue;
                if (p.tags().stream().noneMatch(tags::contains) && p.weight() < 3) continue;
                int tr = (int) Math.round(meters(cur, p) / WALK_M_PER_MIN);
                int arrive = now + tr, s0 = Math.max(arrive, p.open());
                if (s0 - arrive > 60 || s0 + p.duration() > p.close() || s0 + p.duration() > end) continue;
                double v = score(p, tags) / (s0 + p.duration() - now);
                if (v > bestV) { bestV = v; best = p; bestStart = s0; bestTravel = tr; }
            }
            if (best == null) break;
            out.add(new Step(best.id(), best.name(), bestTravel, bestStart, bestStart - (now + bestTravel)));
            now = bestStart + best.duration(); cur = best; used.add(best.id()); cost += best.fee();
        }
        return new Plan(out, new Summary(now - t0, end - now, cost, now));
    }
    private Planner() {}
}
