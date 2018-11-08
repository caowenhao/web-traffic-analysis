package com.twq.parser.utils;

public class QueryAndFragment {
    private String query;
    private String frament;

    public QueryAndFragment(String query, String frament) {
        this.query = query;
        this.frament = frament;
    }

    public String getQuery() {
        return query;
    }

    public String getFrament() {
        return frament;
    }
}
