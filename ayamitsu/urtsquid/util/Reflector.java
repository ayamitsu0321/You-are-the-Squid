package ayamitsu.urtsquid.util;

import java.lang.reflect.Field;

public final class Reflector {

	private static final boolean obfuscated = !net.minecraft.world.World.class.getSimpleName().equals("World");

	public static boolean isObfuscated() {
		return obfuscated;
	}

	public static Field getField(Class clazz, Object instance, String name) {
		try {
			Field field = clazz.getDeclaredField(name);
			field.setAccessible(true);
			return field;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Object getPrivateValue(Class clazz, Object instance, String name) {
		try {
			Field field = getField(clazz, instance, name);
			return field.get(instance);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void setPrivateValue(Class clazz, Object instance, String name, Object obj) {
		try {
			Field field = getField(clazz, instance, name);
			field.set(instance, obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
