package io.jutil.jdo.internal.core.reflect;

import io.jutil.jdo.core.reflect.ExecutableOperation;
import io.jutil.jdo.core.reflect.MethodParameter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-02-17
 */
public abstract class DefaultExecutableOperation extends DefaultAnnotationOperation implements ExecutableOperation {
	private final Executable executable;
	private final List<Class<?>> superClassList;
	private final List<Class<?>> interfaceList;

	protected List<Class<?>> paramClassList;
	protected List<Executable> superExecutableList;
	protected List<MethodParameter> methodParamList;

	public DefaultExecutableOperation(Executable executable, List<Class<?>> superClassList, List<Class<?>> interfaceList) {
		super(executable);
		this.executable = executable;
		this.superClassList = superClassList;
		this.interfaceList = interfaceList;
		this.parse();
		this.parseAnnotation();
		this.parseParam();
	}

	protected void parse() {
		var paramClasses = executable.getParameterTypes();
		List<Class<?>> paramList = new ArrayList<>();
		for (var paramClass : paramClasses) {
			paramList.add(paramClass);
		}
		this.paramClassList = List.copyOf(paramList);
	}

	protected void parseAnnotation() {
		List<Executable> executableList = new ArrayList<>();
		Map<Class<?>, Annotation> annotationMap = new HashMap<>();
		this.parseAnnotation(executable, executableList, annotationMap);
		if (superClassList != null && !superClassList.isEmpty()) {
			for (var clazz : superClassList) {
				Executable destination = this.findExecutable(executable, clazz);
				if (destination == null) {
					continue;
				}

				this.parseAnnotation(destination, executableList, annotationMap);
			}
		}
		if (interfaceList != null && !interfaceList.isEmpty()) {
			for (var inter : interfaceList) {
				Executable destination = this.findExecutable(executable, inter);
				if (destination == null) {
					continue;
				}

				this.parseAnnotation(destination, executableList, annotationMap);
			}
		}

		this.superExecutableList = List.copyOf(executableList);
		this.initAnnotationMap(annotationMap);
	}

	protected void parseAnnotation(Executable executable, List<Executable> executableList,
	                               Map<Class<?>, Annotation> annotationMap) {
		executableList.add(executable);
		Annotation[] annotations = executable.getDeclaredAnnotations();
		for (var annotation : annotations) {
			if (annotationMap.containsKey(annotation.annotationType())) {
				continue;
			}

			annotationMap.put(annotation.annotationType(), annotation);
		}
	}

	protected void parseParam() {
		int count = executable.getParameterCount();
		List<MethodParameter> paramList = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			List<Parameter> list = new ArrayList<>();
			for (var superMethod : superExecutableList) {
				var params = superMethod.getParameters();
				list.add(params[i]);
			}
			var param = new DefaultMethodParameter(list);
			paramList.add(param);
		}
		this.methodParamList = List.copyOf(paramList);
	}



	@Override
	public final List<Class<?>> getParameterTypeList() {
		return paramClassList;
	}

	@Override
	public final List<MethodParameter> getParameterList() {
		return methodParamList;
	}

	@Override
	public final int getModifiers() {
		return executable.getModifiers();
	}

	@Override
	public String getName() {
		return executable.getName();
	}

	/**
	 * find overload executable, such as Method, Constructor
	 *
	 * @param src
	 * @param clazz
	 * @return
	 */
	protected abstract Executable findExecutable(Executable src, Class<?> clazz);

}
