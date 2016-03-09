/**
 *
 */
package com.payu.sdk.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.w3c.dom.Element;

import com.payu.sdk.PayU;
import com.payu.sdk.PayUCreditCard;
import com.payu.sdk.PayUCustomers;
import com.payu.sdk.PayUPayments;
import com.payu.sdk.PayUPlans;
import com.payu.sdk.PayURecurringBillItem;
import com.payu.sdk.PayUReports;
import com.payu.sdk.PayUSubscription;
import com.payu.sdk.PayUTokens;
import com.payu.sdk.constants.Resources.RequestMethod;
import com.payu.sdk.exceptions.ConnectionException;
import com.payu.sdk.exceptions.InvalidParametersException;
import com.payu.sdk.exceptions.PayUException;
import com.payu.sdk.exceptions.SDKException;
import com.payu.sdk.helper.HttpClientHelper;
import com.payu.sdk.helper.SignatureHelper;
import com.payu.sdk.helper.WebClientDevWrapper;
import com.payu.sdk.model.AdditionalValue;
import com.payu.sdk.model.Address;
import com.payu.sdk.model.AddressV4;
import com.payu.sdk.model.Currency;
import com.payu.sdk.model.Order;
import com.payu.sdk.model.TransactionType;
import com.payu.sdk.paymentplan.model.SubscriptionPlan;
import com.payu.sdk.payments.model.PaymentRequest;
import com.payu.sdk.utils.CommonRequestUtil;
import com.payu.sdk.utils.JaxbUtil;
import com.payu.sdk.utils.LoggerUtil;
import com.payu.sdk.utils.PaymentPlanRequestUtil;
import com.payu.sdk.utils.RequestUtil;
import com.payu.sdk.utils.xml.AddressAdapter;
import com.payu.sdk.utils.xml.MapDetailsAdapter;
import com.payu.sdk.utils.xml.MapDetailsElement;
import com.payu.sdk.utils.xml.PayloadAdapter;
import com.payu.sdk.utils.xml.XmlFormatter;

/**
 * @author PayULatam
 *
 */
@Listeners(TestNGLoggingListener.class)
public class UtilTest {

	/* HELPERS TESTS */

	/**
	 * Test the signature helper utility
	 */
	@Test
	public void signatureHelperTest() {

		Order order = new Order();

		try {

			SignatureHelper.buildSignature(order, null, "1",
					SignatureHelper.DECIMAL_FORMAT_1,
					SignatureHelper.MD5_ALGORITHM);
			Assert.fail("No exception was thrown");

		} catch (IllegalArgumentException e) {
			LoggerUtil.error(e.getMessage(), e);
		}

		try {

			SignatureHelper.buildSignature(order, 1, "1",
					SignatureHelper.DECIMAL_FORMAT_1,
					SignatureHelper.MD5_ALGORITHM);
			Assert.fail("No exception was thrown");

		} catch (IllegalArgumentException e) {
			LoggerUtil.error(e.getMessage(), e);
		}

		try {
			order.setReferenceCode("ABC");
			SignatureHelper.buildSignature(order, 1, "1",
					SignatureHelper.DECIMAL_FORMAT_1,
					SignatureHelper.MD5_ALGORITHM);
			Assert.fail("No exception was thrown");

		} catch (IllegalArgumentException e) {
			LoggerUtil.error(e.getMessage(), e);
		}

		try {
			order.setAdditionalValues(new HashMap<String, AdditionalValue>());

			AdditionalValue additionalValue = new AdditionalValue();

			order.getAdditionalValues().put("TX_VALUE", additionalValue);
			SignatureHelper.buildSignature(order, 1, "1",
					SignatureHelper.DECIMAL_FORMAT_1,
					SignatureHelper.MD5_ALGORITHM);
			Assert.fail("No exception was thrown");

		} catch (IllegalArgumentException e) {
			LoggerUtil.error(e.getMessage(), e);
		}

		try {
			order.setAdditionalValues(new HashMap<String, AdditionalValue>());

			AdditionalValue additionalValue = new AdditionalValue();
			additionalValue.setCurrency(Currency.COP);

			order.getAdditionalValues().put("TX_VALUE", additionalValue);
			SignatureHelper.buildSignature(order, 1, "1",
					SignatureHelper.DECIMAL_FORMAT_1,
					SignatureHelper.MD5_ALGORITHM);
			Assert.fail("No exception was thrown");

		} catch (IllegalArgumentException e) {
			LoggerUtil.error(e.getMessage(), e);
		}

		try {
			order.setAdditionalValues(new HashMap<String, AdditionalValue>());

			AdditionalValue additionalValue = new AdditionalValue();
			additionalValue.setCurrency(Currency.COP);

			order.getAdditionalValues().put("TX_VALUE", additionalValue);
			SignatureHelper.buildSignature(order, 1, "1",
					SignatureHelper.DECIMAL_FORMAT_1, "OTRO");
			Assert.fail("No exception was thrown");

		} catch (IllegalArgumentException e) {
			LoggerUtil.error(e.getMessage(), e);
		}

		try {
			order.setAdditionalValues(new HashMap<String, AdditionalValue>());

			AdditionalValue additionalValue = new AdditionalValue();
			additionalValue.setCurrency(Currency.COP);
			additionalValue.setValue(BigDecimal.TEN);

			order.getAdditionalValues().put("TX_VALUE", additionalValue);
			SignatureHelper.buildSignature(order, 1, "1",
					SignatureHelper.DECIMAL_FORMAT_1, "OTRO");
			Assert.fail("No exception was thrown");

		} catch (IllegalArgumentException e) {
			LoggerUtil.error(e.getMessage(), e);
		}

		order.setAdditionalValues(new HashMap<String, AdditionalValue>());

		AdditionalValue additionalValue = new AdditionalValue();
		additionalValue.setCurrency(Currency.COP);
		additionalValue.setValue(BigDecimal.TEN);

		order.getAdditionalValues().put("TX_VALUE", additionalValue);
		SignatureHelper
				.buildSignature(order, 1, "1",
						SignatureHelper.DECIMAL_FORMAT_1,
						SignatureHelper.SHA_ALGORITHM);

	}

	/**
	 * The web client dev wrapper test
	 */
	@Test
	public void webClientDevWrapperTest() {

		try {
			HttpClient base = null;
			WebClientDevWrapper.wrapClient(base);
			Assert.fail("No exception was thrown");
		} catch (ConnectionException e) {
			LoggerUtil.error(e.getMessage(), e);
		}

	}

	/* UTILS TESTS */

	/**
	 * The common request util test
	 */
	@Test
	public void commonRequestUtilTest() {

		try {
			String[] test = new String[] {};
			CommonRequestUtil.validateNotAllowedParameters(null, test);
			Assert.fail("No exception was thrown");
		} catch (InvalidParametersException e) {
			LoggerUtil.error(e.getMessage(), e);
		}

	}

	/**
	 * Test jaxb util special cases
	 */
	@Test(expectedExceptions = { java.lang.IllegalArgumentException.class })
	public void jaxbUtilTest() {

		try {
			SubscriptionPlan plan;
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put(PayU.PARAMETERS.PLAN_DESCRIPTION, "Basic Plan");
			parameters.put(PayU.PARAMETERS.PLAN_CODE,
					"ASd456" + System.currentTimeMillis());
			parameters.put(PayU.PARAMETERS.PLAN_INTERVAL, "MONTH");
			parameters.put(PayU.PARAMETERS.PLAN_INTERVAL_COUNT, "12");
			parameters.put(PayU.PARAMETERS.PLAN_CURRENCY, "COP");
			parameters.put(PayU.PARAMETERS.PLAN_TAX, "10000");
			parameters.put(PayU.PARAMETERS.PLAN_TAX_RETURN_BASE, "40000");
			parameters.put(PayU.PARAMETERS.ACCOUNT_ID, "2");
			parameters.put(PayU.PARAMETERS.PLAN_ATTEMPTS_DELAY, "2");
			parameters.put(PayU.PARAMETERS.PLAN_MAX_PAYMENTS, "2");
			try {
				plan = PaymentPlanRequestUtil
						.buildSubscriptionPlanRequest(parameters);
				JaxbUtil.convertXmlToJava(String.class, plan.toXml());
				Assert.fail("No exception was throwed");
			} catch (InvalidParametersException ex) {
				LoggerUtil.error(ex.getMessage(), ex);
				Assert.fail(ex.getMessage());
			} catch (PayUException ex) {
				LoggerUtil.error(ex.getMessage(), ex);
			}
			String xml = null;
			plan = JaxbUtil.convertXmlToJava(SubscriptionPlan.class, xml);
			Assert.assertNull(plan, "The object is not null");

			xml = "";
			plan = JaxbUtil.convertXmlToJava(SubscriptionPlan.class, xml);
			Assert.assertNull(plan, "The object is not null");

			xml = JaxbUtil.convertJavaToXml(plan, false);
			Assert.assertNull(xml, "The xml is not null");

			xml = JaxbUtil.convertJavaToXml(new String(), false);
			Assert.fail("The xml coversión was successful");
		} catch (PayUException ex) {
			LoggerUtil.error(ex.getMessage(), ex);
			Assert.fail(ex.getMessage());
		}

	}

	/**
	 * The logger util test
	 */
	@Test
	public void loggerUtilTest() {

		Exception ex = new Exception("Exception");
		LoggerUtil.debug("debug", ex);
		LoggerUtil.error("error", ex);
		LoggerUtil.warning("warning", ex);
	}

	/**
	 * Payment plan request util test
	 */
	@Test
	public void paymentPlanRequestUtilTest() {

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(PayU.PARAMETERS.PLAN_VALUE, "ABC");

		try {
			PaymentPlanRequestUtil.buildSubscriptionPlanRequest(parameters);
			Assert.fail("No exception was thrown");
		} catch (InvalidParametersException e) {
			LoggerUtil.error(e.getMessage(), e);
		}

		parameters.remove(PayU.PARAMETERS.PLAN_VALUE);
		parameters.put(PayU.PARAMETERS.PLAN_CURRENCY, "ABC");

		try {
			PaymentPlanRequestUtil.buildSubscriptionPlanRequest(parameters);
			Assert.fail("No exception was thrown");
		} catch (InvalidParametersException e) {
			LoggerUtil.error(e.getMessage(), e);
		}

		parameters.remove(PayU.PARAMETERS.PLAN_CURRENCY);
		parameters.put(PayU.PARAMETERS.PLAN_ATTEMPTS_DELAY, "ABC");

		try {
			PaymentPlanRequestUtil.buildSubscriptionPlanRequest(parameters);
			Assert.fail("No exception was thrown");
		} catch (InvalidParametersException e) {
			LoggerUtil.error(e.getMessage(), e);
		}

		parameters.put(PayU.PARAMETERS.PLAN_ATTEMPTS_DELAY, "");

		try {
			SubscriptionPlan subscription = PaymentPlanRequestUtil
					.buildSubscriptionPlanRequest(parameters);

			Assert.assertNull(subscription.getPaymentAttemptsDelay());

		} catch (InvalidParametersException e) {
			LoggerUtil.error(e.getMessage(), e);
			Assert.fail(e.getMessage());
		}

	}

	/**
	 * Request util test
	 */
	@Test
	public void requestUtilTest() {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(PayU.PARAMETERS.START_DATE, "ABC");

		try {
			RequestUtil.buildGetCreditCardTokensRequest(parameters);
			Assert.fail("No exception was thrown");
		} catch (InvalidParametersException e) {
			LoggerUtil.error(e.getMessage(), e);
		}

		try {
			parameters.put(PayU.PARAMETERS.ORDER_ID, "1020");
			parameters.put(PayU.PARAMETERS.NOTIFY_URL, "https://www.test-confirmacion.com");
			parameters.put(PayU.PARAMETERS.RESPONSE_URL, "https://www.test-respuesta.com");
			
			PaymentRequest request = (PaymentRequest) RequestUtil
					.buildPaymentRequest(parameters,
							TransactionType.AUTHORIZATION);
			Assert.assertNotNull(request.getTransaction().getOrder(),
					"Null order");

		} catch (InvalidParametersException e) {
			LoggerUtil.error(e.getMessage(), e);
		}

	}

	/* UTILS XML TESTS */

	/**
	 * The address adapter test
	 */
	@Test
	public void addressAdapterTest() {

		AddressAdapter adapter = new AddressAdapter();

		Address add;
		AddressV4 addV4 = null;

		try {
			add = adapter.unmarshal(addV4);
			Assert.assertNull(add);
		} catch (ParseException ex) {
			LoggerUtil.error(ex.getMessage(), ex);
			Assert.fail(ex.getMessage());
		}

		addV4 = new AddressV4();

		try {
			add = adapter.unmarshal(addV4);
			Assert.assertNotNull(add);
		} catch (ParseException ex) {
			LoggerUtil.error(ex.getMessage(), ex);
			Assert.fail(ex.getMessage());
		}

	}

	/**
	 * The payload adapter test
	 */
	@Test
	public void payloadAdapterTest() {

		PayloadAdapter adapter = new PayloadAdapter();

		Element el = adapter.marshal(null);
		Assert.assertNull(el);
	}

	/**
	 * The xml formatter test
	 */
	@Test
	public void xmlFormatterTest() {

		try {
			String input = null;
			XmlFormatter.prettyFormat(input);
			Assert.fail("No exception was thrown");
		} catch (PayUException e) {
			LoggerUtil.error(e.getMessage(), e);
		}

	}

	/* OTHER TESTS */

	/**
	 * Test multiple error message
	 */
	@Test
	public void errorListTest() {

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(PayU.PARAMETERS.PLAN_DESCRIPTION, "Basic Plan");
		parameters.put(PayU.PARAMETERS.PLAN_CODE,
				"ASd456" + System.currentTimeMillis());
		parameters.put(PayU.PARAMETERS.PLAN_INTERVAL, "MONTH");

		try {
			HttpClientHelper.sendRequest(PaymentPlanRequestUtil
					.buildSubscriptionPlanRequest(parameters),
					RequestMethod.POST);
		} catch (ConnectionException ex) {
			LoggerUtil.error(ex.getMessage(), ex);
		} catch (SDKException ex) {
			LoggerUtil.error(ex.getMessage(), ex);
			Assert.fail(ex.getMessage());
		}

	}

	/**
	 * Create a simple error test
	 */
	@Test
	public void simpleErrorTest() {

		Thread.currentThread().setName("createPlan");

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(PayU.PARAMETERS.PLAN_DESCRIPTION, "Basic Plan");
		parameters.put(PayU.PARAMETERS.PLAN_CODE,
				"ASd456" + System.currentTimeMillis());
		parameters.put(PayU.PARAMETERS.PLAN_INTERVAL, "MONTH");
		parameters.put(PayU.PARAMETERS.PLAN_INTERVAL_COUNT, "12");
		parameters.put(PayU.PARAMETERS.PLAN_CURRENCY, "BRL");
		parameters.put(PayU.PARAMETERS.PLAN_VALUE, "500000000000000");
		parameters.put(PayU.PARAMETERS.PLAN_TAX, "0");
		parameters.put(PayU.PARAMETERS.PLAN_TAX_RETURN_BASE, "40000");
		parameters.put(PayU.PARAMETERS.ACCOUNT_ID, "1");
		parameters.put(PayU.PARAMETERS.PLAN_ATTEMPTS_DELAY, "2");
		parameters.put(PayU.PARAMETERS.PLAN_MAX_PAYMENTS, "2");

		try {
			HttpClientHelper.sendRequest(PaymentPlanRequestUtil
					.buildSubscriptionPlanRequest(parameters),
					RequestMethod.POST);
		} catch (ConnectionException ex) {
			LoggerUtil.error(ex.getMessage(), ex);
		} catch (SDKException ex) {
			LoggerUtil.error(ex.getMessage(), ex);
			Assert.fail(ex.getMessage());
		}

	}

	/**
	 * Test multiple error message
	 */
	@Test
	public void deleteTest() {

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(PayU.PARAMETERS.PLAN_ID, "123");

		try {
			HttpClientHelper.sendRequest(PaymentPlanRequestUtil
					.buildSubscriptionPlanRequest(parameters),
					RequestMethod.DELETE);
		} catch (ConnectionException ex) {
			LoggerUtil.error(ex.getMessage(), ex);
		} catch (SDKException ex) {
			LoggerUtil.error(ex.getMessage(), ex);
			Assert.fail(ex.getMessage());
		}

	}

	/**
	 * Test private constructors
	 *
	 * @throws IllegalArgumentException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public void testPrivateConstructor() throws IllegalArgumentException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {
		Class[] classes = { PayUCreditCard.class, PayUCustomers.class,
				PayUPayments.class, PayUPlans.class,
				PayURecurringBillItem.class, PayUReports.class,
				PayUSubscription.class, PayUTokens.class,
				HttpClientHelper.class, SignatureHelper.class,
				WebClientDevWrapper.class, CommonRequestUtil.class,
				JaxbUtil.class, LoggerUtil.class, PaymentPlanRequestUtil.class,
				RequestUtil.class, XmlFormatter.class };

		for (Class clasz : classes) {
			privateConstructorTest(clasz);
		}
	}

	/**
	 * Private constructor test
	 *
	 * @param clasz
	 * @throws IllegalArgumentException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("rawtypes")
	private void privateConstructorTest(Class clasz)
			throws IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		Constructor[] ctors = clasz.getDeclaredConstructors();
		Assert.assertEquals(1, ctors.length,
				"Utility class should only have one constructor");
		Constructor ctor = ctors[0];
		Assert.assertFalse(ctor.isAccessible(),
				"Utility class constructor should be inaccessible");
		ctor.setAccessible(true); // obviously we'd never do this in production
		Assert.assertEquals(clasz, ctor.newInstance().getClass(),
				"You'd expect the construct to return the expected type");
	}

	/**
	 * The map details adapter test
	 */
	@Test
	public void mapDetailsAddapterTest() {
		MapDetailsAdapter adapter = new MapDetailsAdapter();

		MapDetailsElement v = new MapDetailsElement();
		v.addEntry("test", 1);
		Map<String, Object> obj = adapter.unmarshal(v);
		Assert.assertEquals(1, obj.size(), "Invalid size.");

	}

}