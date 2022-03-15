package deno;

import Hagbrain.Entity.EntityNPC;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.TextFormat;

import java.util.UUID;

public class EntityFarbenmeister extends EntityNPC {

    public EntityFarbenmeister(FullChunk chunk, CompoundTag nbt) {

        super(chunk, nbt);

    }

    @Override
    public void initEntity() {
        if (!this.namedTag.contains("EntityName")){
            this.setNameTag(TextFormat.colorize("&4Farbenmeister"));
            this.namedTag.putString("EntityName", TextFormat.colorize("&4Farbenmeister"));
            this.namedTag.putBoolean("isBaby", false);
        }
        this.ItemInHand = Item.get(ItemID.STICK);
        this.skin = LoadSkin("Farbenmeister");

        super.initEntity();
    }

    @Override
    public boolean attack(EntityDamageEvent e) {

        Entity entity = ((EntityDamageByEntityEvent) e).getDamager();
        if (!(entity instanceof Player))
            return false;

        Player p = (Player) entity;

        Server.getInstance().dispatchCommand(p, "farbarena");
        return false;

    }

    @Override
    public UUID getUniqueId() {

        return UUID.randomUUID();

    }

}