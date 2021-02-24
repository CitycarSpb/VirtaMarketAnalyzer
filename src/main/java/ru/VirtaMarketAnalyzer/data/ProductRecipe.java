package ru.VirtaMarketAnalyzer.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by cobr123 on 18.05.2015.
 */
final public class ProductRecipe {
    @SerializedName("i")
    final private String manufactureID;
    @SerializedName("s")
    final private String specialization;
    @SerializedName("e")
    final private Product equipment;
    @SerializedName("epw")
    final private double equipmentPerWorker;
    @SerializedName("ec")
    final private double energyConsumption;
    @SerializedName("ip")
    final private List<ManufactureIngredient> inputProducts;
    @SerializedName("rp")
    final private List<ManufactureResult> resultProducts;

    public ProductRecipe(
            final String manufactureID,
            final String specialization,
            final Product equipment,
            final double equipmentPerWorker,
            final double energyConsumption,
            final List<ManufactureIngredient> inputProducts,
            final List<ManufactureResult> resultProducts
    ) {
        this.manufactureID = manufactureID;
        this.specialization = specialization;
        this.equipment = equipment;
        this.equipmentPerWorker = equipmentPerWorker;
        this.energyConsumption = energyConsumption;
        this.inputProducts = inputProducts;
        this.resultProducts = resultProducts;
    }

    public String getManufactureID() {
        return manufactureID;
    }

    public List<ManufactureResult> getResultProducts() {
        return resultProducts;
    }

    public List<ManufactureIngredient> getInputProducts() {
        return inputProducts;
    }

    public String getSpecialization() {
        return specialization;
    }

    public Product getEquipment() {
        return equipment;
    }

    public double getEquipmentPerWorker() {
        return equipmentPerWorker;
    }

    public double getEnergyConsumption() {
        return energyConsumption;
    }
}
