package dev.efekos.cla.block.entity;

import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public interface SyncAbleBlockEntity<T extends CustomPayload> {

    T createSyncPacket();


    default Vec3d findRandomPos(Box box){
        Random random = new Random();
        double x = random.nextDouble(box.minX,box.maxX);
        double y = random.nextDouble(box.minY,box.maxY);
        double z = random.nextDouble(box.minZ,box.maxZ);
        return new Vec3d(x,y,z);
    }

}
