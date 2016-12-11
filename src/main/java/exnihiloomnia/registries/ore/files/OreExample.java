package exnihiloomnia.registries.ore.files;

import exnihiloomnia.registries.ore.Ore;
import exnihiloomnia.registries.ore.pojos.POJOreList;
import exnihiloomnia.util.Color;
import net.minecraft.init.Items;

import java.util.Collections;

public abstract class OreExample {
	
	public static POJOreList getExampleRecipeList() {
		POJOreList example = new POJOreList();
		
		example.addEntry(new Ore("steel", new Color("F2AB7C"), 30, true, false, false).setOreDictNames(Collections.singletonList("HardIron")).toPOJOre());
		example.addEntry(new Ore("thingy", new Color("F2AFCC"), 100, true, true, true).setOreDictNames(Collections.singletonList("Wow")).setIngot(Items.ACACIA_BOAT).toPOJOre());

		return example;
	}
}
