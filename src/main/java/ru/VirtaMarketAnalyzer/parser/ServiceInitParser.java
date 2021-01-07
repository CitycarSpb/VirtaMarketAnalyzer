package ru.VirtaMarketAnalyzer.parser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.VirtaMarketAnalyzer.data.Product;
import ru.VirtaMarketAnalyzer.data.RawMaterial;
import ru.VirtaMarketAnalyzer.data.UnitType;
import ru.VirtaMarketAnalyzer.data.UnitTypeSpec;
import ru.VirtaMarketAnalyzer.main.Wizard;
import ru.VirtaMarketAnalyzer.scrapper.Downloader;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by cobr123 on 25.02.16.
 */
public final class ServiceInitParser {
    private static final Logger logger = LoggerFactory.getLogger(ServiceInitParser.class);


    public static UnitType getServiceUnitType(final String host, final String realm, final String id) throws IOException {
        final Optional<UnitType> opt = getServiceUnitTypes(host, realm).stream()
                .filter(v -> v.getId().equals(id)).findFirst();
        if (!opt.isPresent()) {
            throw new IllegalArgumentException("Не найден сервис с id '" + id + "'");
        }
        return opt.get();
    }

    public static List<UnitType> getServiceUnitTypes(final String host, final String realm) throws IOException {
        final String lang = (Wizard.host.equals(host) ? "ru" : "en");
        final String url = host + "api/" + realm + "/main/unittype/browse?lang=" + lang;

        final List<UnitType> list = new ArrayList<>();
        try {
            final String json = Downloader.getJson(url);
            final Gson gson = new Gson();
            final Type mapType = new TypeToken<Map<String, Map<String, Object>>>() {
            }.getType();
            final Map<String, Map<String, Object>> mapOfUnitTypes = gson.fromJson(json, mapType);

            for (final Map.Entry<String, Map<String, Object>> entry : mapOfUnitTypes.entrySet()) {
                final Map<String, Object> unitType = entry.getValue();

                if ("service_light".equalsIgnoreCase(unitType.get("kind").toString())
                        || "educational".equalsIgnoreCase(unitType.get("kind").toString())
                        || "restaurant".equalsIgnoreCase(unitType.get("kind").toString())
                        || "repair".equalsIgnoreCase(unitType.get("kind").toString())
                        || "medicine".equalsIgnoreCase(unitType.get("kind").toString())
                        || "it".equalsIgnoreCase(unitType.get("kind").toString())
                ) {
                    final String id = unitType.get("id").toString();
                    final String caption = unitType.get("name").toString();
                    final String imgUrl = "/img/unit_types/" + unitType.get("symbol").toString() + ".gif";

                    list.add(new UnitType(id, caption, imgUrl, getServiceSpecs(host, realm, id)));
                }
            }
        } catch (final Exception e) {
            Downloader.invalidateCache(url);
            logger.error(url + "&format=debug");
            throw e;
        }
        return list;
    }

    public static List<UnitTypeSpec> getServiceSpecs(final String host, final String realm, final String unit_type_id) throws IOException {
        final String lang = (Wizard.host.equals(host) ? "ru" : "en");
        final String url = host + "api/" + realm + "/main/unittype/produce?id=" + unit_type_id + "&lang=" + lang;

        final List<UnitTypeSpec> list = new ArrayList<>();
        try {
            final String json = Downloader.getJson(url);
            final Gson gson = new Gson();
            final Type mapType = new TypeToken<Map<String, Map<String, Object>>>() {
            }.getType();
            final Map<String, Map<String, Object>> mapOfUnitTypes = gson.fromJson(json, mapType);

            for (final Map.Entry<String, Map<String, Object>> entry : mapOfUnitTypes.entrySet()) {
                final Map<String, Object> unitType = entry.getValue();

                final String id = unitType.get("id").toString();
                final String caption = unitType.get("name").toString();
                final String equipment_product_id = unitType.get("equipment_product_id").toString();

                list.add(new UnitTypeSpec(id, caption, getProduct(host, realm, equipment_product_id), getRawMaterials(host, realm, unitType)));
            }
        } catch (final Exception e) {
            Downloader.invalidateCache(url);
            logger.error(url + "&format=debug");
            throw e;
        }
        return list;
    }

    private static Product getProduct(final String host, final String realm, final String id) throws IOException {
        return ProductInitParser.getManufactureProduct(host, realm, id);
    }

    private static List<RawMaterial> getRawMaterials(final String host, final String realm, final Map<String, Object> unitType) throws IOException {
        final List<RawMaterial> rawMaterials = new ArrayList<>();
        final Object inputObj = unitType.get("input");
        if (inputObj != null && !(inputObj instanceof ArrayList)) {
            final Map<String, Object> inputList = (Map<String, Object>) inputObj;
            for (final Map.Entry<String, Object> entry : inputList.entrySet()) {
                final Map<String, Object> input = (Map<String, Object>) entry.getValue();
                final Product product = getProduct(host, realm, input.get("id").toString());
                final double quantity = Double.parseDouble(input.get("qty").toString());
                rawMaterials.add(new RawMaterial(product, quantity));
            }
        }
        return rawMaterials;
    }

    private static String removeCurlyBraces(final String line) {
        return line.substring(1, line.length() - 1);
    }
}
