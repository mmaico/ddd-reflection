package com.trex.clone.node;


public class NestedObjectCopied {

    private final Object origin;
    private final Object destination;

    public NestedObjectCopied(Object origin, Object destination) {
        this.origin = origin;
        this.destination = destination;
    }

    public static NestedObjectCopied createNestedCopied(Object origin, Object destination) {
        return new NestedObjectCopied(origin, destination);
    }

    public Object getOrigin() {
        return origin;
    }

    public Object getDestination() {
        return destination;
    }

    @Override
    public boolean equals(Object o) {
        if (this.origin == o) return true;
        if (o == null) return false;

        NestedObjectCopied that = (NestedObjectCopied) o;

        return origin != null ? origin.equals(that.origin) : Boolean.FALSE;
    }

    @Override
    public int hashCode() {
        int result = origin != null ? origin.hashCode() : 0;
        return result;
    }
}
