package com.gmail.berndivader.mythicmobsext.utils.math;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.gmail.berndivader.mythicmobsext.utils.Utils;
import com.gmail.berndivader.mythicmobsext.utils.Vec2D;
import com.gmail.berndivader.mythicmobsext.utils.Vec3D;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;

/**
 * Various math utility methods.
 */
public final class MathUtils {

	private static BlockFace[] axis = { BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH, BlockFace.EAST };
	private static BlockFace[] radial = { BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST,
			BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST };

	public static final float DEGTORAD = 0.017453293F;
	public static final float RADTODEG = 57.29577951F;

	/**
	 * Safe minimum, such that 1 / SAFE_MIN does not overflow.
	 *
	 * <p>
	 * In IEEE 754 arithmetic, this is also the smallest normalized number
	 * 2<sup>-1022</sup>. The value of this constant is from Apache Commons Math
	 * 2.2.
	 * </p>
	 */
	public static final double SAFE_MIN = 0x1.0p-1022;

	private MathUtils() {
	}

	public static float getLookAtYaw(Entity loc, Entity lookat) {
		return getLookAtYaw(loc.getLocation(), lookat.getLocation());
	}

	public static float getLookAtYaw(Block loc, Block lookat) {
		return getLookAtYaw(loc.getLocation(), lookat.getLocation());
	}

	public static float getLookAtYaw(Location loc, Location lookat) {
		return getLookAtYaw(lookat.getX() - loc.getX(), lookat.getZ() - loc.getZ());
	}

	public static float getLookAtYaw(Vector motion) {
		return getLookAtYaw(motion.getX(), motion.getZ());
	}

	public static float getLookAtYaw(double dx, double dz) {
		float yaw = 0;
		if (dx != 0) {
			if (dx < 0) {
				yaw = 270;
			} else {
				yaw = 90;
			}
			yaw -= atan(dz / dx);
		} else if (dz < 0) {
			yaw = 180;
		}
		return -yaw - 90;
	}

	public static Vector rotate(float yaw, float pitch, Vector value) {
		return rotate(yaw, pitch, value.getX(), value.getY(), value.getZ());
	}

	public static Vector rotate(float yaw, float pitch, double x, double y, double z) {
		float angle;
		angle = yaw * DEGTORAD;
		double sinyaw = Math.sin(angle);
		double cosyaw = Math.cos(angle);
		angle = pitch * DEGTORAD;
		double sinpitch = Math.sin(angle);
		double cospitch = Math.cos(angle);
		double newx = 0.0;
		double newy = 0.0;
		double newz = 0.0;
		newz -= x * cosyaw;
		newz -= y * sinyaw * sinpitch;
		newz -= z * sinyaw * cospitch;
		newx += x * sinyaw;
		newx -= y * cosyaw * sinpitch;
		newx -= z * cosyaw * cospitch;
		newy += y * cospitch;
		newy -= z * sinpitch;
		return new Vector(newx, newy, newz);
	}

	public static float normalise(float v, float s, float e) {
		float w = e - s, o = v - s;
		return (float) ((o - (Math.floor(o / w) * w)) + s);
	}

	public static Location move(Location loc, Vector offset) {
		return move(loc, offset.getX(), offset.getY(), offset.getZ());
	}

	public static Location move(Location loc, double dx, double dy, double dz) {
		Vector off = rotate(loc.getYaw(), loc.getPitch(), dx, dy, dz);
		double x = loc.getX() + off.getX();
		double y = loc.getY() + off.getY();
		double z = loc.getZ() + off.getZ();
		return new Location(loc.getWorld(), x, y, z, loc.getYaw(), loc.getPitch());
	}

	/**
	 * atan, fixed-style
	 * 
	 * @param double value
	 * @return float
	 */
	private static float atan(double value) {
		return RADTODEG * (float) Math.atan(value);
	}

	/**
	 * Modulus, divisor-style.
	 *
	 * @param a a
	 * @param n n
	 * @return the modulus
	 */
	public static int divisorMod(int a, int n) {
		return (int) (a - n * Math.floor(Math.floor(a) / n));
	}

	/**
	 * Returns the cosine of an angle given in degrees. This is better than just
	 * {@code Math.cos(Math.toRadians(degrees))} because it provides a more accurate
	 * result for angles divisible by 90 degrees.
	 *
	 * @param degrees the angle
	 * @return the cosine of the given angle
	 */
	public static double dCos(double degrees) {
		int dInt = (int) degrees;
		if (degrees == dInt && dInt % 90 == 0) {
			dInt %= 360;
			if (dInt < 0) {
				dInt += 360;
			}
			switch (dInt) {
			case 0:
				return 1.0;
			case 90:
				return 0.0;
			case 180:
				return -1.0;
			case 270:
				return 0.0;
			}
		}
		return Math.cos(Math.toRadians(degrees));
	}

	/**
	 * Returns the sine of an angle given in degrees. This is better than just
	 * {@code Math.sin(Math.toRadians(degrees))} because it provides a more accurate
	 * result for angles divisible by 90 degrees.
	 *
	 * @param degrees the angle
	 * @return the sine of the given angle
	 */
	public static double dSin(double degrees) {
		int dInt = (int) degrees;
		if (degrees == dInt && dInt % 90 == 0) {
			dInt %= 360;
			if (dInt < 0) {
				dInt += 360;
			}
			switch (dInt) {
			case 0:
				return 0.0;
			case 90:
				return 1.0;
			case 180:
				return 0.0;
			case 270:
				return -1.0;
			}
		}
		return Math.sin(Math.toRadians(degrees));
	}

	public static double lerp(double point1, double point2, double alpha) {
		return point1 + alpha * (point2 - point1);
	}

	public static float smoothstep(float f, boolean clamp) {
		float ff = f * f * (3.0f - 2.0f * f);
		return clamp(ff, clamp);
	}

	public static float clamp(float val, float min, float max) {
		return Math.max(min, Math.min(max, val));
	}

	public static double clamp(double val, double min, double max) {
		return Math.max(min, Math.min(max, val));
	}

	public static float clamp(float y, boolean clamp) {
		return clamp ? y < 0 ? 0 : y > 1 ? 1 : y : y;
	}

	public static double clamp(double y, boolean clamp) {
		return clamp ? y < 0 ? 0 : y > 1 ? 1 : y : y;
	}

	public static float normalize(float value, float min, float max) {
		float delta = value - min, delta_1 = max - min;
		return delta / delta_1;
	}

	public static double normalize(double value, double min, double max) {
		double delta = value - min, delta_1 = max - min;
		return delta / delta_1;
	}

	public static double scale(double value, double min, double max) {
		return value / (max - min) + min;
	}

	public static boolean isNumeric(String s) {
		return s != null ? s.matches("[0-9]*") : false;
	}

	public static Vector calculateVelocity(Vector from, Vector to, double gravity, double heightGain) {

		int endGain = to.getBlockY() - from.getBlockY();
		double horizDist = Math.sqrt(distanceSquared(from, to));
		double maxGain = heightGain > (endGain + heightGain) ? heightGain : (endGain + heightGain);

		double a = -horizDist * horizDist / (4 * maxGain);
		double b = horizDist;
		double c = -endGain;

		double slope = -b / (2 * a) - Math.sqrt(b * b - 4 * a * c) / (2 * a);
		double vy = Math.sqrt(maxGain * (gravity + 0.0013675090252708 * heightGain));
		double vh = vy / slope;

		int dx = to.getBlockX() - from.getBlockX();
		int dz = to.getBlockZ() - from.getBlockZ();
		double mag = Math.sqrt(dx * dx + dz * dz);
		double dirx = dx / mag;
		double dirz = dz / mag;

		double vx = vh * dirx;
		double vz = vh * dirz;

		return new Vector(vx, vy, vz);
	}

	private static double distanceSquared(Vector from, Vector to) {

		double dx = to.getBlockX() - from.getBlockX();
		double dz = to.getBlockZ() - from.getBlockZ();

		return dx * dx + dz * dz;
	}

	public static Vector spread(Vector from, double yaw, double pitch) {
		Vector vec = from.clone();

		float cosyaw = (float) Math.cos(yaw);
		float cospitch = (float) Math.cos(pitch);
		float sinyaw = (float) Math.sin(yaw);
		float sinpitch = (float) Math.sin(pitch);
		float bX = (float) (vec.getY() * sinpitch + vec.getX() * cospitch);
		float bY = (float) (vec.getY() * cospitch - vec.getX() * sinpitch);
		return new Vector(bX * cosyaw - vec.getZ() * sinyaw, bY, bX * sinyaw + vec.getZ() * cosyaw);
	}

	public static double launchAngle(Location from, Vector to, double v, double elev, double g) {
		Vector victor = from.toVector().subtract(to);
		Double dist = Math.sqrt(Math.pow(victor.getX(), 2) + Math.pow(victor.getZ(), 2));
		double v2 = Math.pow(v, 2);
		double v4 = Math.pow(v, 4);
		double derp = g * (g * Math.pow(dist, 2) + 2 * elev * v2);
		if (v4 < derp) {
			return Math.atan((2 * g * elev + v2) / (2 * g * elev + 2 * v2));
		} else {
			return Math.atan((v2 - Math.sqrt(v4 - derp)) / (g * dist));
		}
	}

	public static double hangtime(double launchAngle, double v, double elev, double g) {
		double a = v * Math.sin(launchAngle);
		double b = -2 * g * elev;
		if (Math.pow(a, 2) + b < 0) {
			return 0;
		}
		return (a + Math.sqrt(Math.pow(a, 2) + b)) / g;
	}

	public static Vec2D calculateDirectionVec2D(Vec3D target_position, float velocity, float G) {
		double x = target_position.getX();
		double y = target_position.getY();
		double z = target_position.getZ();

		float yaw = (float) (Math.atan2(z, x) * 180 / Math.PI) - 90;
		double distance = Math.sqrt(x * x + z * z);
		float pitch = (float) -Math
				.toDegrees(Math.atan((velocity * velocity - Math.sqrt((float) (velocity * velocity * velocity * velocity
						- G * (G * (distance * distance) + 2 * y * (velocity * velocity))))) / (G * distance)));
		return new Vec2D(yaw, pitch);
	}

	public static Vector calculateDirectionVector(Vec3D target_position, float velocity, float G) {
		Vec2D vec2d = calculateDirectionVec2D(target_position, velocity, G);
		return getDirection((float) vec2d.getX(), (float) vec2d.getY());
	}

	public static Vector getDirection(float yaw, float pitch) {
		Vector vector = new Vector();

		double rotX = DEGTORAD * yaw;
		double rotY = DEGTORAD * pitch;
		vector.setY(-Math.sin(rotY));
		double h = Math.cos(rotY);
		vector.setX(-h * Math.sin(rotX));
		vector.setZ(h * Math.cos(rotX));
		return vector;
	}

	public static BlockFace getBlockFacing(float y, boolean bl1) {
		return bl1 ? radial[Math.round(y / 45f) & 0x7] : axis[Math.round(y / 90f) & 0x3];
	}

	@Deprecated
	public static Vector getSideOffsetVector(float vYa, double hO, boolean iy) {
		double y = 0d;
		if (!iy)
			y = Math.toRadians(vYa);
		double xo = Math.cos(y) * hO;
		double zo = Math.sin(y) * hO;
		return new Vector(xo, 0d, zo);
	}

	public static Vector getSideOffsetVectorFixed(float vYa, double hO, boolean iy) {
		double y = 0d;
		if (!iy)
			y = Math.toRadians(vYa);
		double xo = Math.cos(y) * hO;
		double zo = Math.sin(y) * hO;
		return new Vector(xo, y, zo);
	}

	public static Vector getFrontBackOffsetVector(Vector v, double o) {
		Vector d = v.clone();
		d.normalize();
		d.multiply(o);
		return d;
	}

	public static Vector getForwardOffsetVector(Location location, double length) {
		return location.getDirection().clone().multiply(length);
	}

	public static float lookAtYaw(Location loc, Location lookat) {
		loc = loc.clone();
		lookat = lookat.clone();
		float yaw = 0.0F;
		double dx = lookat.getX() - loc.getX();
		double dz = lookat.getZ() - loc.getZ();
		if (dx != 0) {
			if (dx < 0) {
				yaw = (float) (1.5 * Math.PI);
			} else {
				yaw = (float) (0.5 * Math.PI);
			}
			yaw = yaw - (float) Math.atan(dz / dx);
		} else if (dz < 0) {
			yaw = (float) Math.PI;
		}
		yaw = -yaw * 180f / (float) Math.PI;
		return yaw;
	}

	public static Vec2D lookAtVec(Vector loc, Vector lookat) {
		float yaw = 0.0F;
		double dx = lookat.getX() - loc.getX(), dz = lookat.getZ() - loc.getZ(), dy = lookat.getY() - loc.getY();
		double dxz = Math.sqrt(Math.pow(dx, 2) + Math.pow(dz, 2));
		if (dx != 0) {
			if (dx < 0) {
				yaw = (float) (1.5 * Math.PI);
			} else {
				yaw = (float) (0.5 * Math.PI);
			}
			yaw = yaw - (float) Math.atan(dz / dx);
		} else if (dz < 0) {
			yaw = (float) Math.PI;
		}
		float pitch = (float) -Math.atan(dy / dxz);
		return new Vec2D(-yaw * 180f / Math.PI, pitch * 180f / Math.PI);
	}

	public static Vec2D lookAtVec(Location loc, Location lookat) {
		loc = loc.clone();
		lookat = lookat.clone();
		float yaw = 0.0f, pitch = yaw;
		double dx = lookat.getX() - loc.getX(), dz = lookat.getZ() - loc.getZ(), dy = lookat.getY() - loc.getY();
		double dxz = Math.sqrt(Math.pow(dx, 2) + Math.pow(dz, 2));
		if (dx != 0) {
			if (dx < 0) {
				yaw = (float) (1.5 * Math.PI);
			} else {
				yaw = (float) (0.5 * Math.PI);
			}
			yaw = yaw - (float) Math.atan(dz / dx);
		} else if (dz < 0) {
			yaw = (float) Math.PI;
		}
		pitch = (float) -Math.atan(dy / dxz);
		return new Vec2D(-yaw * 180f / Math.PI, pitch * 180f / Math.PI);
	}

	public static Location moveTo(Location loc, Vector offset) {
		float ryaw = -loc.getYaw() / 180f * (float) Math.PI;
		float rpitch = loc.getPitch() / 180f * (float) Math.PI;
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		z -= offset.getX() * Math.sin(ryaw);
		z += offset.getY() * Math.cos(ryaw) * Math.sin(rpitch);
		z += offset.getZ() * Math.cos(ryaw) * Math.cos(rpitch);
		x += offset.getX() * Math.cos(ryaw);
		x += offset.getY() * Math.sin(rpitch) * Math.sin(ryaw);
		x += offset.getZ() * Math.sin(ryaw) * Math.cos(rpitch);
		y += offset.getY() * Math.cos(rpitch);
		y -= offset.getZ() * Math.sin(rpitch);
		return new Location(loc.getWorld(), x, y, z, loc.getYaw(), loc.getPitch());
	}

	public static AbstractLocation getCircleLoc(Location c, double rX, double rZ, double rY, double air) {
		double x = c.getX() + rX * Math.cos(air);
		double z = c.getZ() + rZ * Math.sin(air);
		double y = c.getY() + rY * Math.cos(air);
		Location loc = new Location(c.getWorld(), x, y, z);
		Vector difference = c.toVector().clone().subtract(loc.toVector());
		loc.setDirection(difference);
		return BukkitAdapter.adapt(loc);
	}

	public static double round(double value, int places) {
		return new BigDecimal(value).round(new MathContext(places, RoundingMode.HALF_UP)).doubleValue();
	}

	public static double distance2D(Vector f, Vector t) {
		double dx = t.getBlockX() - f.getBlockX();
		double dz = t.getBlockZ() - f.getBlockZ();
		return dx * dx + dz * dz;
	}

	public static double distance3D(Vector f, Vector t) {
		double dx = t.getBlockX() - f.getBlockX(), dy = t.getBlockY() - f.getBlockY(),
				dz = t.getBlockZ() - f.getBlockZ();
		return dx * dx + dz * dz + dy * dy;
	}

	public static double distance3D(double x1, double y1, double z1, double x2, double y2, double z2) {
		return Math.pow(x1 - x2, 2d) + Math.pow(y1 - y2, 2d) + Math.pow(z1 - z2, 2d);
	}

	public static double distance2D(double x1, double z1, double x2, double z2) {
		return Math.pow(x1 - x2, 2d) + Math.pow(z1 - z2, 2d);
	}

	public static Location getLocationInFront(Location start, double range) {
		return start.clone().add(start.getDirection().setY(0).normalize().multiply(range));
	}

	public static int randomRangeInt(String range) {
		ThreadLocalRandom r = ThreadLocalRandom.current();
		int amount = 0;
		String[] split;
		int min, max;
		if (range.contains("to")) {
			split = range.split("to");
			min = Integer.parseInt(split[0]);
			max = Integer.parseInt(split[1]);
			if (max < min)
				max = min;
			amount = r.nextInt(min, max + 1);
		} else
			amount = Integer.parseInt(range);
		return amount;
	}

	public static double randomRangeDouble(String range) {
		ThreadLocalRandom r = ThreadLocalRandom.current();
		double amount = 0.0D;
		String[] split;
		double min, max;
		if (range.contains("to")) {
			split = range.split("to");
			min = Double.parseDouble(split[0]);
			max = Double.parseDouble(split[1]);
			if (max < min)
				max = min;
			amount = r.nextDouble(min, max);
		} else
			amount = Double.parseDouble(range);
		return amount;
	}

	public static byte encodeAngle(float angle) {
		return (byte) (angle * 256f / 360f);
	}

	public static int encodeVelocity(double v) {
		return (int) (v * 8000D);
	}

	public static long encodePosition(double d) {
		return (long) (d * 4096D);
	}

	public static boolean isHeadingTo(Vector offset, Vector velocity) {
		double dbefore = offset.lengthSquared();
		if (dbefore < 0.0001) {
			return true;
		}
		Vector clonedVelocity = velocity.clone();
		setVecLenSqrt(clonedVelocity, dbefore);
		return dbefore > clonedVelocity.subtract(offset).lengthSquared();
	}

	public static void setVecLen(Vector vector, double length) {
		setVecLenSqrt(vector, Math.signum(length) * length * length);
	}

	public static void setVecLenSqrt(Vector vector, double lengthsquared) {
		double vlength = vector.lengthSquared();
		if (Math.abs(vlength) > 0.0001) {
			if (lengthsquared < 0) {
				vector.multiply(-Math.sqrt(-lengthsquared / vlength));
			} else {
				vector.multiply(Math.sqrt(lengthsquared / vlength));
			}
		}
	}

	public static boolean playerInMotion(Player p) {
		Vec3D v3 = Utils.players.get(p.getUniqueId());
		return Math.abs(v3.getX()) > 0 || Math.abs(v3.getY()) > 0 || Math.abs(v3.getZ()) >= 0;
	}

	/**
	 * Checks if the source location is inside the cuboid defined by edge_1 and
	 * edge_2
	 * 
	 * @param source {@link Location} The source/destination location
	 * @param edge_1 {@link Location} An edge of the cubiod to check
	 * @param edge_2 {@link Location} The opposite edge to check
	 * @return {@link Boolean}
	 */
	public static boolean inCubiod(Location source, Location edge_1, Location edge_2) {
		double edge_1x = edge_1.getX(), edge_1y = edge_1.getX(), edge_1z = edge_1.getZ();
		double edge_2x = edge_2.getX(), edge_2y = edge_2.getX(), edge_2z = edge_2.getZ();
		Vector max = new Vector(Math.max(edge_1x, edge_2x), Math.max(edge_1y, edge_2y), Math.max(edge_1z, edge_2z));
		Vector min = new Vector(Math.min(edge_1x, edge_2x), Math.min(edge_1y, edge_2y), Math.min(edge_1z, edge_2z));
		return source.toVector().isInAABB(min, max);
	}

}
