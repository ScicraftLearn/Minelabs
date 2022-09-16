package be.uantwerpen.minelabs.network;

import be.uantwerpen.minelabs.Minelabs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class NetworkingConstants {
    public static final Identifier LEWISDATASYNC = new Identifier(Minelabs.MOD_ID, "lewis_data_sync");
    public static final Identifier IONICDATASYNC = new Identifier(Minelabs.MOD_ID, "ionic_data_sync");

    public static final Identifier LABCHESTDATASYNC = new Identifier(Minelabs.MOD_ID, "lab_chest_data_sync");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(LABCHESTDATASYNC, LabChestDataPacket::receive);
    }

    public static void registerS2CPackets() {

    }
}
