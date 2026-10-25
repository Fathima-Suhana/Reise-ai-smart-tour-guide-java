package com.reise;

import java.util.*;

/** History and fun facts per attraction, in the same order as AttractionData. Rows: {history, fact, fact2}. */
final class TouristInfo {
    private static final Map<String, String[][]> T = Map.of(
        "France", new String[][]{
            {"Built by Gustave Eiffel's company for the 1889 World's Fair, it was meant to stand for only 20 years.", "The iron structure grows about 15 cm taller in summer heat.", "It was the world's tallest man-made structure until the Chrysler Building was finished in 1930."},
            {"A medieval fortress turned royal palace, opened as a public museum in 1793.", "Seeing every artwork for 30 seconds each would take about 200 days.", "It is the most visited museum in the world."},
            {"Gothic cathedral begun in 1163 on the Île de la Cité, the heart of medieval Paris.", "Distances from Paris are measured from a marker in front of it.", "Its tall spire was added in the 19th century by the architect Viollet-le-Duc."},
            {"Commissioned by Napoleon in 1806 to honour his armies; completed in 1836.", "Twelve avenues radiate from the square around it.", "Victor Hugo's body lay in state beneath the arch in 1885."},
            {"Basilica on Montmartre hill, consecrated in 1919 and visible across the city.", "Its dome offers one of the highest viewpoints in Paris.", "Its travertine stone releases a chalky substance in the rain, which keeps the basilica bright white."},
            {"Housed in a former railway station from 1900, now home to Impressionist masterpieces.", "Its giant station clock doubles as a museum window.", "It opened as a museum in 1986, after the old station was saved from demolition."},
            {"Created in 1612 for Marie de' Medici beside the Luxembourg Palace.", "Children have sailed toy boats on its pond for generations.", "The park's orchard keeps old French apple and pear varieties."},
            {"Opulent opera house completed in 1875, famous as the setting of The Phantom of the Opera.", "It reportedly has an underground lake beneath the stage.", "The auditorium chandelier weighs about seven tonnes."},
            {"Opened in 1804, it is among the most visited cemeteries in the world.", "Notable artists, writers and musicians are buried here.", "It was among the first garden cemeteries and inspired many others worldwide."},
            {"Laid out in 1564 for a royal palace that no longer exists.", "It links the Louvre to Place de la Concorde.", "The garden was redesigned in the 1660s by the landscape architect André Le Nôtre."}},
        "India", new String[][]{
            {"Mughal fortress built by Shah Jahan between 1639 and 1648 as the seat of the empire.", "The Prime Minister raises the national flag here every Independence Day.", "Its red sandstone walls give the fort its name, and it took about ten years to build."},
            {"A 42 m war memorial designed by Edwin Lutyens and completed in 1931.", "It is inscribed with the names of tens of thousands of soldiers.", "It is built from red Bharatpur stone."},
            {"A 73 m minaret begun in 1193, among the tallest brick minarets in the world.", "A nearby iron pillar has resisted rust for about 1,600 years.", "The complex is a UNESCO World Heritage Site."},
            {"Built in the 1560s, the first garden-tomb on the Indian subcontinent.", "Its design later inspired the Taj Mahal.", "It is also a UNESCO World Heritage Site, and it is said to have been commissioned by Humayun's widow."},
            {"Bahá'í house of worship completed in 1986, shaped like a lotus flower.", "It has 27 marble-clad petals.", "The architect Fariborz Sahba took inspiration from the lotus, a sacred flower in Indian culture."},
            {"Park surrounding 15th-century tombs of the Sayyid and Lodhi dynasties.", "Locals gather here for morning walks and yoga.", "The park was originally called Lady Willingdon Park."}},
        "Italy", new String[][]{
            {"Amphitheatre opened in AD 80, hosting gladiator contests for tens of thousands.", "A retractable awning once shaded the crowd.", "Its Latin name was the Flavian Amphitheatre."},
            {"The centre of public life in ancient Rome, with temples and basilicas.", "Julius Caesar's funeral took place here.", "Its stones were reused for centuries as a quarry for other buildings."},
            {"Temple rebuilt by Emperor Hadrian around AD 126, now a church.", "Its dome is still the largest unreinforced concrete dome.", "The portico has 16 giant granite columns brought from Egypt."},
            {"Baroque fountain completed in 1762 and fed by an ancient aqueduct.", "Coins tossed in are collected for charity.", "It featured in the 1960 film La Dolce Vita."},
            {"Collections grown from the early 1500s, ending in the Sistine Chapel.", "Michelangelo painted the chapel ceiling between 1508 and 1512.", "The spiral staircase at the exit is a double helix, so people can go up and down without meeting."},
            {"Landscaped park laid out by Cardinal Scipione Borghese in the 1600s.", "Its gallery houses sculptures by Bernini.", "The park became public in the early 1900s when the city bought it."}},
        "Japan", new String[][]{
            {"Tokyo's oldest temple, founded in 645 in Asakusa.", "Its Kaminarimon gate hangs a giant red lantern.", "The temple honours Kannon, the goddess of mercy, said to have been found in the Sumida River by two fishermen."},
            {"Shinto shrine completed in 1920, dedicated to Emperor Meiji.", "Its forest was planted with about 100,000 donated trees.", "Its giant torii gates are made of cypress wood."},
            {"A 634 m broadcasting tower that opened in 2012.", "It is the tallest tower in the world.", "Its central pillar draws on the design of traditional pagodas to resist earthquakes."},
            {"The famous scramble intersection outside Shibuya Station.", "Thousands of people can cross in a single signal change.", "The crossing has appeared in many films and is a symbol of Tokyo."},
            {"Opened in 1873 as one of Japan's first public parks.", "It is a top spot for cherry blossoms and holds several museums.", "Japan's first zoo opened in this park in 1882."},
            {"Former site of Edo Castle, seat of the Tokugawa shoguns.", "Massive stone walls of the old castle still remain.", "The moat and walls are what remain of Edo Castle, once one of the largest castles in the world."}},
        "UK", new String[][]{
            {"Fortress begun by William the Conqueror around 1078.", "Ravens are kept here by tradition.", "It has served as a royal palace, a prison, a mint and even a zoo."},
            {"Founded in 1753 and opened to the public in 1759.", "It holds the Rosetta Stone.", "It began with about 71,000 objects from the collection of Sir Hans Sloane."},
            {"Became the monarch's London residence in 1837 under Queen Victoria.", "The Changing of the Guard draws crowds outside.", "The palace has 775 rooms."},
            {"Coronation church of English monarchs since 1066.", "Many monarchs and famous figures are buried here.", "Over 3,000 people are buried or commemorated here."},
            {"Former royal hunting ground opened to the public in 1637.", "Speakers' Corner has hosted open debate for generations.", "The Serpentine lake was created in 1730 for Queen Caroline."},
            {"Modern art museum in a former power station, opened in 2000.", "Its huge Turbine Hall hosts large installations.", "The building's chimney is 99 metres tall."}});

    static void apply(Map<String, List<Attraction>> all) {
        T.forEach((country, rows) -> {
            List<Attraction> l = all.get(country);
            for (int i = 0; i < rows.length; i++) {
                Attraction a = l.get(i);
                l.set(i, new Attraction(a.id(), a.name(), a.tags(), a.duration(), a.weight(), a.open(), a.close(),
                        a.crowd(), a.fee(), a.lat(), a.lon(), rows[i][0], rows[i][1], rows[i][2]));
            }
        });
    }
    private TouristInfo() {}
}
