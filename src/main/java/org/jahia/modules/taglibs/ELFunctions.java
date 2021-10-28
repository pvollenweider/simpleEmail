package org.jahia.modules.taglibs;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;

public class ELFunctions {

    /**
     * Logger for ELFunctions
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ELFunctions.class);

    public static String removeSessionAttribute(HttpServletRequest request, String attribute) {
        if (StringUtils.isNotBlank(attribute)) {
            request.getSession().removeAttribute(attribute);
        }
        return StringUtils.EMPTY;
    }

}
