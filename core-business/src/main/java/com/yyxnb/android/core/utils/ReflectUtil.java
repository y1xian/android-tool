package com.yyxnb.android.core.utils;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * ReflectUtil
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/11/12
 */
public class ReflectUtil {

	private final Class<?> type;

	private final Object object;

	private ReflectUtil(final Class<?> type) {
		this(type, type);
	}

	private ReflectUtil(final Class<?> type, Object object) {
		this.type = type;
		this.object = object;
	}

	public ReflectUtil newInstance() {
		return newInstance(new Object[0]);
	}

	public ReflectUtil newInstance(Object... args) {
		Class<?>[] types = getArgsType(args);
		try {
			Constructor<?> constructor = type().getDeclaredConstructor(types);
			return newInstance(constructor, args);
		} catch (NoSuchMethodException e) {
			List<Constructor<?>> list = new ArrayList<>();
			for (Constructor<?> constructor : type().getDeclaredConstructors()) {
				if (match(constructor.getParameterTypes(), types)) {
					list.add(constructor);
				}
			}
			if (list.isEmpty()) {
				throw new RuntimeException(e);
			} else {
				sortConstructors(list);
				return newInstance(list.get(0), args);
			}
		}
	}

	private void sortConstructors(List<Constructor<?>> list) {
		list.sort((o1, o2) -> {
			Class<?>[] types1 = o1.getParameterTypes();
			Class<?>[] types2 = o2.getParameterTypes();
			int len = types1.length;
			for (int i = 0; i < len; i++) {
				if (!types1[i].equals(types2[i])) {
					if (wrapper(types1[i]).isAssignableFrom(wrapper(types2[i]))) {
						return 1;
					} else {
						return -1;
					}
				}
			}
			return 0;
		});
	}

	private boolean isSimilarSignature(final Method possiblyMatchingMethod,
									   final String desiredMethodName,
									   final Class<?>[] desiredParamTypes) {
		return possiblyMatchingMethod.getName().equals(desiredMethodName)
				&& match(possiblyMatchingMethod.getParameterTypes(), desiredParamTypes);
	}

	private boolean match(final Class<?>[] declaredTypes, final Class<?>[] actualTypes) {
		if (declaredTypes.length == actualTypes.length) {
			for (int i = 0; i < actualTypes.length; i++) {
				if (actualTypes[i] == NULL.class
						|| wrapper(declaredTypes[i]).isAssignableFrom(wrapper(actualTypes[i]))) {
					continue;
				}
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	private <T extends AccessibleObject> T accessible(T accessible) {
		if (accessible == null) {
			return null;
		}
		if (accessible instanceof Member) {
			Member member = (Member) accessible;
			if (Modifier.isPublic(member.getModifiers())
					&& Modifier.isPublic(member.getDeclaringClass().getModifiers())) {
				return accessible;
			}
		}
		if (!accessible.isAccessible()) {
			accessible.setAccessible(true);
		}
		return accessible;
	}

	private Class<?>[] getArgsType(final Object... args) {
		if (args == null) {
			return new Class[0];
		}
		Class<?>[] result = new Class[args.length];
		for (int i = 0; i < args.length; i++) {
			Object value = args[i];
			result[i] = value == null ? NULL.class : value.getClass();
		}
		return result;
	}

	private Class<?> wrapper(final Class<?> type) {
		if (type == null) {
			return null;
		} else if (type.isPrimitive()) {
			if (boolean.class == type) {
				return Boolean.class;
			} else if (int.class == type) {
				return Integer.class;
			} else if (long.class == type) {
				return Long.class;
			} else if (short.class == type) {
				return Short.class;
			} else if (byte.class == type) {
				return Byte.class;
			} else if (double.class == type) {
				return Double.class;
			} else if (float.class == type) {
				return Float.class;
			} else if (char.class == type) {
				return Character.class;
			} else if (void.class == type) {
				return Void.class;
			}
		}
		return type;
	}

	private static String property(String string) {
		int length = string.length();

		if (length == 0) {
			return "";
		} else if (length == 1) {
			return string.toLowerCase();
		} else {
			return string.substring(0, 1).toLowerCase() + string.substring(1);
		}
	}

	public <T> T get() {
		return (T) object;
	}

	private Class<?> type() {
		return type;
	}

	@Override
	public int hashCode() {
		return object.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof ReflectUtil && object.equals(((ReflectUtil) obj).get());
	}

	@Override
	public String toString() {
		return object.toString();
	}


	private static class NULL {
	}
}
