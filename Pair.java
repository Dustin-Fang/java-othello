package org.cis120.othello;

/**
 * This class helps store the moves made as a tuple
 * These tuples are used to track valid moves
 * 
 * @author fdustin
 *
 */
public class Pair implements Comparable<Pair> {
    private final Integer first;
    private final Integer second;

    public Pair(int first, int second) {
        this.first = first;
        this.second = second;
    }

    public int getFirst() {
        return this.first;
    }

    public int getSecond() {
        return this.second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof Pair)) {
            return false;
        }
        Pair that = (Pair) o;

        return this.first.equals(that.first) && this.second.equals(that.second);
    }

    @Override
    public String toString() {
        return "[" + this.first + ", " + this.second + "]";
    }

    @Override
    public int compareTo(Pair other) {
        // compare the first numbers
        if (this.first > other.first) {
            return 1;
        } else if (this.first < other.first) {
            return -1;
        } else {
            if (this.second > other.second) {
                return 1;
            } else if (this.second < other.second) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
