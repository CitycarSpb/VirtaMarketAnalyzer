package ru.VirtaMarketAnalyzer.parser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.VirtaMarketAnalyzer.data.Country;
import ru.VirtaMarketAnalyzer.data.Region;
import ru.VirtaMarketAnalyzer.main.Wizard;
import ru.VirtaMarketAnalyzer.scrapper.Downloader;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by cobr123 on 24.04.2015.
 */
public final class CityInitParser {
    private static final Logger logger = LoggerFactory.getLogger(CityInitParser.class);

    public static Region getRegion(final String host, final String realm, final String id) throws IOException {
        final Optional<Region> regionOpt = getRegions(host, realm).stream().filter(region -> region.getId().equals(id)).findFirst();
        if (!regionOpt.isPresent()) {
            throw new IllegalArgumentException("Не найден регион с id '" + id + "'");
        }
        return regionOpt.get();

    }

    public static List<Region> getRegions(final String host, final String realm) throws IOException {
        final String lang = (Wizard.host.equals(host) ? "ru" : "en");
        final String url = host + "api/" + realm + "/main/geo/region/browse?lang=" + lang;

        final List<Region> list = new ArrayList<>();
        try {
            final String json = Downloader.getJson(url);
            final Gson gson = new Gson();
            final Type mapType = new TypeToken<Map<String, Map<String, Object>>>() {
            }.getType();
            final Map<String, Map<String, Object>> mapOfRegions = gson.fromJson(json, mapType);

            for (final Map.Entry<String, Map<String, Object>> entry : mapOfRegions.entrySet()) {
                final Map<String, Object> region = entry.getValue();

                final String country_id = region.get("country_id").toString();
                final String id = region.get("id").toString();
                final String caption = region.get("name").toString();
                final double incomeTaxRate = Double.parseDouble(region.get("tax").toString());

                list.add(new Region(country_id, id, caption, incomeTaxRate));
            }
        } catch (final Exception e) {
            logger.error(url + "&format=debug");
            throw e;
        }
        return list;
    }

    public static List<Country> getCountries(final String host, final String realm) throws IOException {
        final String lang = (Wizard.host.equals(host) ? "ru" : "en");
        final String url = host + "api/" + realm + "/main/geo/country/browse?lang=" + lang;

        final List<Country> list = new ArrayList<>();
        try {
            final String json = Downloader.getJson(url);
            final Gson gson = new Gson();
            final Type mapType = new TypeToken<Map<String, Map<String, Object>>>() {
            }.getType();
            final Map<String, Map<String, Object>> mapOfCountry = gson.fromJson(json, mapType);


            for (final Map.Entry<String, Map<String, Object>> entry : mapOfCountry.entrySet()) {
                final Map<String, Object> country = entry.getValue();

                final String id = country.get("id").toString();
                final String caption = country.get("name").toString();

                list.add(new Country(id, caption));
            }
        } catch (final Exception e) {
            logger.error(url + "&format=debug");
            throw e;
        }
        return list;
    }
}