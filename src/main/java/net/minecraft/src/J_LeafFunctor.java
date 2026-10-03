package net.minecraft.src;

abstract class J_LeafFunctor implements J_Functor {

    J_LeafFunctor() {
    }

    public final Object func_27059_b(Object obj) {
        if (!func_27058_a(obj)) {
            throw J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException.func_27322_a(this);
        } else {
            return func_27063_c(obj);
        }
    }

    protected abstract Object func_27063_c(Object obj);
}
