module jdo.spring.boot.starter {
	requires java.sql;
	requires jdo.core;
	requires spring.core;
	requires spring.aop;
	requires spring.beans;
	requires spring.context;
	requires spring.jdbc;
	requires spring.tx;
	requires spring.boot;
	requires spring.boot.autoconfigure;

	requires static lombok;

	exports io.jutil.jdo.spring.annotation;
	exports io.jutil.jdo.spring.engine;

	exports io.jutil.jdo.internal.spring.config to spring.beans, spring.context, spring.boot;

	opens io.jutil.jdo.internal.spring.config to spring.core;
}