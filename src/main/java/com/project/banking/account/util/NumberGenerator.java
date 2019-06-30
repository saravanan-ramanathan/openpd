package com.project.banking.account.util;

import java.util.Random;

public class NumberGenerator {

	private static long MIN = 10000000;
	private static long MAX = 20000000;

	public static Long getNextNumber() {
		Random random = new Random();
		return random.longs(MIN, MAX).findFirst().getAsLong();
	}
}
