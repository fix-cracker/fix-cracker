package org.fixt.fixcracker.core.api;

public interface MessageTemplateStorage {
    String getMessageTemplate(String protocolName, String templateName);

    void putMessageTemplate(String protocolName, String templateName, String messageString) throws Exception;

    void removeMessageTemplate(String protocolName, String templateName);

    String[] getMessageTemplateArray(String protocolName) throws Exception;
}
