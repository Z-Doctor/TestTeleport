package zdoctor.testmod.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import zdoctor.testmod.init.ZItems;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent e) {
		ZItems.preInit();
	}

	public void init(FMLInitializationEvent e) {

	}

	public void postInit(FMLPostInitializationEvent e) {
	}

}