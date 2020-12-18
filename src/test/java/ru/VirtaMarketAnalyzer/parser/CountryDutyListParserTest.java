package ru.VirtaMarketAnalyzer.parser;

import org.junit.jupiter.api.Test;
import ru.VirtaMarketAnalyzer.data.Country;
import ru.VirtaMarketAnalyzer.data.CountryDutyList;
import ru.VirtaMarketAnalyzer.main.Wizard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CountryDutyListParserTest {

    @Test
    void getAllCountryDutyListTest() {
        final List<Country> countries = new ArrayList<>();
        countries.add(new Country("2931", "Россия"));
        final Map<String, List<CountryDutyList>> map = CountryDutyListParser.getAllCountryDutyList(Wizard.host, "olga", countries);
        assertFalse(map.isEmpty());
        assertFalse(map.get("2931").isEmpty());
    }

    @Test
    void addDutySameCountryTest() throws IOException {
        final double price = 900.0;
        final double priceWithDuty = CountryDutyListParser.addDuty(Wizard.host, "olga", "310392", "310392", "1473", price);
        assertEquals(900.0, priceWithDuty);
    }

    @Test
    void addDutyTest() throws IOException {
        //Нукус (Узбекистан) -> Великие Луки (Россия, Северо-Запад)
        //Двигатель
        final double price = 900.0;
        final double priceWithDuty = CountryDutyListParser.addDuty(Wizard.host, "olga", "310392", "2931", "1473", price);
        assertEquals(981, priceWithDuty);
    }

    @Test
    void getTransportCostTest() throws Exception {
        //Нукус (Узбекистан) -> Великие Луки (Россия, Северо-Запад)
        //Двигатель
        final double transportCost1 = CountryDutyListParser.getTransportCost(Wizard.host, "olga", "310400", "331870", "1473");
        final double transportCost2 = CountryDutyListParser.getTransportCost(Wizard.host, "olga", "331870", "310400", "1473");
        assertEquals(24.6, transportCost1);
        assertEquals(transportCost1, transportCost2);
    }

    @Test
    void addDutyAndTransportCostTest() throws Exception {
        //Нукус (Узбекистан) -> Великие Луки (Россия, Северо-Запад)
        //Двигатель
        final double price = 900.0;
        final double dutyAndTransportCost = CountryDutyListParser.addDutyAndTransportCost(Wizard.host, "olga", "310392", "310392", "310400", "331870", "1473", price);
        assertEquals(981 + 24.6, dutyAndTransportCost);
    }
}