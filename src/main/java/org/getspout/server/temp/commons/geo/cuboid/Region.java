package org.getspout.server.temp.commons.geo.cuboid;

import org.getspout.server.temp.commons.geo.World;
import org.getspout.server.temp.commons.geo.point.Point;

/**
 * Represents a cube containing 16x16x16 Chunks (256x256x256 Blocks)
 */
public class Region extends Cube {
	private final static double EDGE = 256.0;

	public Region(World world, double x, double y, double z) {
		super(new Point(world, x, y, z), EDGE);
	}
}
