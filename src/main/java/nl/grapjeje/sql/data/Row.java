package nl.grapjeje.sql.data;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Row {
    private final Map<String, String> values = new HashMap<>();

    public void set(String column, String value) {
        values.put(column, value);
    }

    public String getValue(String column) {
        return values.getOrDefault(column, "");
    }

    public String serialize(List<String> columns) {
        StringBuilder sb = new StringBuilder();
        for (String col : columns) {
            sb.append(getValue(col)).append(";");
        }
        return sb.toString();
    }
}
