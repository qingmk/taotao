package com.taotao.common.service;
/**
 * 抽取重复代码
 * @author Administrator
 *
 * @param <T>
 * @param <E>
 */
public interface Function<T,E> {
	public T callback(E e);
}
