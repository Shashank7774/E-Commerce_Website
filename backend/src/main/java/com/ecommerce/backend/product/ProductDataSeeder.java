package com.ecommerce.backend.product;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class ProductDataSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;

    public ProductDataSeeder(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {

        List<Product> products = List.of(

                // =========================
                // FASHION
                // =========================

                new Product(
                        "relaxed-cotton-shirt",
                        "Relaxed Cotton Shirt",
                        "YourStore",
                        "Fashion",
                        new BigDecimal("1299.00"),
                        new BigDecimal("1999.00"),
                        new BigDecimal("4.50"),
                        124,
                        "https://images.unsplash.com/photo-1602810318383-cc40c33a3b3f?auto=format&fit=crop&w=800&q=80",
                        "Bestseller",
                        50
                ),

                new Product(
                        "minimal-linen-overshirt",
                        "Minimal Linen Overshirt",
                        "YourStore",
                        "Fashion",
                        new BigDecimal("1899.00"),
                        new BigDecimal("2799.00"),
                        new BigDecimal("4.50"),
                        65,
                        "https://images.unsplash.com/photo-1596755094514-f87e34085b2c?auto=format&fit=crop&w=800&q=80",
                        "Trending",
                        45
                ),

                new Product(
                        "classic-denim-jacket",
                        "Classic Denim Jacket",
                        "YourStore",
                        "Fashion",
                        new BigDecimal("2199.00"),
                        new BigDecimal("3299.00"),
                        new BigDecimal("4.70"),
                        87,
                        "https://images.unsplash.com/photo-1543076447-215ad9ba6923?auto=format&fit=crop&w=800&q=80",
                        "Popular",
                        32
                ),

                new Product(
                        "premium-basic-tshirt",
                        "Premium Basic T-Shirt",
                        "YourStore",
                        "Fashion",
                        new BigDecimal("799.00"),
                        new BigDecimal("1199.00"),
                        new BigDecimal("4.40"),
                        156,
                        "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?auto=format&fit=crop&w=800&q=80",
                        "Bestseller",
                        80
                ),

                new Product(
                        "oversized-hoodie",
                        "Oversized Everyday Hoodie",
                        "YourStore",
                        "Fashion",
                        new BigDecimal("1599.00"),
                        new BigDecimal("2499.00"),
                        new BigDecimal("4.60"),
                        93,
                        "https://images.unsplash.com/photo-1556821840-3a63f95609a7?auto=format&fit=crop&w=800&q=80",
                        "New",
                        40
                ),

                new Product(
                        "straight-fit-chinos",
                        "Straight Fit Chinos",
                        "YourStore",
                        "Fashion",
                        new BigDecimal("1399.00"),
                        new BigDecimal("1999.00"),
                        new BigDecimal("4.30"),
                        72,
                        "https://images.unsplash.com/photo-1624378439575-d8705ad7ae80?auto=format&fit=crop&w=800&q=80",
                        "New",
                        55
                ),

                new Product(
                        "cotton-polo-shirt",
                        "Classic Cotton Polo",
                        "YourStore",
                        "Fashion",
                        new BigDecimal("999.00"),
                        new BigDecimal("1599.00"),
                        new BigDecimal("4.50"),
                        118,
                        "https://images.unsplash.com/photo-1625910513413-5fc45d5c4c6f?auto=format&fit=crop&w=800&q=80",
                        "Popular",
                        65
                ),

                new Product(
                        "everyday-casual-shirt",
                        "Everyday Casual Shirt",
                        "YourStore",
                        "Fashion",
                        new BigDecimal("1099.00"),
                        new BigDecimal("1699.00"),
                        new BigDecimal("4.20"),
                        64,
                        "https://images.unsplash.com/photo-1603252109303-2751441dd157?auto=format&fit=crop&w=800&q=80",
                        "Deal",
                        38
                ),

                new Product(
                        "classic-leather-sneakers",
                        "Classic Leather Sneakers",
                        "YourStore",
                        "Fashion",
                        new BigDecimal("2999.00"),
                        new BigDecimal("3999.00"),
                        new BigDecimal("4.60"),
                        112,
                        "https://images.unsplash.com/photo-1495555961986-6d4c1ecb7be3?auto=format&fit=crop&w=800&q=80",
                        "Premium",
                        30
                ),

                new Product(
                        "premium-everyday-sneakers",
                        "Premium Everyday Sneakers",
                        "YourStore",
                        "Fashion",
                        new BigDecimal("2499.00"),
                        new BigDecimal("3499.00"),
                        new BigDecimal("4.60"),
                        87,
                        "https://images.unsplash.com/photo-1542291026-7eec264c27ff?auto=format&fit=crop&w=800&q=80",
                        "New",
                        35
                ),

                // =========================
                // BEAUTY
                // =========================

                new Product(
                        "daily-glow-skincare-set",
                        "Daily Glow Skincare Set",
                        "YourStore",
                        "Beauty",
                        new BigDecimal("1499.00"),
                        new BigDecimal("2199.00"),
                        new BigDecimal("4.70"),
                        98,
                        "https://images.unsplash.com/photo-1556229010-6c3f2c9ca5f8?auto=format&fit=crop&w=800&q=80",
                        "Popular",
                        40
                ),

                new Product(
                        "hydrating-face-serum",
                        "Hydrating Face Serum",
                        "YourStore",
                        "Beauty",
                        new BigDecimal("999.00"),
                        new BigDecimal("1499.00"),
                        new BigDecimal("4.80"),
                        143,
                        "https://images.unsplash.com/photo-1620916566398-39f1143ab7be?auto=format&fit=crop&w=800&q=80",
                        "Best Seller",
                        60
                ),

                new Product(
                        "vitamin-c-face-serum",
                        "Vitamin C Brightening Serum",
                        "YourStore",
                        "Beauty",
                        new BigDecimal("899.00"),
                        new BigDecimal("1399.00"),
                        new BigDecimal("4.60"),
                        91,
                        "https://images.unsplash.com/photo-1612817288484-6f916006741a?auto=format&fit=crop&w=800&q=80",
                        "Trending",
                        45
                ),

                new Product(
                        "gentle-face-cleanser",
                        "Gentle Daily Face Cleanser",
                        "YourStore",
                        "Beauty",
                        new BigDecimal("549.00"),
                        new BigDecimal("799.00"),
                        new BigDecimal("4.40"),
                        76,
                        "https://images.unsplash.com/photo-1556228720-195a672e8a03?auto=format&fit=crop&w=800&q=80",
                        "Everyday",
                        70
                ),

                new Product(
                        "hydrating-body-lotion",
                        "Hydrating Body Lotion",
                        "YourStore",
                        "Beauty",
                        new BigDecimal("699.00"),
                        new BigDecimal("999.00"),
                        new BigDecimal("4.30"),
                        59,
                        "https://images.unsplash.com/photo-1608248543803-ba4f8c70ae0b?auto=format&fit=crop&w=800&q=80",
                        "Deal",
                        55
                ),

                new Product(
                        "daily-sunscreen-spf50",
                        "Daily Sunscreen SPF 50",
                        "YourStore",
                        "Beauty",
                        new BigDecimal("749.00"),
                        new BigDecimal("1099.00"),
                        new BigDecimal("4.70"),
                        134,
                        "https://images.unsplash.com/photo-1556228578-8c89e6adf883?auto=format&fit=crop&w=800&q=80",
                        "Bestseller",
                        85
                ),

                new Product(
                        "overnight-repair-cream",
                        "Overnight Repair Cream",
                        "YourStore",
                        "Beauty",
                        new BigDecimal("1199.00"),
                        new BigDecimal("1799.00"),
                        new BigDecimal("4.50"),
                        67,
                        "https://images.unsplash.com/photo-1611930022073-b7a4ba5fcccd?auto=format&fit=crop&w=800&q=80",
                        "New",
                        35
                ),

                new Product(
                        "lip-care-balm-set",
                        "Lip Care Balm Set",
                        "YourStore",
                        "Beauty",
                        new BigDecimal("399.00"),
                        new BigDecimal("599.00"),
                        new BigDecimal("4.20"),
                        84,
                        "https://images.unsplash.com/photo-1586495777744-4413f21062fa?auto=format&fit=crop&w=800&q=80",
                        "Deal",
                        100
                ),

                new Product(
                        "clay-face-mask",
                        "Purifying Clay Face Mask",
                        "YourStore",
                        "Beauty",
                        new BigDecimal("599.00"),
                        new BigDecimal("899.00"),
                        new BigDecimal("4.40"),
                        63,
                        "https://images.unsplash.com/photo-1570172619644-dfd03ed5d881?auto=format&fit=crop&w=800&q=80",
                        "Popular",
                        48
                ),

                new Product(
                        "hair-care-essentials",
                        "Hair Care Essentials Kit",
                        "YourStore",
                        "Beauty",
                        new BigDecimal("1299.00"),
                        new BigDecimal("1999.00"),
                        new BigDecimal("4.60"),
                        52,
                        "https://images.unsplash.com/photo-1522338242992-e1a54906a8da?auto=format&fit=crop&w=800&q=80",
                        "New",
                        30
                ),

                // =========================
                // GROCERY
                // =========================

                new Product(
                        "organic-pantry-essentials",
                        "Organic Pantry Essentials",
                        "YourStore",
                        "Grocery",
                        new BigDecimal("899.00"),
                        new BigDecimal("1199.00"),
                        new BigDecimal("4.40"),
                        76,
                        "https://images.unsplash.com/photo-1542838132-92c53300491e?auto=format&fit=crop&w=800&q=80",
                        "Organic",
                        100
                ),

                new Product(
                        "organic-breakfast-pack",
                        "Organic Breakfast Pack",
                        "YourStore",
                        "Grocery",
                        new BigDecimal("699.00"),
                        new BigDecimal("899.00"),
                        new BigDecimal("4.30"),
                        54,
                        "https://images.unsplash.com/photo-1517686469429-8bdb88b9f907?auto=format&fit=crop&w=800&q=80",
                        "Bestseller",
                        80
                ),

                new Product(
                        "premium-basmati-rice",
                        "Premium Basmati Rice",
                        "YourStore",
                        "Grocery",
                        new BigDecimal("649.00"),
                        new BigDecimal("799.00"),
                        new BigDecimal("4.50"),
                        86,
                        "https://images.unsplash.com/photo-1586201375761-83865001e31c?auto=format&fit=crop&w=800&q=80",
                        "Popular",
                        120
                ),

                new Product(
                        "natural-honey",
                        "Pure Natural Honey",
                        "YourStore",
                        "Grocery",
                        new BigDecimal("499.00"),
                        new BigDecimal("699.00"),
                        new BigDecimal("4.60"),
                        109,
                        "https://images.unsplash.com/photo-1587049352846-4a222e784d38?auto=format&fit=crop&w=800&q=80",
                        "Natural",
                        90
                ),

                new Product(
                        "cold-pressed-olive-oil",
                        "Cold Pressed Olive Oil",
                        "YourStore",
                        "Grocery",
                        new BigDecimal("899.00"),
                        new BigDecimal("1199.00"),
                        new BigDecimal("4.50"),
                        71,
                        "https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?auto=format&fit=crop&w=800&q=80",
                        "Premium",
                        65
                ),

                new Product(
                        "mixed-nuts-premium",
                        "Premium Mixed Nuts",
                        "YourStore",
                        "Grocery",
                        new BigDecimal("799.00"),
                        new BigDecimal("1099.00"),
                        new BigDecimal("4.70"),
                        95,
                        "https://images.unsplash.com/photo-1599599810694-57a5d9b7f8a3?auto=format&fit=crop&w=800&q=80",
                        "Bestseller",
                        75
                ),

                new Product(
                        "whole-grain-oats",
                        "Whole Grain Rolled Oats",
                        "YourStore",
                        "Grocery",
                        new BigDecimal("349.00"),
                        new BigDecimal("499.00"),
                        new BigDecimal("4.40"),
                        121,
                        "https://images.unsplash.com/photo-1517093728432-a0440f8d45af?auto=format&fit=crop&w=800&q=80",
                        "Healthy",
                        140
                ),

                new Product(
                        "green-tea-collection",
                        "Premium Green Tea Collection",
                        "YourStore",
                        "Grocery",
                        new BigDecimal("449.00"),
                        new BigDecimal("699.00"),
                        new BigDecimal("4.30"),
                        62,
                        "https://images.unsplash.com/photo-1556679343-c7306c1976bc?auto=format&fit=crop&w=800&q=80",
                        "Popular",
                        85
                ),

                new Product(
                        "organic-peanut-butter",
                        "Organic Peanut Butter",
                        "YourStore",
                        "Grocery",
                        new BigDecimal("379.00"),
                        new BigDecimal("549.00"),
                        new BigDecimal("4.50"),
                        88,
                        "https://images.unsplash.com/photo-1589367920969-ab8e050bbb04?auto=format&fit=crop&w=800&q=80",
                        "Organic",
                        95
                ),

                new Product(
                        "premium-coffee-beans",
                        "Premium Arabica Coffee Beans",
                        "YourStore",
                        "Grocery",
                        new BigDecimal("749.00"),
                        new BigDecimal("999.00"),
                        new BigDecimal("4.80"),
                        137,
                        "https://images.unsplash.com/photo-1447933601403-0c6688de566e?auto=format&fit=crop&w=800&q=80",
                        "Bestseller",
                        70
                )
        );

        int inserted = 0;

        for (Product product : products) {

            if (!productRepository
                    .findBySlug(product.getSlug())
                    .isPresent()) {

                productRepository.save(product);
                inserted++;
            }
        }

        System.out.println(
                "Product seeding completed. New products inserted: "
                        + inserted
        );
    }
}