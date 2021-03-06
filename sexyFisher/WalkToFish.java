package scripts.sexyFisher;

import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSTile;
import scripts.API.Fishing;
import scripts.API.Node;

import javax.print.DocFlavor;
import java.util.LinkedList;
import java.util.function.BooleanSupplier;

public class WalkToFish extends Node {

    private LinkedList<String> tools = new LinkedList<String>();
    private RSTile fishingLocation;
    private String fishingSpotName;

    public WalkToFish(Fishing.TOOLS fishingMethod, Fishing.LOCATION location){

        switch (location){
            case LUMBRIDGE_SWAMP:
                this.fishingLocation = Fishing.LOCATION.LUMBRIDGE_SWAMP.getAsRSTile();
                break;
            case KARAMJA_DOCK:
                this.fishingLocation = Fishing.LOCATION.KARAMJA_DOCK.getAsRSTile();
                tools.add("Coins");
                break;
            case BARBARIAN_VILLAGE:
                this.fishingLocation = Fishing.LOCATION.BARBARIAN_VILLAGE.getAsRSTile();
                break;
        }

        switch (fishingMethod){
            case FLY:
                tools.clear();
                tools.add("Feather");
                tools.add("Fly fishing rod");
                this.fishingSpotName = "Rod Fishing spot";
                break;
            case LOBSTER:
                tools.clear();
                tools.add("Lobster pot");
                this.fishingSpotName = "Fishing spot";
                break;
            case SMALLNET:
                tools.clear();
                tools.add("Small fishing net");
                this.fishingSpotName = "Fishing spot";
                break;
            case BIGNET:
                tools.clear();
                tools.add("Big fishing net");
                break;
            case HARPOON:
                tools.clear();
                tools.add("Harpoon");
                this.fishingSpotName = "Fishing spot";
                break;
            case BAIT:
                tools.clear();
                tools.add("Fishing bait");
                tools.add("Fishing rod");
                this.fishingSpotName = "Rod Fishing spot";
                break;
        }
    }

    private boolean inventoryCheck(){
        if(Inventory.isFull()){
            return false;
        }
        for(String tool : tools){
            if(Inventory.getCount(tool) == 0){
                System.out.println(tool);
                return false;
            }
            if((tool.equals("Feather") || tool.equals("Fishing bait")) && Inventory.getCount(tool) < 27)//we don't have enough bait/feathers for a full inventory
                return false;
            if(tool.equals("Coins") && Inventory.getCount(tool) < 60)//test for karamja dock fishing
                return false;
        }
        return true;
    }

    @Override
    public void execute() {
        System.out.println("Walking to fish1");
        if(!WebWalking.walkTo(fishingLocation, new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() {
                return Interfaces.get(595, 37) != null;//webwalking opened the world map
            }
        }, 500))
            System.out.println("FAILED");
        else
            System.out.println("SUCCESS");
        if(Interfaces.get(595, 37) != null)
            Interfaces.get(595, 37).click("Close");
    }

    @Override
    public boolean validate() {
        if(!Fishing.isAtFish(fishingLocation, fishingSpotName)){
            if(inventoryCheck())
                return true;
        }
        return false;
    }
}