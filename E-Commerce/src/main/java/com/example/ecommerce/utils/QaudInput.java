package com.example.ecommerce.utils;

public class QaudInput<T,U,V,W>{
	private T input1;
	private U input2;
	private V input3;
	private W input4;
	
	public QaudInput(T input1,U input2,V input3,W input4) {
		this.input1=input1;
		this.input2=input2;
		this.input3=input3;
		this.input4=input4;
	}

	
	
	public W getInput4() {
		return input4;
	}



	public void setInput4(W input4) {
		this.input4 = input4;
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

	public V getInput3() {
		return input3;
	}

	public void setInput3(V input3) {
		this.input3 = input3;
	}

	
}