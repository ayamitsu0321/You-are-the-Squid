package ayamitsu.urtsquid.util;

import java.lang.reflect.Field;

import net.minecraft.entity.player.EntityPlayer;

public final class Reflector {

	private static final boolean isRenameTable;
	/*private static final boolean obfuscated = !net.minecraft.world.World.class.getSimpleName().equals("World");

	public static boolean isObfuscated() {
		return obfuscated;
	}*/

	public static boolean isRenameTable() {
		return isRenameTable;
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

	public static Field getField(Class clazz, Object instance, int i) {
		try {
			Field field = clazz.getDeclaredFields()[i];
			field.setAccessible(true);
			return field;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Object getPrivateValue(Class clazz, Object instance, int i) {
		try {
			Field field = getField(clazz, instance, i);
			return field.get(instance);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Object getPrivateValue(Class clazz, Object instance, String name) {
		try {
			Field field = getField(clazz, instance, name);
			return field.get(instance);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void setPrivateValue(Class clazz, Object instance, int i, Object obj) {
		try {
			Field field = getField(clazz, instance, i);
			field.set(instance, obj);
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

	static {
		Field itemInUse = null;

		try {
			itemInUse = EntityPlayer.class.getDeclaredField("itemInUse");
		} catch (NoSuchFieldException e) {}

		isRenameTable = itemInUse != null;
	}

}
