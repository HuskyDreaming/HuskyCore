package com.huskydreaming.huskycore.interfaces;

import com.huskydreaming.huskycore.utilities.Util;

import java.util.List;
import java.util.ArrayList;

public interface Parseable {

    String parse();

    List<String> parseList();

    default String parameterize(Object... objects) {
        String string = parse();
        for (int i = 0; i < objects.length; i++) {
            String parameter = (objects[i] instanceof String stringObject) ? stringObject : String.valueOf(objects[i]);
            string = string.replace("{" + i + "}", Util.capitalize(parameter.replace("_", " ")));
        }

        return string;
    }

    default List<String> parameterizeList(Object... objects) {
        List<String> parameterList = new ArrayList<>();
        for (String string : parseList()) {
            for (int i = 0; i < objects.length; i++) {
                string = string.replace("{" + i + "}", String.valueOf(objects[i]));
            }
            parameterList.add(string);
        }
        return parameterList;
    }
}