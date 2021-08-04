package pl.projectcode.pocketsectors.common.util;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class StringUtil {

    public static String join(List<String> input, String separator) {
        if(input == null || input.size() <= 0)
            return "";

        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < input.size(); i++) {
            sb.append(input.get(i));

            if(i != input.size() - 1)
                sb.append(separator);
        }

        return sb.toString();
    }
}
