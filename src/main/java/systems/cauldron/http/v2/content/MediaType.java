/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.cauldron.http.v2.content;

import java.util.HashMap;
import java.util.Map;

/**
 * @author admin
 */
public class MediaType {

    private MediaType(Category cat, String subCat, Map<String, String> pMap) {
        this.contentType = cat;
        this.subContentType = subCat;
        this.parameterMap = pMap;
    }

    public enum Category {

        application,
        audio,
        example,
        image,
        message,
        model,
        multipart,
        text,
        video,
        any;
    }

    private static Category getCategory(String value) {
        switch (value) {
            case "text":
                return Category.text;
            case "image":
                return Category.image;
            case "application":
                return Category.application;
            case "audio":
                return Category.audio;
            case "video":
                return Category.video;
            case "multipart":
                return Category.multipart;
            case "message":
                return Category.message;
            case "model":
                return Category.model;
            default:
                return null;
        }
    }

    public static MediaType parse(String input) {
        String[] results = input.split("/");
        if (results.length == 2) {
            Category cat = getCategory(results[0]);
            String[] asdf = results[1].split(";");
            String subCat = asdf[0];
            Map<String, String> pMap = new HashMap<>();
            for (int i = 1; i < asdf.length; i++) {
                String[] asdf2 = asdf[i].split("=", 2);
                if (asdf2.length == 2) {
                    pMap.put(asdf2[0], asdf2[1]);
                } else {
                    pMap.put(asdf2[0], null);
                }
            }
            return new MediaType(cat, subCat, pMap);
        }
        return null;
    }

    private final Category contentType;
    private final String subContentType;
    private final Map<String, String> parameterMap;
}
