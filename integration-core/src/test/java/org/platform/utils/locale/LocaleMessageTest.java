package org.platform.utils.locale;

import java.util.Locale;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@TransactionConfiguration(transactionManager="transactionManager",defaultRollback=false)
@Transactional
public class LocaleMessageTest {

	@Resource
	private MessageSource messageSourceReloadable = null;

	@Test
	public void testMessageReloadable() {
		System.out.println("locale: " + Locale.UK);
		String message = messageSourceReloadable.getMessage(
				"username", null, "", Locale.US);
		System.out.println("message: " + message);
		Assert.assertEquals("username", message);
	}


}
