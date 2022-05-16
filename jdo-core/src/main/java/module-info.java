module jdo.core {
	requires java.sql;
	requires java.naming;
	requires java.management;
	requires org.slf4j;
	requires druid;

	requires static lombok;

	exports io.jutil.jdo.core.annotation;
	exports io.jutil.jdo.core.collection;
	exports io.jutil.jdo.core.engine;
	exports io.jutil.jdo.core.exception;
	exports io.jutil.jdo.core.parser;
	exports io.jutil.jdo.core.reflect;


}