package dev.efekos.cla.client.renderer;

import dev.efekos.cla.Main;
import net.minecraft.util.Identifier;

public record SignTextures(String def,String urgent) {

    Identifier urgentId(){
        return Identifier.of(Main.MOD_ID,"textures/gui/sign/" + urgent + ".png");
    }

    Identifier defId(){
        return Identifier.of(Main.MOD_ID,"textures/gui/sign/" + def + ".png");
    }

    Identifier get(boolean urgent){
        return urgent ? urgentId() : defId();
    }

}
