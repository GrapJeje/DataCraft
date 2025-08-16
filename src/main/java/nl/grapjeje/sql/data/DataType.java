package nl.grapjeje.sql.data;

import lombok.Getter;

@Getter
public enum DataType {
    SMALL_TEXT(1),
    TEXT(5),
    BIG_TEXT(10),
    INTEGER(1),
    BOOLEAN(1),
    DOUBLE(2);

    private final int height;

    DataType(int height) {
        this.height = height;
    }
}
