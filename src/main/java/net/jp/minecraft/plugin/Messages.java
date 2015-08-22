package net.jp.minecraft.plugin;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Messages {

	public static void getTitle(CommandSender sender){
		sender.sendMessage("");
		sender.sendMessage(ChatColor.GOLD + "---------- SimpleMyPoints ----------");
	}

	public static void getVoidLine(CommandSender sender){
		sender.sendMessage("");
	}

	public static void getLine(CommandSender sender){
		sender.sendMessage(ChatColor.GOLD + "------------------------------------");
	}

	public static void getArgumentTooMany(CommandSender sender){
		sender.sendMessage(ChatColor.RED + "□ コマンドが不正です。(理由：引数が多すぎます)");
	}

	public static void getArgumentLess(CommandSender sender){
		sender.sendMessage(ChatColor.RED + "□ コマンドが不正です。(理由：引数が足りません)");
	}

	public static void getPermissonError(CommandSender sender , String permisson){
		sender.sendMessage(ChatColor.RED + "□ パーミッションがありません。("+ permisson +")");
	}

	public static void getNotingPoint(CommandSender sender , String point){
		sender.sendMessage(ChatColor.RED + "□ "+ point + "は登録されていないためテレポートできませんでした。");
	}
}
