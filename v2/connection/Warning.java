/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class Warning {

    public Warning(final Integer code, final String agent, final String text, final Date date) {
        this.code = code;
        this.agent = agent;
        this.text = text;
        this.date = date;
    }

    public Warning(final Integer code, final String agent, final String text) {
        this.code = code;
        this.agent = agent;
        this.text = text;
        this.date = null;
    }

    public static Warning parse(String input) {
        String[] result = input.split(" ");
        switch (result.length) {
            case 3:
                return new Warning(Integer.parseInt(result[0]), result[1], result[2]);
            case 4:
                try {
                    return new Warning(Integer.parseInt(result[0]), result[1], result[2], DateFormat.getInstance().parse(input));
                } catch (ParseException ex) {
                    return null;
                }
        }
        return null;
    }
    private final Integer code;
    private final String agent;
    private final String text;
    private final Date date;
}
