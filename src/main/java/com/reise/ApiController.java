package com.reise;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
class ApiController {

    @GetMapping("/countries")
    Set<String> countries() { return AttractionData.BY_COUNTRY.keySet(); }

    @GetMapping("/attractions")
    List<Attraction> attractions(@RequestParam String country) { return find(country); }

    @GetMapping("/plan")
    Planner.Plan plan(@RequestParam String country,
                      @RequestParam(defaultValue = "0") int start,
                      @RequestParam(defaultValue = "5") double hrs,
                      @RequestParam(defaultValue = "9") int t0,
                      @RequestParam(defaultValue = "") String tags,
                      @RequestParam(defaultValue = "") String skip) {
        List<Attraction> all = find(country);
        if (start < 0 || start >= all.size()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown start id");
        if (hrs < 0.5 || hrs > 14 || t0 < 0 || t0 > 23) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Time out of range");
        return Planner.plan(all, start, hrs, t0, csv(tags), csv(skip).stream().map(Integer::valueOf).collect(Collectors.toSet()));
    }

    private static List<Attraction> find(String country) {
        List<Attraction> l = AttractionData.BY_COUNTRY.get(country);
        if (l == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown country");
        return l;
    }
    private static Set<String> csv(String s) {
        return s.isBlank() ? Set.of() : Arrays.stream(s.split(",")).map(String::trim).collect(Collectors.toSet());
    }
}
