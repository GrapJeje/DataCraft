package nl.grapjeje.sql.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Column {
    private final String name;
    private final DataType type;
}
