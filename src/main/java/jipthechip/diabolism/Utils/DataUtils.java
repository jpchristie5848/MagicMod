package jipthechip.diabolism.Utils;

import io.wispforest.owo.util.ImplementedInventory;
import jipthechip.diabolism.data.MagicElement;
import jipthechip.diabolism.data.brewing.Fluid;
import jipthechip.diabolism.data.spell.Wand;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;

import java.io.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class DataUtils {

    public static byte[] floatToByteArr(float [] arr) {
        if(arr != null){
            try {
                ByteArrayOutputStream bas = new ByteArrayOutputStream();
                DataOutputStream ds = new DataOutputStream(bas);
                for (float f : arr)
                    ds.writeFloat(f);

                return bas.toByteArray();
            }catch(IOException e){
                System.out.println("failed to convert float to byte arr: "+ Arrays.toString(e.getStackTrace()));
            }
        }
        return new byte[0];
    }

    public static float[] byteToFloatArr(byte [] arr) {
        if(arr != null) {
            try {
                ByteArrayInputStream bas = new ByteArrayInputStream(arr);
                DataInputStream ds = new DataInputStream(bas);
                float[] fArr = new float[arr.length / 4];  // 4 bytes per float
                for (int i = 0; i < fArr.length; i++) {
                    fArr[i] = ds.readFloat();
                }
                return fArr;
            } catch (IOException e) {
                System.out.println("failed to convert byte to float arr: " + Arrays.toString(e.getStackTrace()));
            }
        }
        return new float[0];
    }

    /** Read the object from Base64 string. */
    public static Object DeserializeFromString( String s ){
        byte [] data = Base64.getDecoder().decode( s );
        try{
            ObjectInputStream ois = new ObjectInputStream(
                    new ByteArrayInputStream(  data ) );
            Object o  = ois.readObject();
            ois.close();
            return o;
        }catch(Exception e){
            System.out.println("error deserializing object: "+e.getMessage());
            return null;
        }
    }

    /** Write the object to a Base64 string. */
    public static String SerializeToString( Serializable o ) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try{
            ObjectOutputStream oos = new ObjectOutputStream( baos );
            oos.writeObject( o );
            oos.close();

            return Base64.getEncoder().encodeToString(baos.toByteArray());
        }catch(IOException e){
            System.out.println("error serializing object of class '"+o.getClass()+"': "+e.toString());
            return null;
        }
    }

    public static <T extends Serializable> NbtCompound writeObjectToItemNbt(ItemStack stack, T obj){
        NbtCompound nbt = new NbtCompound();
        String key;
        if(obj instanceof Fluid){
            key = Fluid.class.getName().toLowerCase();
        }
        else{
            key = obj.getClass().getName().toLowerCase();
        }
        nbt.putString(key, SerializeToString(obj));
        stack.setNbt(nbt);
        return nbt;
    }

    public static <T extends Serializable> T readObjectFromItemNbt(ItemStack stack, Class<T> objClass){
        NbtCompound nbt = stack.getNbt();
        if(nbt != null){
            return objClass.cast(DeserializeFromString(nbt.getString(objClass.getName().toLowerCase())));
        }
        return null;
    }

    public static String getMapString(HashMap<?, ?> map){
        if(map == null) return "{}";
        return "{"+map.entrySet().stream().map(entry -> {
            try{
                String key = entry.getKey() != null ? entry.getKey().toString() : "null";
                String value = entry.getValue() != null ? entry.getValue().toString() : "null";
                return "(" + key + ", " + value + ")";
            }catch(Exception e){
                System.out.println("Failed to convert HashMap entry to string: "+e.getMessage());
                return "(Invalid Entry)";
            }
        }).collect(Collectors.joining(", "))+"}";
    }
}
