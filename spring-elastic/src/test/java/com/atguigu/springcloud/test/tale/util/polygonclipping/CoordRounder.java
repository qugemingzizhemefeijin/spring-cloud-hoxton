package com.atguigu.springcloud.test.tale.util.polygonclipping;

import com.atguigu.springcloud.test.tale.util.splaytree.Node;
import com.atguigu.springcloud.test.tale.util.splaytree.SplayTree;

public class CoordRounder {

    private static final double EPSILON = Math.pow(2, -52);
    private static final double EPSILON_SQ = EPSILON * EPSILON;

    private final SplayTree<Double, Object> tree = new SplayTree<>();

    public CoordRounder() {
        // preseed with 0 so we don't end up with values < Number.EPSILON
        this.round(0);
    }

    // Note: this can rounds input values backwards or forwards.
    //       You might ask, why not restrict this to just rounding
    //       forwards? Wouldn't that allow left endpoints to always
    //       remain left endpoints during splitting (never change to
    //       right). No - it wouldn't, because we snap intersections
    //       to endpoints (to establish independence from the segment
    //       angle for t-intersections).
    public double round(double coord) {
        Node<Double, Object> node = this.tree.add(coord);

        Node<Double, Object> prevNode = this.tree.prev(node);
        if (prevNode != null && cmp(node.getKey(), prevNode.getKey()) == 0) {
            this.tree.remove(coord);
            return prevNode.getKey();
        }

        Node<Double, Object> nextNode = this.tree.next(node);
        if (nextNode != null && cmp(node.getKey(), nextNode.getKey()) == 0) {
            this.tree.remove(coord);
            return nextNode.getKey();
        }

        return coord;
    }

    // FLP comparator
    private int cmp(double a, double b) {
        // check if they're both 0
        if (-EPSILON < a && a < EPSILON) {
            if (-EPSILON < b && b < EPSILON) {
                return 0;
            }
        }

        // check if they're flp equal
        double ab = a - b;
        if (ab * ab < EPSILON_SQ * a * b) {
            return 0;
        }

        // normal comparison
        return a < b ? -1 : 1;
    }

}
