package com.example.ecommerce.utils;

public class BiInput<T,U> {

	private T input1;
	private U input2;
	
	public BiInput(T input1,U input2) {
		this.input1=input1;
		this.input2=input2;
	}

	public T getInput1() {
		return input1;
	}

	public void setInput1(T input1) {
		this.input1 = input1;
	}

	public U getInput2() {
		return input2;
	}

	public void setInput2(U input2) {
		this.input2 = input2;
	}
	
}
