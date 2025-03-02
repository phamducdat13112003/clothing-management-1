package org.example.clothingmanagement.entity;

import java.util.List;

public class PurchaseOrderDetailDTO {
    private List<String> images;
    private List<String> productNames;
    private List<Double> weights;
    private List<String> colors;
    private List<String> sizes;
    private List<Integer> quantities;
    private List<Double> prices;
    private List<Double> totalPrices;

    public PurchaseOrderDetailDTO(List<String> images, List<String> productNames, List<Double> weights, List<String> colors,
                                  List<String> sizes, List<Integer> quantities, List<Double> prices, List<Double> totalPrices) {
        this.images = images;
        this.productNames = productNames;
        this.weights = weights;
        this.colors = colors;
        this.sizes = sizes;
        this.quantities = quantities;
        this.prices = prices;
        this.totalPrices = totalPrices;
    }

    public List<String> getImages() {
        return images;
    }

    public List<String> getProductNames() {
        return productNames;
    }

    public List<Double> getWeights() {
        return weights;
    }

    public List<String> getColors() {
        return colors;
    }

    public List<String> getSizes() {
        return sizes;
    }

    public List<Integer> getQuantities() {
        return quantities;
    }

    public List<Double> getPrices() {
        return prices;
    }

    public List<Double> getTotalPrices() {
        return totalPrices;
    }


}
