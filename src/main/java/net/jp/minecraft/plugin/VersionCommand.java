package net.jp.minecraft.plugin;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class VersionCommand {

	private static final String COMMAND_NAME = "version";
	private static final String PERMISSON_NODE = "smp."+COMMAND_NAME;

	/*
	 *  helpコマンドの実行クラス
	 * @auther syokkendesuyo
	 */

	public static String getCommandName(){
		return COMMAND_NAME;
	}

	public static String getPermissonNode(){
		return PERMISSON_NODE;
	}
	public static void getVersion (CommandSender sender ,String version){
		Messages.getTitle(sender);
		sender.sendMessage(ChatColor.AQUA + " Version : " + version);
		sender.sendMessage(ChatColor.AQUA + " Auther  : syokkendesuyo");
		Messages.getVoidLine(sender);
	}
}
