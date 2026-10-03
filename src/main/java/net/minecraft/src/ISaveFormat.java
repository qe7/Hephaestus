package net.minecraft.src;

import java.util.List;

public interface ISaveFormat {
    String func_22178_a();

    ISaveHandler getSaveLoader(String s, boolean flag);

    List func_22176_b();

    void flushCache();

    WorldInfo func_22173_b(String s);

    void func_22172_c(String s);

    void func_22170_a(String s, String s1);

    boolean isOldMapFormat(String s);

    boolean convertMapFormat(String s, IProgressUpdate iprogressupdate);
}
