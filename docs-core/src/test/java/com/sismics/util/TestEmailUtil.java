package com.sismics.util;

import javax.mail.internet.MimeBodyPart;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * Test of the email utilities.
 */
public class TestEmailUtil {

    @Test
    public void testParsePlainTextMailContent() throws Exception {
        MimeBodyPart part = new MimeBodyPart();
        String plainText = "This is a plain text email.";
        part.setText(plainText, StandardCharsets.UTF_8.name());

        EmailUtil.MailContent mailContent = new EmailUtil.MailContent();

        EmailUtil.parseMailContent(part, mailContent);

        Assert.assertEquals(plainText, mailContent.getMessage());
    }
}
