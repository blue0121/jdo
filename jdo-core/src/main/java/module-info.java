module jdo.core {
	requires java.sql;
	requires java.management;
	requires org.slf4j;
	requires com.zaxxer.hikari;

	requires static lombok;

	exports io.jutil.jdo.core.annotation;
	exports io.jutil.jdo.core.collection;
	exports io.jutil.jdo.core.engine;
	exports io.jutil.jdo.core.exception;
	exports io.jutil.jdo.core.parser;
	exports io.jutil.jdo.core.plugin;
	exports io.jutil.jdo.core.reflect;

}