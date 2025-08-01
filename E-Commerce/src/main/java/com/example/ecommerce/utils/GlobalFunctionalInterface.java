package com.example.ecommerce.utils;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface GlobalFunctionalInterface {

	public static <T, U,V,W,R> void allFunction(Consumer<QaudInput<T,U,V,W>> consumer, T input1,U input2,V input3,W input4) {
		 QaudInput<T, U, V,W> quadInput = new QaudInput<>(input1, input2, input3,input4);
	        consumer.accept(quadInput);
	}
	

}

