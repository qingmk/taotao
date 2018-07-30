package com.taotao.cart.threadLocal;

import com.taotao.cart.bean.User;

public class UserThreadLocal {
	private static final ThreadLocal<User> local=new ThreadLocal<User>();
	public static void set(User user) {
		local.set(user);
	}
	
	
	public static User get() {
		return local.get();
	}
}
