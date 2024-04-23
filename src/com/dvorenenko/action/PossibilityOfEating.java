package com.dvorenenko.action;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class PossibilityOfEating {
    @JsonProperty("from")
    private String from;
    @JsonProperty("to")
    private String to;
    @JsonProperty("percent")
    private long percent;


    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public long getPercent() {
        return percent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PossibilityOfEating that = (PossibilityOfEating) o;
        return percent == that.percent && Objects.equals(from, that.from) && Objects.equals(to, that.to);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(from);
        result = 31 * result + Objects.hashCode(to);
        result = 31 * result + Long.hashCode(percent);
        return result;
    }
}
