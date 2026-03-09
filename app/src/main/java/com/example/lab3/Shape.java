package com.example.lab3;

public enum Shape {
    CIRCLE(R.id.circle, "Circle", "Radius") {
        @Override
        double area(double r) {
            return Math.PI * r * r;
        }

        @Override
        double perimeter(double r) {
            return 2 * Math.PI * r;
        }
    },

    SQUARE(R.id.square, "Square", "Side size") {
        @Override
        double area(double a) {
            return a * a;
        }

        @Override
        double perimeter(double a) {
            return 4 * a;
        }
    },

    TRIANGLE(R.id.triangle, "Triangle", "Side size") {
        @Override
        double area(double a) {
            return (a * a * Math.sqrt(3)) / 4.0;
        }

        @Override
        double perimeter(double a) {
            return 3 * a;
        }
    };

    final int radioId;
    final String title;
    final String valueLabel;

    Shape(int radioId, String title, String valueLabel) {
        this.radioId = radioId;
        this.title = title;
        this.valueLabel = valueLabel;
    }

    abstract double area(double value);

    abstract double perimeter(double value);

    static Shape fromRadioId(int id) {
        for (Shape s : values()) {
            if (s.radioId == id) return s;
        }
        return null;
    }
}