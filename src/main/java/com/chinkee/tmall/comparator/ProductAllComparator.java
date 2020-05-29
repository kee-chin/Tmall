package com.chinkee.tmall.comparator;

import com.chinkee.tmall.pojo.Product;

import java.util.Comparator;

public class ProductAllComparator implements Comparator<Product> {

    @Override
    public int compare(Product p1, Product p2) {
        int result = p2.getReviewCount()*p2.getSaleCount()
                - p1.getReviewCount()*p1.getSaleCount();
        return result;
    }
}
