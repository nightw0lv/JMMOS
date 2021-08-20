package gameserver.actor;

import gameserver.holders.NpcHolder;
import gameserver.holders.SpawnHolder;

/**
 * @author Pantelis Andrianakis
 * @version November 28th 2019
 */
public class Monster extends Npc
{
	public Monster(NpcHolder npcHolder, SpawnHolder spawnHolder)
	{
		super(npcHolder, spawnHolder);
		
		// TODO: AI Tasks.
		// TODO: Loot corpse.
	}
	
	@Override
	public boolean isMonster()
	{
		return true;
	}
	
	@Override
	public Monster asMonster()
	{
		return this;
	}
	
	public String ToString()
	{
		return "MONSTER [" + getNpcHolder().getNpcId() + "]";
	}
}
