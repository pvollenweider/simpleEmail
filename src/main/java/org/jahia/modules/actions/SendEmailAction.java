package org.jahia.modules.actions;

import org.jahia.bin.Action;
import org.jahia.bin.ActionResult;
import org.jahia.registries.ServicesRegistry;
import org.jahia.services.content.JCRContentUtils;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.URLResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SendEmailAction extends Action {
    private static final Logger LOGGER = LoggerFactory.getLogger(SendEmailAction.class);
    private String templatePath;

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public ActionResult
    doExecute(HttpServletRequest req, RenderContext renderContext, Resource resource, JCRSessionWrapper session,
              Map<String, List<String>> parameters, URLResolver urlResolver) throws Exception {
        LOGGER.debug("Executing SendEmailAction");

        JCRNodeWrapper currentPage = (JCRNodeWrapper) JCRContentUtils.getParentOfType(resource.getNode(), "jnt:page");
        String emailAddress = getParameter(parameters, "emailAddress");
        LOGGER.debug("emailAddress is " + emailAddress);
        if (emailAddress == null || !isValidEmailAddress(emailAddress)) {
            return sendError(currentPage, "email", req);
        }

        String message = getParameter(parameters, "message");
        LOGGER.debug("message is " + message);
        if (message == null) {
            return sendError(currentPage, "message", req);
        }

        String from = resource.getNode().getPropertyAsString("from");
        String to = resource.getNode().getPropertyAsString("to");

        Map<String, Object> bindings = new HashMap<String, Object>();
        bindings.put("subject", "Send email example");
        bindings.put("emailAddress", emailAddress);
        bindings.put("message", message);
        try {
            LOGGER.debug("Use templatePath " + templatePath);
            ServicesRegistry.getInstance().getMailService().sendMessageWithTemplate(templatePath, bindings, to, from, null, null, Locale.ENGLISH, "simpleEmail");

        } catch (ScriptException | RepositoryException e) {
            LOGGER.error("Error will sending mail to administrator about failing remote publication", e);
            return sendError(currentPage, "sendMessage", req);
        }

        return sendError(currentPage, "mailSent", req);
    }

    public ActionResult sendError(JCRNodeWrapper errorPage, String errorType, HttpServletRequest req, Exception e) {
        if (errorPage == null) {
            LOGGER.debug("Error: Could not found a valid errorPage");
            return ActionResult.BAD_REQUEST;
        } else {
            LOGGER.debug(e.getMessage());
            LOGGER.debug("Redirect to " + errorPage.getPath() + " (" + errorType + ")");
            req.getSession().setAttribute("j_error", errorType);
            return new ActionResult(HttpServletResponse.SC_OK, errorPage.getPath());
        }
    }

    public ActionResult sendError(JCRNodeWrapper errorPage, String errorType, HttpServletRequest req) {
        return sendError(errorPage, errorType, req, new Exception());
    }

    public static boolean isValidEmailAddress(String email) {
        if (email == null) return false;
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }
}
