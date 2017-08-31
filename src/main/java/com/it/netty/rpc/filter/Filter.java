package com.it.netty.rpc.filter;

public interface Filter<T> {
	 T doFilter(Object... objects );
}
