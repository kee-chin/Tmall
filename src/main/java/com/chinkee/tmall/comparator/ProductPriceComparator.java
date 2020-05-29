package com.chinkee.tmall.comparator;

import com.chinkee.tmall.pojo.Product;

import java.util.Comparator; // 比较器

public class ProductPriceComparator implements Comparator<Product> {
    @Override
    public int compare(Product p1, Product p2) {
        int result = (int) (p1.getPromotePrice() - p2.getPromotePrice());
        return result;
    }
}
