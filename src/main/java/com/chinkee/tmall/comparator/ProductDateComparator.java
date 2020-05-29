package com.chinkee.tmall.comparator;

import com.chinkee.tmall.pojo.Product;

import java.util.Comparator;

public class ProductDateComparator implements Comparator<Product> {
    @Override
    public int compare(Product p1, Product p2) {
        int result = p2.getCreateDate().compareTo(p1.getCreateDate());
        return result;
    }
}
