package ru.VirtaMarketAnalyzer.parser;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.PatternLayout;
import org.junit.jupiter.api.Test;
import ru.VirtaMarketAnalyzer.data.*;
import ru.VirtaMarketAnalyzer.main.Utils;
import ru.VirtaMarketAnalyzer.main.Wizard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CityParserTest {

    @Test
    void collectByTradeAtCitiesTest() throws IOException {
        final String realm = "olga";
        final List<Country> countries = new ArrayList<>();
        countries.add(new Country("2931", "Россия"));
        final List<City> cities = new ArrayList<>();
        final City city = new City("2931", "2932", "2933", "Москва", 10, 0, 0, 0, 0, null);
        cities.add(city);
        final List<Product> products = ProductInitParser.getTradingProducts(Wizard.host, realm);
        final List<Region> regions = CityInitParser.getRegions(Wizard.host, realm);
        final Map<String, List<CountryDutyList>> countriesDutyList = CountryDutyListParser.getAllCountryDutyList(Wizard.host, realm, countries);
        final List<TradeAtCity> stats = CityParser.collectByTradeAtCities(Wizard.host, realm, cities, products.get(0), countriesDutyList, regions);
        assertFalse(stats.isEmpty());
    }

    @Test
    void collectByTradeAtCitiesTest2() throws IOException {
        BasicConfigurator.configure(new ConsoleAppender(new PatternLayout("%d{ISO8601} [%t] %p %C{1} %x - %m%n")));
        final String realm = "vera";
        final List<Country> countries = new ArrayList<>();
        countries.add(new Country("423255", "Россия"));
        final List<City> cities = new ArrayList<>();
        final City city = new City("423255", "423256", "423257", "Москва", 10, 0, 0, 0, 0, null);
        cities.add(city);
        final Product product = ProductInitParser.getTradingProduct(Wizard.host, realm, "422717");
        final List<Region> regions = CityInitParser.getRegions(Wizard.host, realm);
        final Map<String, List<CountryDutyList>> countriesDutyList = CountryDutyListParser.getAllCountryDutyList(Wizard.host, realm, countries);
        final List<TradeAtCity> stats = CityParser.collectByTradeAtCities(Wizard.host, realm, cities, product, countriesDutyList, regions);
        assertFalse(stats.isEmpty());
    }
}