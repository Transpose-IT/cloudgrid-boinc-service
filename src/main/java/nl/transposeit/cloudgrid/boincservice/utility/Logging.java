package nl.transposeit.cloudgrid.boincservice.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class Logging {

    private Logger logger;
    private static final String logformat = "[%s %s] - %s";

    public Logging(Class className) {
        this.logger = LoggerFactory.getLogger(className);
    }

    public void info(String message) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        logger.info(String.format(logformat, request.getMethod(), request.getRequestURI(), message));
    }

    public void debug(String message) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        logger.debug(String.format(logformat, request.getMethod(), request.getRequestURI(), message));
    }
}
