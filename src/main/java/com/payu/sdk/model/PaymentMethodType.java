/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 developers-payu-latam
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.payu.sdk.model;

/**
 * Enum representing a payment method type in the PayU SDK.
 *
 * @author PayU Latam
 * @since 1.0.0
 * @version 1.0.0, 21/08/2013
 */
public enum PaymentMethodType {

	/**
	 * Payment with credit card.
	 */
	CREDIT_CARD(2, "CREDIT_CARD"),

	/**
	 * Payment using PSE.
	 */
	PSE(4, "PSE"),

	/**
	 * Cash Payment
	 */
	CASH(7, "CASH"),

	/**
	 * Referenced payment.
	 */
	REFERENCED(8, "REFERENCED"),

	/**
	 * Payment with check account.
	 */
	CHECK_ACCOUNT(1, "CHECK_ACCOUNT"),

	/**
	 * Payment using verified by visa.
	 */
	VERIFIED_BY_VISA(3, "VERIFIED_BY_VISA"),

	/**
	 * Payment using ACH.
	 */
	ACH(5, "ACH"),

	/**
	 * Payment using debit card.
	 */
	DEBIT_CARD(6, "DEBIT_CARD"),

	/**
	 * Payment with Special card.
	 */
	SPECIAL_CARD(9, "SPECIAL_CARD");

	/**
	 * The payment method identifier.
	 */
	private Integer id;

	/** The payment method description. */
	private String description;

	/**
	 * The constructor which receives all the enum data.
	 *
	 * @param id The payment Method type id.
	 * @param description The payment Method type description.
	 */
	private PaymentMethodType(Integer id, String description) {
		this.id = id;
		this.description = description;
	}

	/**
	 * Returns the payment method type identifier.
	 *
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Returns the payment method type description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
}
