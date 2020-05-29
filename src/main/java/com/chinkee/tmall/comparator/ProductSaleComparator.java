package com.chinkee.tmall.comparator;

import com.chinkee.tmall.pojo.Product;

import java.util.Comparator;

public class ProductSaleComparator implements Comparator<Product> {
    @Override
    public int compare(Product p1, Product p2) {
        return p2.getSaleCount() - p1.getSaleCount();
    }
}
