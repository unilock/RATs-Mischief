package ladysnake.ratsmischief.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import ladysnake.ratsmischief.common.init.ModEnchantments;
import ladysnake.ratsmischief.common.item.RatMasterArmorItem;
import ladysnake.ratsmischief.common.item.RatMasterMaskItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@WrapOperation(method = "getPossibleEntries", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;isAvailableForRandomSelection()Z"))
	private static boolean mischief$storeEnchants(Enchantment enchantment, Operation<Boolean> original, @Share("storedEnchantment") LocalRef<Enchantment> storedEnchantment) {
		boolean originalResult = original.call(enchantment);
		if (originalResult) {
			storedEnchantment.set(enchantment);
		}
		return originalResult;
	}

	@WrapOperation(method = "getPossibleEntries", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentTarget;isAcceptableItem(Lnet/minecraft/item/Item;)Z"))
	private static boolean mischief$letTheEnchantDecide(EnchantmentTarget enchantmentTarget, Item item, Operation<Boolean> original, @Share("storedEnchantment") LocalRef<Enchantment> storedEnchantment) {
		if (storedEnchantment.get() == ModEnchantments.RAT_CURSE) {
			return item instanceof RatMasterArmorItem || item instanceof RatMasterMaskItem;
		}
		return original.call(enchantmentTarget, item);
	}
}
