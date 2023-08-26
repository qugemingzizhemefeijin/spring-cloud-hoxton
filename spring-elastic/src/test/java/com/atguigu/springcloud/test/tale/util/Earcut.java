package com.atguigu.springcloud.test.tale.util;

import java.util.List;

public final class Earcut {

    private Earcut() {
        throw new AssertionError("No Instances.");
    }

    private static class Node {
        // vertex index in coordinates array
        int i;
        // vertex coordinates
        double x;
        double y;

        // previous and next vertex nodes in a polygon ring
        Node prev;
        Node next;

        // z-order curve value
        int z = 0;

        // previous and next nodes in z-order
        Node prevZ;
        Node nextZ;

        // indicates whether this is a steiner point
        boolean steiner = false;

        Node(int i, double x, double y) {
            this.i = i;
            this.x = x;
            this.y = y;
        }
    }

    public static List<Integer> earcut(List<Double> vertices, List<Integer> holeIndices, int dim) {
        double[] data = toDoubleArray(vertices);

        boolean hasHoles = holeIndices != null && holeIndices.size() > 0;
        int outerLen = hasHoles ? holeIndices.get(0) * dim : data.length;
        Node outerNode = linkedList(data, 0, outerLen, dim, true);
    }

    // create a circular doubly linked list from polygon points in the specified winding order
    private static Node linkedList(double[] data, int start, int end, int dim, boolean clockwise) {
        Node last = null;

        if (clockwise == (signedArea(data, start, end, dim) > 0)) {
            for (int i = start; i < end; i += dim) {
                last = insertNode(i, data[i], data[i + 1], last);
            }
        } else {
            for (int i = end - dim; i >= start; i -= dim) {
                last = insertNode(i, data[i], data[i + 1], last);
            }
        }

        if (last && equals(last, last.next)) {
            removeNode(last);
            last = last.next;
        }

        return last;
    }

    private static double signedArea(double[] data, int start, int end, int dim) {
        double sum = 0;
        for (int i = start, j = end - dim; i < end; i += dim) {
            sum += (data[j] - data[i]) * (data[i + 1] + data[j + 1]);
            j = i;
        }
        return sum;
    }

    private static double[] toDoubleArray(List<Double> vertices) {
        double[] data = new double[vertices.size()];
        for (int i = 0, size = vertices.size(); i < size; i++) {
            data[i] = vertices.get(i);
        }
        return data;
    }

}
