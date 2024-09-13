package dev.efekos.cla.block.entity;

import net.minecraft.network.packet.CustomPayload;

public interface SyncAbleBlockEntity<T extends CustomPayload> {

    T createSyncPacket();

}
