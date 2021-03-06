package org.getspout.server.io.blockstate;

import java.util.Map;

import org.getspout.server.block.SpoutCreatureSpawner;
import org.getspout.server.util.nbt.CompoundTag;
import org.getspout.server.util.nbt.IntTag;
import org.getspout.server.util.nbt.StringTag;
import org.getspout.server.util.nbt.Tag;

public class CreatureSpawnerStore extends BlockStateStore<SpoutCreatureSpawner> {
	public CreatureSpawnerStore() {
		super(SpoutCreatureSpawner.class, "MobSpawner");
	}


	@Override
	public void load(SpoutCreatureSpawner spawner, CompoundTag compound) {
		super.load(spawner, compound);
		spawner.setCreatureTypeId(compound.getValue().get("EntityId").getValue().toString());
		spawner.setDelay(((IntTag) compound.getValue().get("Delay")).getValue());
	}

	@Override
	public Map<String, Tag> save(SpoutCreatureSpawner spawner) {
		Map<String, Tag> ret = super.save(spawner);
		ret.put("EntityId", new StringTag("EntityId", spawner.getCreatureTypeId()));
		ret.put("Delay", new IntTag("Delay", spawner.getDelay()));
		return ret;
	}
}
